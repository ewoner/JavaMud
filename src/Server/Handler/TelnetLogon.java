/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Handler;


import Entities.Account;
import Entities.enums.AccessLevel;
import Mud.Mud;
import Databases.AccountDatabase;
import Databases.Group.DatabaseGroup;
import Server.NewConnection;
import Server.Protocol.Telnet;
import Server.ServerExceptions.ServerException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class TelnetLogon extends NewHandler<Telnet, String> {

    final private String newaccountFile = "data/logon/newaccount.data";
    final private String logonFile = "data/logon/logon.data";
    static public DatabaseGroup dbg = Mud.dbg;
    private LogonState state;
    private int errors;     // how many times has an invalid answer been entered?
    String name;              // name
    String pass;              // password
    Account account;         // account id
    static public AccountDatabase AccountDB = dbg.accountDB;

    public TelnetLogon(NewConnection<Telnet> conn) {
        super(conn);
        state = LogonState.ENTERNAME;
        errors = 0;
    }

    public static void noRoom(NewConnection<Telnet> conn) throws ServerException {
        String msg = "Sorry, there is no more room on this server.\r\n";
        conn.send(msg);
    }

    public void GotoMenu() {
        NewConnection<Telnet> c = getConnection();
        Account id = account;
        c.removeHandler();
        c.addHandler(new TelnetMenu(c, id));
    }

    @Override
    public void enter() {
        // load the logon message
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(logonFile));
            String str = "";
            while (reader.ready()) {
                str += reader.readLine() + "\r\n";
            }
            getConnection().getProtocol().sendString(getConnection(), str);
        } catch (IOException ex) {
            Logger.getLogger(TelnetLogon.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(TelnetLogon.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
        Account id = null;
        if (errors == 5) {
            getConnection().getProtocol().sendString(getConnection(), "<#FF0000>Too many incorrect responses, closing connection...\r\n");
            getConnection().close();
            return;
        }
        if (state == LogonState.ENTERNAME) // has not entered name yet
        {
            if (command.toLowerCase().equals("new")) {
                // get the new account message
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(newaccountFile));
                    String str = null;
                    while (reader.ready()) {
                        str += reader.readLine();
                    }
                    // switch to the new state and tell the connection.
                    state = LogonState.ENTERNEWNAME;
                    getConnection().getProtocol().sendString(getConnection(), Telnet.clearscreen + str);
                } catch (IOException ex) {
                    Logger.getLogger(TelnetLogon.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        reader.close();
                    } catch (IOException ex) {
                        Logger.getLogger(TelnetLogon.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                id = AccountDB.findname(command);
                if (id == null) {
                    // name does not exist
                    errors++;
                    getConnection().getProtocol().sendString(getConnection(),
                            "<#FF0000>Sorry, the account \"<#FFFFFF>" + command +
                            "<#FF0000>\" does not exist.\r\n" +
                            "Please enter your name, or \"new\" if you are new: <#FFFFFF>");
                } else {
                    // name exists, go to password entrance.
                    state = LogonState.ENTERPASS;
                    account = id;
                    Account a = id;
                    if (a.isBanned()) {
                        getConnection().getProtocol().sendString(getConnection(), "<#FF0000>SORRY! You are BANNED!");
                        getConnection().close();
                        state = LogonState.ENTERDEAD;
                        return;
                    }
                    name = a.getName();
                    pass = a.getPassword();
                    getConnection().getProtocol().sendString(getConnection(),
                            "<#00FF00>Welcome, <#FFFFFF>" + name +
                            "\r\n<#00FF00>Please enter your password: <#FFFFFF>");
                }
            }
            return;
        }
        if (state == LogonState.ENTERNEWNAME) {
            // check if the name is taken:
            id = AccountDB.findname(command);
            if (id != null) {
                errors++;
                getConnection().getProtocol().sendString(getConnection(),
                        "<#FF0000>Sorry, the account name \"<#FFFFFF>" + command +
                        "<#FF0000>\" has already been taken.\r\n" +
                        "<#FFFF00>Please enter another name: <#FFFFFF>");
            } else {
                if (!AccountDB.acceptibleName(command)) {
                    errors++;
                    getConnection().getProtocol().sendString(getConnection(),
                            "<#FF0000>Sorry, the account name \"<#FFFFFF>" + command +
                            "<#FF0000>\" is unacceptible.\r\n" +
                            "<#FFFF00>Please enter your desired name: <#FFFFFF>");
                } else {
                    state = LogonState.ENTERNEWPASS;
                    name = command;
                    getConnection().getProtocol().sendString(getConnection(),
                            "<#00FF00>Please enter your desired password: <#FFFFFF>");
                }
            }
            return;
        }

        if (state == LogonState.ENTERNEWPASS) {
            // make sure there's no whitespace.
            boolean flag = false;
            if (command.contains("\t")) {
                flag = true;
            }
            if (command.contains(" ")) {
                flag = true;
            }
            if (flag) {
                errors++;
                getConnection().getProtocol().sendString(getConnection(),
                        "<#FF0000>INVALID PASSWORD!\r\n" +
                        "<#00FF00>Please enter your desired password: <#FFFFFF>");
                return;
            }

            getConnection().getProtocol().sendString(getConnection(),
                    "<#00FF00>Thank you! You are now entering the realm...\r\n");
            // create and get the new account.
            int sizeflag = AccountDB.size();
            id = AccountDB.create(name, command);
            account = id;
            // make the player the administrator if he's the first to log in.
            if (sizeflag == 0) {
                id.setAccesslevel(AccessLevel.Admin);
            }
            // enter the game menu
            GotoMenu();
            return;
        }
        if (state == LogonState.ENTERPASS) {
            if (pass.equals(command)) {
                getConnection().getProtocol().sendString(getConnection(),
                        "<#00FF00>Thank you! You are now entering the realm...\r\n");

                // enter the game
                GotoMenu();
            } else {
                errors++;
                getConnection().getProtocol().sendString(getConnection(),
                        "<#FF0000>INVALID PASSWORD!\r\n" +
                        "<#FFFF00>Please enter your password: <#FFFFFF>");
            }
            return;
        }
    }

    enum LogonState {

        ENTERNAME, // enter your name, or "new"
        ENTERNEWNAME, // new account, enter new name
        ENTERNEWPASS, // new account, enter new password
        ENTERPASS, // enter password
        ENTERDEAD               // dead state... to prevent banning workarounds
    }
}
