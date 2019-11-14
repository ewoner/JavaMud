/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Mud.MyTimer;
import Server.Handler.NewHandler;
import Server.Protocol.NewProtocol;
import Server.ServerExceptions.ServerException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @param <P>
 * @author Administrator
 */
public class NewConnection<P extends NewProtocol> {

    private SocketChannel socket;
    private P protocol;
    private Stack<NewHandler> handlerstack;
    private long creationtime;
    private boolean closed;
    private Selector readSelector;
    private ByteBuffer readBuffer,  writeBuffer;
    private final int BUFFERSIZE = 8000;
    private CharsetDecoder asciiDecoder;
    private Charset ascii;
    private boolean isblocking;
    private String m_sendbuffer;
    private long m_lastSendTime;
    private boolean m_checksendtime;

    public NewConnection(P protocol) {
        this.protocol = protocol;
        handlerstack = new Stack<NewHandler>();
        creationtime = MyTimer.GetTimeMS();
        closed = false;
        ascii = Charset.forName("US-ASCII");
        asciiDecoder = ascii.newDecoder();
        readBuffer = ByteBuffer.allocateDirect(BUFFERSIZE);
        writeBuffer = ByteBuffer.allocateDirect(BUFFERSIZE);
        m_sendbuffer = "";
    }

    public NewConnection(SocketChannel socket, P protocol) throws ServerException {
        this(protocol);
        try {
            this.socket = socket;
            readSelector = Selector.open();
            socket.configureBlocking(false);
            socket.register(readSelector, SelectionKey.OP_READ, new StringBuffer());
        } catch (IOException ex) {
            throw new ServerException(ex, this.getClass().getName());
        }
    }

    public int GetBufferedBytes() {
        return m_sendbuffer.length();
    }

    public void BufferData(String p_buffer) {
        m_sendbuffer = m_sendbuffer.concat(p_buffer);
    }

    public long getLastSendTime() {
        return m_lastSendTime;
    }

    public void close() {
        closed = true;
    }

    public void closeSocket() throws ServerException {
        try {
            getSocket().close();
        } catch (IOException ex) {
            throw new ServerException(ex, this.getClass().getName());
        }
        if (getHandler() != null) {
            getHandler().leave();
        }
        while (getHandler() != null) {
            handlerstack.pop();
        }
    }

    public long getCreationTime() {
        return creationtime;
    }

    public boolean isClosded() {
        return closed;
    }

    public void switchHandler(NewHandler handler) {
        if (getHandler() != null) {
            getHandler().leave();     // leave the current state if it exists
            handlerstack.pop();   // pop the pointer off
        }
        handlerstack.push(handler);
        handler.enter();     // enter the new state
    }

    public void addHandler(NewHandler handler) {
        if (getHandler() != null) {
            getHandler().leave(); // leave the current state if it exists
        }
        handlerstack.push(handler);
        handler.enter();     // enter the new state
    }

    public void removeHandler() {
        getHandler().leave();     // leave current state
        handlerstack.pop();   // pop the pointer off
        if (getHandler() != null) // if old state exists,
        {
            getHandler().enter(); // tell it connection has re-entered
        }
    }

    public NewHandler getHandler() {
        if (handlerstack.size() == 0) {
            return null;
        }
        return handlerstack.peek();
    }

    public void clearHandlers() {
        // leave the current handler
        if (getHandler() != null) {
            getHandler().leave();
        }
        // delete all the handlers on the stack
        while (getHandler() != null) {
            handlerstack.pop();
        }
    }

    public void sendBuffer() throws ServerException {
        if (m_sendbuffer.length() > 0) {
            // send the data, and erase as much as was sent from the buffer.
            int sent = send(m_sendbuffer);
            if (sent >= m_sendbuffer.length()) {
                m_sendbuffer = "";
            } else {
                m_sendbuffer = m_sendbuffer.substring(sent);
            }
            if (sent > 0) {
                // since data was sent successfully, reset the send time.
                m_lastSendTime = MyTimer.GetTimeS();
                m_checksendtime = false;
            } else {
                // no data was sent, so mark down that the sending time
                // needs to be checked, to see if the socket is entering
                // client deadlock
                if (!m_checksendtime) {
                    // if execution gets here, that means that this connection
                    // has previously not had any problems sending data, so
                    // mark down the time that the sending problem started.
                    // note that it may have started earlier, but since we didn't
                    // start sending data earlier, there really is no way to know
                    // for sure when it started.
                    m_checksendtime = true;
                    m_lastSendTime = MyTimer.GetTimeS();

                }
            }   // end no-data-sent check

        }   // end buffersize check}
    }

    public int send(String data) throws ServerException {
        prepWriteBuffer(data);
        long nbytes = 0;
        long toWrite = writeBuffer.remaining();
        try {
            while (nbytes != toWrite) {
                nbytes += getSocket().write(writeBuffer);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
            Logger.getLogger(NewConnection.class.getName()).info("New Connection: Sent---->\n" + data + "\n");

        } catch (Exception e) {
            throw new ServerException(e, this.getClass().getName());
        }
        writeBuffer.rewind();
        return (int) nbytes;
    }

    public void receive() throws ServerException {
        try {
            //Logger.getLogger(NewConnection.class.getName()).info("New Connection: receive");
            int keysReady = keysReady = getReadSelector().select();

            if (keysReady > 0) {
                Set readyKeys = getReadSelector().selectedKeys();

                for (Iterator i = readyKeys.iterator(); i.hasNext();) {
                    SelectionKey key = (SelectionKey) i.next();
                    i.remove();
                    String result = readRequest(key);
//                    System.out.println("<----- NewConnection.recieve()\n" + result + "----->");
                    protocol.Translate(this, result.toCharArray(), result.length());
                }
            }
        } catch (Exception ex) {
            throw new ServerException(ex, this.getClass().getName());
        }
    }

    public P getProtocol() {
        return protocol;
    }

    private void prepWriteBuffer(String result) {
        writeBuffer.clear();
        result += "\r\n";
        writeBuffer.put(result.getBytes());
        writeBuffer.flip();
    }

    private String readRequest(SelectionKey key) throws ServerException {
//        System.out.println("<------ NC:  readRequest ------>");
        String result = "";
        SocketChannel incomingChannel = (SocketChannel) key.channel();
        try {
            int bytesRead = incomingChannel.read(readBuffer);
            if (bytesRead == -1) {
                Logger.getLogger(NewConnection.class.getName()).warning("disconnect:" + incomingChannel.socket().getInetAddress() +
                        ", end-of-stream");
                closed = true;
            }
            readBuffer.flip();
            result = asciiDecoder.decode(readBuffer).toString();
            readBuffer.clear();
        //System.out.println(result);

        } catch (IOException ie) {
            if (ie.getMessage() != null && ie.getMessage().contains("forcibly closed")) {
                Logger.getLogger(NewConnection.class.getName()).warning("disconnect:" + incomingChannel.socket().getInetAddress() +
                        ", forcibly cloaded.");
                closed = true;
            } else {
                Logger.getLogger(NewConnection.class.getName()).log(Level.SEVERE, "ERROR in readRequest()--" + ie.toString(), ie);
                throw new ServerException(ie, this.getClass().getName(), "ERROR in readRequest() --");
            }
        } catch (Exception ioe) {
            Logger.getLogger(NewConnection.class.getName()).log(Level.SEVERE, "ERROR in readRequest()--" + ioe.toString(), ioe);
            throw new ServerException(ioe, this.getClass().getName(), "ERROR in readRequest() --");
        }
        return result;
    }

    void setBlocking(boolean blockmode) throws ServerException {
        try {
            this.getSocket().configureBlocking(blockmode);
        } catch (IOException ex) {
            throw new ServerException(ex, this.getClass().getName());
        }
        isblocking = blockmode;
    }

    /**
     * @return the socket
     */
    public SocketChannel getSocket() {
        return socket;
    }

    /**
     * @return the readSelector
     */
    public Selector getReadSelector() {
        return readSelector;
    }
}
