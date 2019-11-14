/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Handler;

import Server.Handler.*;
import Entities.Account;
import Entities.MudCharacter;
import Server.NewConnection;
import Server.Protocol.Telnet;

/**
 *
 * @author Administrator
 */
public class CreationHandler extends NewHandler<Telnet, String> {

    public CreationHandler(NewConnection<Telnet> connection) {
        super(connection);
    }

    @Override
    public void enter() {
        printMenu();
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

    @Override
    public void handle(String command) {
        int option = Integer.parseInt(command);
        switch (option) {
            case 0:
                getConnection().close();
                break;
            case 1:
                getConnection().addHandler(new CreateMobMenu(getConnection()));
                break;
//            case 2:
//                if (account.numCharacters() >= account.getAllowedCharacters()) {
//                    getConnection().getProtocol().sendString(getConnection(), "<#FF0000>Sorry, you are not allowed any more characters.\r\n");
//                    return;
//                }
//                getConnection().addHandler(new TelnetMenuNew(getConnection(), account));
//                break;
//            case 3:
//                getConnection().addHandler(new TelnetMenuDelete(getConnection(), account));
//                break;
//            case 4:
//                getConnection().addHandler(new TelnetMenuHelp(getConnection(), account));
//                break;
//            case 5:
//                getConnection().addHandler(new TelnetMenuHelp(getConnection(), account));
//                break;
        }
    }

    private void printMenu() {
        String str = Telnet.clearscreen +
                "<#FFFFFF>-------------------------------------------------------------------------------\r\n" +
                "<#FFFF00> Creation v1.0 Main Menu\r\n" +
                "<#FFFFFF>-------------------------------------------------------------------------------\r\n" +
                " 0 - Quit\r\n" +
                " 1 - Mob\r\n" +
                " 2 - Item\r\n" +
                " 3 - Room\r\n" +
                " 4 - Region\r\n" +
                " 5 - Portal\r\n" +
                "-------------------------------------------------------------------------------\r\n" +
                "<#7F7F7F> Enter Choice: <#FFFFFF>";
        getConnection().getProtocol().sendString(getConnection(), str);
    }

    private class CreateMobMenu extends NewHandler<Telnet, String> {

        public CreateMobMenu(NewConnection<Telnet> connection) {
            super(connection);
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

        @Override
        public void handle(String command) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
