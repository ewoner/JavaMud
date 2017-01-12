package Databases;

import Databases.Bases.DefaultInstanceDatabase;
import Entities.Account;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The database that holds accounts.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public class AccountDatabase extends DefaultInstanceDatabase<Account> {

    private final String accountfolder = "data/accounts/";

    /**
     * Creates a new account.
     *
     * @param name Name of the account
     * @param pass Password for the new account
     * @return The new account.
     */
    public Account create(String name, String pass) {
        int id = findOpenID();

        // get the account pointer
        Account a = create(id);
        a.setID(id);
        a.setName(name);
        a.setPassword(pass);
        a.setLoginTime(new Date());
        return a;

    }

    /**
     * Loads all accounts from the account folder.
     */
    public void load() {
        loadDirectory(accountfolder);
    }

    /**
     * Saves all accounts to the account folder.
     */
    public void save() {
        PrintWriter accountfile = null;
        try {
            String dir = accountfolder;
            // loop while there are accounts, saving each one to its own file:
            for (Account a : getContainer().values()) {
                String accountfilename = dir + a.getName() + ".data";
                accountfile = new PrintWriter(new BufferedWriter(new FileWriter(accountfilename)));
                saveEntity(accountfile, a);
                accountfile.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(AccountDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            accountfile.close();
        }
    }

    /**
     * Cheacks to make sure a name does not contain any special characters
     * and is of the right lenth.
     *
     * @param name The name to check.
     * @return TRUE if good to use.
     */
    public boolean acceptibleName(String name) {
        String inv = " \"'~!@#$%^&*+/\\[]{}<>()=.,?;:";
        for (int i = 0; i < name.length(); i++) {
            if (inv.contains("" + name.charAt(i))) {
                return false;
            }
        }
        if (name.length() > 16 || name.length() < 3) {
            return false;    // too long or too short
        }
        if (!Character.isLetter(name.charAt(0))) {
            return false;    // doesn't start with letter
        }
        return true;
    }

    @Override
    public Account create(int id) {
        Account a = new Account();
        a.setID(id);
        getContainer().put(id, a);
        return a;
    }
}
