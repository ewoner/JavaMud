/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Handler;

import Server.NewConnection;
import Server.Protocol.NewProtocol;
import Server.Protocol.Telnet;



/**
 *
 * @author Administrator
 */
abstract public class NewHandler<P extends NewProtocol, C> {

    private NewConnection<P> connection;
    
    public NewHandler(NewConnection<P> conn){
        connection = conn;
    }

    /**
     * 
     * @param con
     */
    static public void NoRoom(NewConnection<Telnet> con) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    abstract public void enter();

    abstract public void leave();

    abstract public void hungUp();

    abstract public void  flooded();

    abstract public void handle(C command);

    /**
     * @return the connection
     */
    public NewConnection<P> getConnection() {
        return connection;
    }
    
}
