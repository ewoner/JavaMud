/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Handler;

import Server.NewConnection;
import Server.Protocol.Telnet;


/**
 *
 * @author Administrator
 */
public class DefaultHandler extends NewHandler<Telnet, String> {

    /**
     * 
     * @param con
     */
    
    /*public static void NoRoom(NewConnection<Telnet> con) {
    throw new UnsupportedOperationException("Not yet implemented");
    }*/
    public DefaultHandler(NewConnection<Telnet> con) {
        super(con);
    }

    @Override
    public void handle(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void Enter() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void Leave() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void HungUp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void Flooded() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void enter() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void leave() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void hungUp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void flooded() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
