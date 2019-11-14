/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Server.ServerExceptions.ServerException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
class ListenManager extends Thread {

    private List<ServerSocketChannel> sockets = new ArrayList<ServerSocketChannel>();
    private NewConnectionManager manager;
    private final int MAX = 10;
    private Selector acceptSelector;
    private boolean running = false;
    private Logger log = Logger.getLogger(this.getClass().getName());

    public ListenManager() throws ServerException {
        super("ListenManager");
        try {
            log.info("ListenManager initialization.....");
            manager = null;
            acceptSelector = Selector.open();
        } catch (IOException ex) {
            throw new ServerException(ex, this.getClass().getName());
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        for (ServerSocketChannel ssc : sockets) {
            ssc.close();
        }
    }

    void addPort(int port) throws Exception {
//        ServerSocketChannel ssc = ServerSocketChannel.open();
//        ssc.configureBlocking(false);
//        InetAddress address = InetAddress.getLocalHost();
//        ssc.socket().bind(new InetSocketAddress(address, port));
//        Logger.getLogger(ListenManager.class.getName()).info("Bound to " + address);
//        ssc.register(this.acceptSelector, SelectionKey.OP_ACCEPT);

        if (sockets.size() == MAX) {
            throw new Exception("Socket Limit Reached");
        }

        // create a new socket
        ServerSocketChannel lsock = ServerSocketChannel.open();

        // make the socket non-blocking, so that it won't block if a
        // connection exploit is used when accepting (see Chapter 4)
        lsock.configureBlocking(false);

        // listen on the requested port
        InetAddress address = InetAddress.getLocalHost();
        lsock.socket().bind(new InetSocketAddress(address, port));
        log.info("Bound to " + address);
        lsock.register(this.acceptSelector, SelectionKey.OP_ACCEPT);
        sockets.add(lsock);
    }

    @Override
    public void run() {
        log.info("ListenManager.run(): Selecting");
        System.out.println("<----- ListenManager.run() ----->");
        while (isRunning()) {
            try {
                acceptSelector.select();
                Set readyKeys = acceptSelector.selectedKeys();
                for (Iterator i = readyKeys.iterator(); i.hasNext();) {
                    SelectionKey key = (SelectionKey) i.next();
                    i.remove();
                    ServerSocketChannel readyChannel = (ServerSocketChannel) key.channel();
                    SocketChannel incomingChannel = readyChannel.accept();
                    log.info("AcceptThread: Connection from " + incomingChannel.socket().getInetAddress());
                    manager.newConnection(incomingChannel);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        log.info("ListenManager.run(): Has Stopped");
        System.out.println("<----- ListenManager HAS STOPPED ----->");


    }

    void setConnectionManager(NewConnectionManager manager) {
        // set the new action function
        this.manager = manager;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }
}
