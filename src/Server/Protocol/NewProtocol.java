/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Protocol;

import Server.NewConnection;


/**
 *
 * @author Administrator
 */
public interface NewProtocol<P extends NewProtocol> {

    void Translate(NewConnection<P> connection, char[] buffer, int size) ;
}
