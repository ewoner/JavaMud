/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Handler;

import Entities.Account;
import Entities.MudCharacter;
import Mud.Mud;
import Databases.AccountDatabase;
import Databases.CharacterDatabase;
import Databases.Group.DatabaseGroup;
import Server.NewConnection;
import Server.Protocol.Telnet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the main menu class for the opening of the mud.
 *
 *  @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.0.1
 * 1.  Fixed an issue that the deleted character was not being removed from the dbg and only the account.
 * Version 1.0.0
 */
public class TelnetMenu extends NewHandler<Telnet, String> {

    Account account;
    String helpFile = "data/logon/help.data";
    static public DatabaseGroup dbg = Mud.dbg;
    static public AccountDatabase AccountDB = dbg.accountDB;
    static public CharacterDatabase CharacterDB = dbg.characterDB;

    public TelnetMenu(NewConnection<Telnet> c, Account id) {
        super(c);
        account = id;
    }

    @Override
    public void enter() {
        printMenu();
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
        int option = 5;
        try {
            option = Integer.parseInt(command);
        } catch (Exception ex) {
        }

        switch (option) {
            case 0:
                getConnection().close();
                break;
            case 1:
                getConnection().addHandler(new TelnetMenuEnter(getConnection(), account));
                break;
            case 2:
                if (account.numCharacters() >= account.getAllowedCharacters()) {
                    getConnection().getProtocol().sendString(getConnection(), "<#FF0000>Sorry, you are not allowed any more characters.\r\n");
                    return;
                }
                getConnection().addHandler(new TelnetMenuNew(getConnection(), account));
                break;
            case 3:
                getConnection().addHandler(new TelnetMenuDelete(getConnection(), account));
                break;
            case 4:
                getConnection().addHandler(new TelnetMenuHelp(getConnection(), account));
                break;

        }
    }

    private void printMenu() {
        String str = Telnet.clearscreen
                + "<#FFFFFF>-------------------------------------------------------------------------------\r\n"
                + "<#FFFF00> MyMud v1.0 Main Menu\r\n"
                + "<#FFFFFF>-------------------------------------------------------------------------------\r\n"
                + " 0 - Quit\r\n"
                + " 1 - Enter the Game\r\n"
                + " 2 - Create a new Character\r\n"
                + " 3 - Delete an existing Character\r\n"
                + " 4 - View Help\r\n"
                + "-------------------------------------------------------------------------------\r\n"
                + "<#7F7F7F> Enter Choice: <#FFFFFF>";

        getConnection().getProtocol().sendString(getConnection(), str);
    }

    private class TelnetMenuHelp extends NewHandler<Telnet, String> {

        private Account acount;

        TelnetMenuHelp(NewConnection<Telnet> con, Account id) {
            super(con);
            acount = id;
        }

        @Override
        public void enter() {
            printHelp();
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
            getConnection().removeHandler();
        }

        private void printHelp() {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(helpFile));
                String str = null;
                while (reader.ready()) {
                    str += reader.readLine();
                }
                getConnection().getProtocol().sendString(getConnection(), Telnet.clearscreen + str);
            } catch (IOException ex) {
                Logger.getLogger(TelnetMenu.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(TelnetMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private class TelnetMenuNew extends NewHandler<Telnet, String> {

        Account m_account;         // account
        MudCharacter m_char;            // id of new character
        int classNum = 0;
        int raceNum = 0;
        CreateCharacter creator;

        TelnetMenuNew(NewConnection<Telnet> p_conn, Account p_account) {
            super(p_conn);
            m_account = p_account;

            // clear the ID
            m_char = null;


        }

        @Override
        public void handle(String p_data) {

            // if m_char != 0, then that means the user has selected a character
            // to create. Get a name.
            if (m_char != null) {
                if (!AccountDB.acceptibleName(p_data)) {
                    getConnection().getProtocol().sendString(
                            getConnection(),
                            "<#FF0000>Sorry, that name is not acceptible\r\n"
                            + "<#00FF00>Please enter your desired name: <#FFFFFF>");
                    return;
                }

                if (CharacterDB.findname(p_data) != null) {
                    getConnection().getProtocol().sendString(
                            getConnection(),
                            "<#FF0000>Sorry, that name is already taken\r\n"
                            + "<#00FF00>Please enter your desired name: <#FFFFFF>");
                    return;
                }

                MudCharacter c = m_char;
                c.setName(p_data);
                getConnection().removeHandler();
                return;
            }

            // get the number the user typed.
            int option = Integer.parseInt(p_data.trim());

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
            if (raceNum == 0) {
                raceNum = option;
                getConnection().getProtocol().sendString(getConnection(), CreateCharacter.listClasses);
                return;
            }
            if (classNum == 0) {
                classNum = option;
            }

            //create the character
            m_char = dbg.characterDB.generate(option + 1000);

            // check if it was valid
            if (m_char == null) {
                getConnection().getProtocol().sendString(getConnection(),
                        "<#FF0000>Invalid option, please try again: <#FFFFFF>");
                return;
            }

            m_char.setAccount(m_account.getID());
            m_account.addCharacter(m_char.getID());

            // now perform the inital setup:
            CreateCharacter.setup(m_char.getID(), raceNum, classNum);


            // ask for name
            getConnection().getProtocol().sendString(getConnection(),
                    "<#00FF00>Please enter your desired name: <#FFFFFF>");
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

        void printRaces() {
            getConnection().getProtocol().sendString(getConnection(), CreateCharacter.listRases);
        }
    }  // end class TelnetMenuNew

    private class TelnetMenuDelete extends NewHandler<Telnet, String> {

        Account m_account;         // account
        MudCharacter m_char;      // id of new character

        TelnetMenuDelete(NewConnection<Telnet> p_conn, Account p_account) {
            super(p_conn);
            // clear the ID
            m_char = null;
            m_account = p_account;
        }

        @Override
        public void handle(String p_data) {
            // if m_char != 0, then that means the user has selected a character
            // to delete. Confirm that he typed "Y" or "y", and then baleet it.
            if (m_char != null) {
                if (!p_data.equalsIgnoreCase("y")) {
                    getConnection().removeHandler();
                    return;
                }

                // Baleet the character
                m_account.removeCharacter(m_char.getID());
                dbg.characterDB.erase(m_char.getID());
                getConnection().removeHandler();
                return;
            }

            // get the number the user typed.
            int option = Integer.parseInt(p_data.trim());

            // if 0, exit state
            if (option == 0) {
                getConnection().removeHandler();
                return;
            }
            MudCharacter c = Mud.dbg.characterDB.get(option);
            if (!m_account.getCharacters().contains(c.getID())) {
                getConnection().getProtocol().sendString(
                        getConnection(),
                        "<#FF0000>INVALID CHARACTER NUMBER\r\n"
                        + "<#FFFFFF>Enter Number of Character to delete: ");
                return;
            }

            // grab the ID of the character you want to delete.
            //create the character
            m_char = c;

            // ask for confirmation
            getConnection().getProtocol().sendString(getConnection(),
                    "<#FF0000>Really Delete Character? (Y/N) ");
        }

        @Override
        public void enter() {
            PrintCharacters();
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

        public void PrintCharacters() {
            String str =
                    "<#7F7F7F>-------------------------------------------------------------------------------\r\n"
                    + "<#FFFF00> Your Characters\r\n"
                    + "<#7F7F7F>-------------------------------------------------------------------------------\r\n";

            String chars = " 0   - Go Back\r\n";
            for (Integer k : this.m_account.getCharacters()) {
                MudCharacter ch = dbg.characterDB.get(k);
                chars += ch.getID() + "   - " + ch.getName() + "\r\n";
            }
            chars += "<#7F7F7F>-------------------------------------------------------------------------------\r\n"
                    + "Enter number of character to delete: ";

            getConnection().getProtocol().sendString(getConnection(), str + chars);
        }
    };  // end class TelnetMenuDelete

    class TelnetMenuEnter extends NewHandler<Telnet, String> {

        Account m_account;         // account

        TelnetMenuEnter(NewConnection<Telnet> p_conn, Account p_account) {
            super(p_conn);
            m_account = p_account;
        }

        @Override
        public void handle(String p_data) {
            // get the number the user typed.
            int option = Integer.parseInt(p_data.trim());

            // if 0, exit state
            if (option == 0) {
                getConnection().removeHandler();
                return;
            }
            MudCharacter c = Mud.dbg.characterDB.get(option);
            if (!m_account.getCharacters().contains(c.getID())) {
                getConnection().getProtocol().sendString(
                        getConnection(),
                        "<#FF0000>INVALID CHARACTER NUMBER\r\n"
                        + "<#FFFFFF>Enter Number of Character to use: ");
                return;
            }
            // grab the ID of the character you want to use.
            MudCharacter m_char = c;

            // remove this handler and go to the game
            getConnection().switchHandler(new TelnetGame(getConnection(), m_account, m_char));
        }

        @Override
        public void enter() {
            PrintCharacters();
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

        void PrintCharacters() {
            String str =
                    "<#7F7F7F>-------------------------------------------------------------------------------\r\n"
                    + "<#FFFF00> Your Characters\r\n"
                    + "<#7F7F7F>-------------------------------------------------------------------------------\r\n";

            String chars = " 0   - Go Back\r\n";
            for (Integer k : this.m_account.getCharacters()) {
                MudCharacter ch = dbg.characterDB.get(k);
                chars += ch.getID() + "   - " + ch.getName() + "\r\n";
            }
            chars += "<#7F7F7F>-------------------------------------------------------------------------------\r\n"
                    + "Enter number of character to use: ";

            getConnection().getProtocol().sendString(getConnection(), str + chars);

        }
    }
}
