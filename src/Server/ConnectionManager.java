package Server;

import Server.ServerExceptions.ServerException;
import java.nio.channels.SocketChannel;

/**
 *
 * @author ewone
 */
public interface ConnectionManager {

    int availableConnections();

    void close(NewConnection toClose) throws ServerException;

    void closeConnections() throws ServerException;

    void listen() throws ServerException;

    /**
     *
     * @throws Server.ServerExceptions.ServerException
     */
    void manage() throws ServerException;

    void newConnection(SocketChannel socket) throws ServerException;

    void send() throws ServerException;

    void shutdown() throws ServerException;

    int totalConnections();
    
}
