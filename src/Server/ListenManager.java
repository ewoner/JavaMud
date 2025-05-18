package Server;

import Server.ServerExceptions.ServerException;
import java.io.IOException;
import java.util.logging.Logger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class ListenManager extends Thread {

    private Selector selector;
    private Map<Integer, ServerSocketChannel> sockets = new HashMap<>();
    private NewConnectionManager manager;
    private boolean running = false;
    private Logger log = Logger.getLogger(this.getClass().getName());

    public ListenManager() throws ServerException {
        super("ListenManager");
        try {
            log.info("ListenManager initialization.....");
            manager = null;
            selector = Selector.open();
        } catch (IOException ex) {
            throw new ServerException(ex, this.getClass().getName());
        }
    }

    public void addPort(int port) throws Exception {
        try {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            InetAddress address = InetAddress.getLocalHost();
            ssc.socket().bind(new InetSocketAddress(address, port));
            log.info("Bound to " + address);
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            sockets.put(port, ssc);
        } catch (IOException ex) {
            throw new Exception("Failed to add port", ex);
        }
    }

    @Override
    public void run() {
        log.info("ListenManager.run(): Selecting");
        System.out.println("<----- ListenManager.run() ----->");
        while (isRunning()) {
            try {
                selector.select();
                Set<SelectionKey> readyKeys = selector.selectedKeys();
                for (Iterator<SelectionKey> i = readyKeys.iterator(); i.hasNext();) {
                    SelectionKey key = i.next();
                    i.remove();
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    SocketChannel incomingChannel = ssc.accept();
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

    public void setConnectionManager(NewConnectionManager manager) {
        this.manager = manager;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void close() throws IOException {
        for (ServerSocketChannel ssc : sockets.values()) {
            ssc.close();
        }
        selector.close();
    }
}
