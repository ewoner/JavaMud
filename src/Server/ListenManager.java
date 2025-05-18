package Server;

import Server.ServerExceptions.ServerException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ListenManager extends Thread {

    private final ExecutorService portBinder = Executors.newCachedThreadPool();

    /**
     * Maps port numbers to the respective server socket channels
     */
    private Map<Integer, ServerSocketChannel> sockets = new ConcurrentHashMap<>();
    /**
     * A reference to the selector used to monitor the sockets
     */
    private Selector selector;
    /**
     * The manager responsible for creating new connections
     */
    private ConnectionManager manager;
    /**
     * Whether or not the thread should be running
     */
    private volatile boolean running = false;
    /**
     * Logger for debug messages
     */
    private Logger log = Logger.getLogger(this.getClass().getName());

    /**
     * Constructs a new {@code ListenManager} instance.
     * <p>
     * Initializes the ListenManager by opening a {@link Selector} for managing
     * non-blocking I/O channels. Logs the initialization process and handles
     * potential {@link IOException} by logging a severe error and throwing a
     * {@link RuntimeException}.
     *
     * @throws RuntimeException if the selector cannot be opened due to an I/O
     * error.
     */
    public ListenManager(ConnectionManager manager) {
        // Set the thread name for debugging purposes
        super("ListenManager");
        // Log the initialization of ListenManager
        log.fine("Initializing ListenManager");
        // Initialize the connection manager to null
        this.manager = manager;
        try {
            // Open a new selector for managing non-blocking I/O
            selector = Selector.open();
        } catch (IOException ex) {
            // Log a severe error if the selector cannot be opened
            log.severe("Could not open selector.  Failing.");
            // Throw a RuntimeException to indicate initialization failure
            throw new RuntimeException(ex);
        }
    }

    /**
     * Adds a port to the list of ports to listen on
     *
     * @param port The port to listen on
     */
    public void addPort(int port) {
        portBinder.execute(() -> {
            try {
                log.log(Level.FINE, "Attempting to add port: {0}", port);
                ServerSocketChannel ssc = ServerSocketChannel.open();
                ssc.configureBlocking(false);
                ssc.socket().bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
                ssc.register(selector, SelectionKey.OP_ACCEPT);
                sockets.put(port, ssc);
                log.log(Level.INFO, "Port {0} added successfully.", port);
            } catch (IOException ex) {
                log.log(Level.SEVERE, "IOException occurred while adding port {0}: {1}", new Object[]{port, ex.getMessage()});
            }
        });

    }

    /**
     * Sets the connection manager that will handle new connections. This method
     * should be used to set the connection manager for this listen manager.
     * This manager will be used to create new connections when a new connection
     * is accepted.
     *
     * @param manager The manager to set
     */
    public void setConnectionManager(ConnectionManager manager) {
        this.manager = manager;
        // Log that the connection manager has been set
        log.fine("Setting connection manager for ListenManager.");
    }

    /**
     * Sets the running state of the thread.
     *
     * <p>
     * This method updates the internal running state of the thread. It is
     * typically called to start or stop the thread's execution.
     *
     * @param running {@code true} to indicate that the thread should be
     * running, {@code false} to stop it.
     */
    public synchronized void setRunning(boolean running) {
        if (this.running != running) {
            this.running = running;
            selector.wakeup();
            log.fine(running ? "Starting ListenManager thread." : "Stopping ListenManager thread.");
        }
    }

    /**
     * Returns whether or not the thread is running
     *
     * @return Whether the thread is running
     */
    public boolean isRunning() {
        boolean currentState = this.running;
        log.log(Level.FINE, "Checking if ListenManager thread is running: {0}", currentState);
        return currentState;
    }

    /**
     * Closes the sockets and the selector
     *
     * @throws IOException If the sockets or selector could not be closed
     */
    public void close() {
        portBinder.shutdown();
        log.fine("Closing all sockets and selector.");
        selector.wakeup();
        if (sockets != null) {
            for (ServerSocketChannel ssc : sockets.values()) {
                if (ssc != null) {
                    log.log(Level.FINE, "Closing socket: {0}", ssc);
                    try {
                        ssc.close();
                    } catch (IOException e) {
                        log.log(Level.SEVERE, "Error closing a socket: {0}", e.getMessage());
                    }
                } else {
                    log.severe("Tried to close a null socket.");
                }
            }
        } else {
            log.severe("Tried to close a null list of sockets.");
        }
        if (selector != null) {
            log.fine("Closing selector.");
            try {
                selector.close();
            } catch (IOException e) {
                log.log(Level.SEVERE, "Error closing the selector: {0}", e.getMessage());
            }
        } else {
            log.severe("Tried to close a null selector.");
        }
        log.fine("Closing complete.");
    }

    @Override
    public void run() {
        try {
            while (isRunning()) {
                selector.select(500);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                for (SelectionKey key : selectedKeys) {
                    if ((key.isValid()) && (key.isAcceptable())) {
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                        SocketChannel incomingChannel = serverSocketChannel.accept();
                        if (!incomingChannel.isOpen()) {
                            key.cancel();
                            incomingChannel.close();
                            log.warning("Connection closed unexpectedly.");
                        }
                        log.log(Level.INFO, "AcceptThread: Connection from {0}", incomingChannel.socket().getInetAddress());
                        if (manager != null) {
                            try {
                                manager.newConnection(incomingChannel);
                            } catch (ServerException ex) {
                                log.log(Level.SEVERE, "An error occurred while creating a new connection", ex);
                            }
                        } else {
                            log.severe("No connection manager set. Cannot create new connection.");
                        }
                    }
                }
            }
        } catch (IOException ex) {
            log.log(Level.SEVERE, "An error occurred while selecting for incoming connections", ex);
        }
    }
}
