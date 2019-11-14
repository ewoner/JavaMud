/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Handler;

import Entities.Account;
import Entities.MudCharacter;
import Mud.Mud;
import Server.NewConnection;
import Server.Protocol.Telnet;

/**
 * The class that handles all new characters that are created with the menu.
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.0.0
 *
 */
public class TelnetNewCharacter extends NewHandler<Telnet, String> {

    Account m_account;         // account
    MudCharacter m_char;            // id of new character
    CreateCharacter creator;

    TelnetNewCharacter(NewConnection<Telnet> p_conn, Account p_account) {
        super(p_conn);
        m_account = p_account;

        // clear the ID
        m_char = null;
    }

    @Override
    public void enter() {
        printRaces();
    }

    @Override
    public void leave() {
    }

    @Override
    public void hungUp() {
    }

    @Override
    public void flooded() {
    }

    @Override
    public void handle(String command) {
        // if m_char != 0, then that means the user has selected a character
        // to create. Get a name.
        if (m_char != null) {
            if (!Mud.dbg.accountDB.acceptibleName(command)) {
                getConnection().getProtocol().sendString(
                        getConnection(),
                        "<#FF0000>Sorry, that name is not acceptible\r\n" +
                        "<#00FF00>Please enter your desired name: <#FFFFFF>");
                return;
            }

            if (Mud.dbg.characterDB.findname(command) != null) {
                getConnection().getProtocol().sendString(
                        getConnection(),
                        "<#FF0000>Sorry, that name is already taken\r\n" +
                        "<#00FF00>Please enter your desired name: <#FFFFFF>");
                return;
            }

            MudCharacter c = m_char;
            c.setName(command);
            getConnection().removeHandler();
            return;
        }

        // get the number the user typed.
        int option = Integer.parseInt(command.trim());

        // if 0, exit state
        if (option == 0) {
            getConnection().removeHandler();
            return;
        }

        // even though we checked this earlier, it is *critical* that you check it
        // again, because the user may decide to be "clever", and log in twice.
        // If he's logged in twice (or even more!), he could easily create more
        // characters than he's allowed, since the check is done when the user
        // enters this state.
        if (m_account.numCharacters() >= m_account.getAllowedCharacters()) {
            getConnection().getProtocol().sendString(
                    getConnection(),
                    "<#FF0000>Haha, nice try. You're not allowed any more characters!\r\n");
            return;
        }

        //create the character
        m_char = Mud.dbg.characterDB.generate(option);

        // check if it was valid
        if (m_char == null) {
            getConnection().getProtocol().sendString(getConnection(),
                    "<#FF0000>Invalid option, please try again: <#FFFFFF>");
            return;
        }

        m_char.setAccount(m_account.getID());
        m_account.addCharacter(m_char.getID());

        // now perform the inital setup:
        CreateCharacter.setup(m_char.getID(), option, option);

        // ask for name
        getConnection().getProtocol().sendString(getConnection(),
                "<#00FF00>Please enter your desired name: <#FFFFFF>");
    }

    private void printRaces() {
        getConnection().getProtocol().sendString(getConnection(), CreateCharacter.listRases);
    }
}
