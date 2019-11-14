package Entities;

import Entities.bases.Entity;
import Entities.enums.AccessLevel;
import Entities.interfaces.HasCharacters;
import Entities.interfaces.HasRoom;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * The basic Account Entity of the game.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public class Account extends Entity implements HasCharacters, HasRoom {

    private Set<Integer> characters;
    private String password;
    private Date loginTime;
    private AccessLevel accesslevel;
    private int allowedCharacters;
    private boolean banned;

    /**
     * Creates a bare bones new Account Entity.  
     * 
     * NOTE- DOES NOT ADD IT TO THE DATABASE.
     */
    public Account() {
        this(null, "UNDEFINED", new Date(), AccessLevel.Peon, 2, false);
    }

    private Account(String name, String pass, Date loginTime, AccessLevel access, int max_char, boolean banned) {
        setName(name);
        setPassword(pass);
        setLoginTime(loginTime);
        setAccesslevel(access);
        setAllowedCharacters(max_char);
        setBanned(banned);
        characters = new HashSet<Integer>(allowedCharacters);
    }

    @Override
    public void load(BufferedReader reader) throws IOException {
        String temp = reader.readLine();//[NAME]
        temp = reader.readLine();
        setName(temp);
        temp = reader.readLine();//[PASS]
        temp = reader.readLine();
        password = temp;
        temp = reader.readLine();//[FIRSTLOGINTIME]
        temp = reader.readLine();
        loginTime = new Date(Long.parseLong(temp));
        temp = reader.readLine();//[ACCESSLEVEL]
        temp = reader.readLine();
        accesslevel = AccessLevel.valueOf(temp);
        temp = reader.readLine();//[ALLOWEDCHARS]
        temp = reader.readLine();
        allowedCharacters = Integer.parseInt(temp);
        temp = reader.readLine();//[BANNED]
        temp = reader.readLine();
        banned = Boolean.parseBoolean(temp);
        temp = reader.readLine();//[NUM CHARS]
        temp = reader.readLine();
        int numChar = Integer.parseInt(temp);
        temp = reader.readLine();//[CHARACTERS]
        for (int i = 0; i < numChar; i++) {
            MudCharacter ch = dbg.characterDB.LoadEntity(reader);
            characters.add(ch.getID());
        }
        temp = reader.readLine();//[/CHARACTERS]

    }

    @Override
    public void save(PrintWriter p_stream) {
        p_stream.println("[NAME]");
        p_stream.println(getName());
        p_stream.println("[PASS]");
        p_stream.println(password);
        p_stream.println("[FIRSTLOGINTIME]");
        p_stream.println(this.loginTime.getTime());
        p_stream.println("[ACCESSLEVEL]");
        p_stream.println(this.accesslevel.toString());
        p_stream.println("[ALLOWEDCHARS]");
        p_stream.println(this.allowedCharacters);
        p_stream.println("[BANNED]");
        p_stream.println(banned);
        p_stream.println("[NUM CHARACTERS]");
        p_stream.println(characters.size());


        p_stream.println("[CHARACTERS]            ");
        for (Integer i : characters) {
            MudCharacter c = dbg.characterDB.get(i);
            dbg.characterDB.SaveEntity(p_stream, c);
        }
        p_stream.println("[/CHARACTERS]");
    }

    /**
     *  Gets the Account's Password
     *
     * @return Password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the accounts password
     *
     * @param password New Password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *  Gets the last time the account was login the Mud.
     *
     * @return Date of last log in time.
     */
    public Date getLoginTime() {
        return loginTime;
    }

    /**
     * Sets the last log in time.
     *
     * @param loginTime New Log in time
     */
    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    /**
     * Gets the total number of allowed characters for Account.
     *
     * @return Number of allowed characters.
     */
    public int getAllowedCharacters() {
        return allowedCharacters;
    }

    /**
     * Sets the number of allowed charaters.
     *
     * @param allowedCharacters New limit.
     */
    public void setAllowedCharacters(int allowedCharacters) {
        this.allowedCharacters = allowedCharacters;
    }

    /**
     * Is the account been banned or not.
     *
     * @return TRUE if banned.
     */
    public boolean isBanned() {
        return banned;
    }

    /**
     * Sets the account to ite new Banned state.
     * @param banned  TRUE = BANNED.
     */
    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    /**
     * gets the account's access level.
     *
     * @return Access level
     */
    public AccessLevel getAccesslevel() {
        return accesslevel;
    }

    /**
     * Sets the accounts new access level.
     *
     * @param accesslevel New Level.
     */
    public void setAccesslevel(AccessLevel accesslevel) {
        this.accesslevel = accesslevel;
    }

    @Override
    public void addCharacter(int characterid) {
        characters.add(characterid);
    }

    @Override
    public void removeCharacter(int characterid) {
        characters.remove(characterid);
    }

    @Override
    public int numCharacters() {
        return characters.size();
    }

    @Override
    public Set<Integer> getCharacters() {
        return characters;
    }

    @Override
    public Set<MudCharacter> Characters() {
        Set<MudCharacter> itemset = new HashSet<MudCharacter>();
        for (Integer k : getCharacters()) {
            itemset.add(dbg.characterDB.get(k));
        }
        return itemset;
    }

    @Override
    public MudCharacter seekCharacter(String name) {
        for (Integer k : getCharacters()) {
            MudCharacter mob = dbg.characterDB.get(k);
            if (mob.getName().equalsIgnoreCase(name)) {
                return mob;
            }
        }
        if (name.length() < 3) {
            return null;
        }
        for (Integer k : getCharacters()) {
            MudCharacter mob = dbg.characterDB.get(k);
            if (mob.getName().toLowerCase().contains(name.toLowerCase())) {
                return mob;
            }
        }
        return null;
    }

    @Override
    public Entities.Room Room() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRoom(int roomid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getRoom() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
