package Server;

import Server.Handler.DefaultHandler;
import Server.Handler.TelnetLogon;
import Server.Protocol.Telnet;
import Server.ServerExceptions.ServerException;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Administrator
 */
public class NewConnectionManager implements ConnectionManager {

    static public List<NewConnection> connections = new ArrayList<>();
    final int sendTimeOut = 60;
    final int maxBuffed = 8192;

    public NewConnectionManager() {
    }

    @Override
    public void newConnection(SocketChannel socket) throws ServerException  {
        NewConnection<Telnet> con = new NewConnection<>(socket, new Telnet());
        if (availableConnections() != 0) {
            DefaultHandler.NoRoom(con);
            con.closeSocket();
        } else {
            connections.add(con);
            con.setBlocking(false);
            con.addHandler(new TelnetLogon(con));
        }
    }

    @Override
    public int availableConnections() {
        return 0;
    }

    @Override
    public int totalConnections() {
        return 0;
    }

    @Override
    public void closeConnections() throws ServerException {
        List<NewConnection> newList = new ArrayList<>();
        synchronized (newList) {
            while (newList.size() < connections.size()) {
                newList.add(new NewConnection(new Telnet()));
            }
            Collections.copy(newList, connections);
            for (NewConnection con : newList) {
                if (con.isClosded()) {
                    close(con);
                }
            }
        }
    }

    @Override
    public void listen() throws ServerException {
        List<NewConnection> newList = new ArrayList<>();
        synchronized (newList) {
            while (newList.size() < connections.size()) {
                newList.add(new NewConnection(new Telnet()));
            }
            Collections.copy(newList, connections);
            for (NewConnection con : newList) {
                int keysReady = 0;
                try {
                    keysReady = con.getReadSelector().selectNow();
                } catch (IOException ex) {
                    throw new ServerException(ex, this.getClass().getName());
                }
                if (keysReady > 0) {
                    Set readyKeys = con.getReadSelector().selectedKeys();
                    for (Iterator i = readyKeys.iterator(); i.hasNext();) {
                        i.remove();
                        con.receive();

                    }
                }

            }
        }
    }

    @Override
    public void send() throws ServerException {
        List<NewConnection> newList = new ArrayList<>();
        synchronized (newList) {
            while (newList.size() < connections.size()) {
                newList.add(new NewConnection(new Telnet()));
            }
            Collections.copy(newList, connections);
            for (NewConnection con : newList) {
                con.sendBuffer();
                if (con.GetBufferedBytes() > maxBuffed || con.getLastSendTime() > con.getLastSendTime() + sendTimeOut) {
                    con.close();
                    con.getHandler().hungUp();
                }
            }
        }
    }

    /**
     * 
     * @throws Server.ServerExceptions.ServerException
     */
    @Override
    public void manage() throws ServerException {
        listen();
        send();
        closeConnections();
    }

    @Override
    public void close(NewConnection toClose) throws ServerException {
        toClose.closeSocket();
        connections.remove(toClose);
    }

    @Override
    public void shutdown() throws ServerException {
        for (NewConnection con : connections) {
            con.closeSocket();
        }
    }
}
