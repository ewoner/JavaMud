package Entities;

import Commands.Command;
import Entities.Templates.CharacterTemplate;
import Entities.bases.EntityWithRegion_Room;
import Entities.interfaces.HasItems;
import Entities.interfaces.Template;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The basic Character Entity of the game.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 * 
 * Version 1.1.1
 * 1.  Added the need for a command to be at least 2 characters long. (see below)
 * 2.  All movement commands now supported with "n, e, s, w, u, d".
 * Version 1.0.1
 * 1.  getItems() new returns only a copy of the set
 *  Version 1.0.0 - Release Verson
 * 
 */
public class MudCharacter extends EntityWithRegion_Room
        implements HasItems {

    private Set<Integer> items = new HashSet<Integer>();
    private List<Command> commands = new ArrayList<Command>();
    private int account;
    private boolean quiet; // interpret typing as chat or command?     
    private boolean verbose;// print room descriptions?
    private boolean loggedin;// are you logged in?
    private String lastcommand;// the last command the character entered

    /**
     * Creates a basic bare bones Character.  
     * 
     * NOTE - DOES NOT ADD TO DATABASE.
     */
    public MudCharacter() {
        account = 0;
        loggedin = false;
        quiet = false;
        verbose = true;
        setID(0);
    }

    /**
     * Adds the character to the Room and Region.
     */
    public void add() {
        Region().addCharacter(getID());
        Room().addCharacter(getID());
    }

    /**
     * Removes the character from the Room and Region.
     */
    public void remove() {
        if (Region() != null && Room() != null) {
            Region().removeCharacter(getID());
            Room().removeCharacter(getID());
        }

    }

    /**
     * Returns if the character is part of an account or not.
     *
     * @return TRUE if Acount>0 (PLAYER)
     */
    public boolean isPlayer() {
        return (account != 0);
    }

    /**
     * Adds the command to the Chracter.
     *
     * @param commandname
     * @return TRUE if it is added
     */
    public boolean addCommand(String commandname) {
        if (hasCommand(commandname)) {
            return false;
        }
        try {
            commands.add(dbg.commandDB.generateCommand(commandname, this));
            return true;
        } catch (Exception ex) {

            return false;
        }
    }

    /**
     * Deletes a Command from the Character.
     *
     * @param commandname
     * @return TRUE if deleted or FALSE if not found.
     */
    public boolean delCommand(String commandname) {
        for (Command c : commands) {
            if (c.getName().equalsIgnoreCase(commandname)) {
                commands.remove(c);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns if the character has a command by that name or not.
     * Only does a full search.
     *
     * @param commandName
     * @return TRUE if Character has command.
     */
    public boolean hasCommand(String commandName) {

        for (Command command : commands) {
            if (command.getName().equalsIgnoreCase(commandName)) {
                return true;
            }
        }
        return false;
    }

    /**
     *  Returns the command from the Characte by name.
     * Does a full and then partial match search.
     *
     * @param commandname
     * @return Command if found, else NULL.
     */
    public Command findCommand(String commandname) {
        for (Command command : commands) {
            if (commandname.equalsIgnoreCase(command.getName())) {
                return command;
            }
        }

        if ((commandname.length() < 2) && !(commandname.equalsIgnoreCase("n") || commandname.equalsIgnoreCase("s") || commandname.equalsIgnoreCase("e") || commandname.equalsIgnoreCase("w") || commandname.equalsIgnoreCase("u") || commandname.equalsIgnoreCase("d"))) {
            return null;
        }
        if (commandname.equalsIgnoreCase("n")) commandname = "north";
        if (commandname.equalsIgnoreCase("e")) commandname = "east";
        if (commandname.equalsIgnoreCase("w")) commandname = "west";
        if (commandname.equalsIgnoreCase("s")) commandname = "south";
        if (commandname.equalsIgnoreCase("u")) commandname = "up";
        if (commandname.equalsIgnoreCase("d")) commandname = "down";

        for (Command command : commands) {
            if (command.getName().toLowerCase().startsWith(commandname.toLowerCase())) {
                return command;
            }
        }
        return null;
        //finish this method
    }

    @Override
    public void addItem(int item) {
        items.add(item);
    }

    @Override
    public void removeItem(int item) {
        items.remove(item);
    }

    @Override
    public int numberItems() {
        return items.size();
    }

    @Override
    public Set<Integer> getItems() {
        Set<Integer> values = new HashSet<Integer>();
        values.addAll(items);
        return values;
    }

    @Override
    public void loadTemplate(Template temp) {
        CharacterTemplate template = (CharacterTemplate) temp;
        setTemplateID(template.getID());
        setName(template.getName());
        setDescription(template.getDescription());
        copyAttributes(template.getAttributes());
        for (String command : template.getTemplateCommands()) {
            addCommand(command);
        }
        for (String logic : template.getTemplateLogics()) {
            this.addLogic(logic);
        }
    }

    /**
     * Gets the account ID of the character
     * Account ID of 0 mean it is not a player.
     *
     * @return Acount ID
     */
    public int getAccount() {
        return account;
    }

    /**
     * Sets the characters Account ID.
     *  Account ID of 0 means it is not a player.
     *
     * @param accountid ID of Account.
     */
    public void setAccount(int accountid) {
        this.account = accountid;
    }

    /**
     * Returns if the characte is in quiet mode or not.  Quiet Mode means that
     * the Default command of "say" is not used.
     *
     * @return TRUE if in quiet mode.
     */
    public boolean isQuiet() {
        return quiet;
    }

    /**
     * Sets the quiet mode.
     *
     * @param quiet TRUE mean quiet mode.
     */
    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    /**
     * Is Verbose on of off.
     * Verbose mode shows less data to the screan.
     *
     * @return TRUE if on.
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * Sets Verbose mode.
     *
     * @param verbose TRUE meand VERBOSE is on.
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Is the player logged on or not.
     *
     * @return TRUE is loggedin
     */
    public boolean isLoggedin() {
        return loggedin;
    }

    /**
     * Sets if the player is log on or not.
     *
     * @param loggedin TRUE means logged on.
     */
    public void setLoggedin(boolean loggedin) {
        this.loggedin = loggedin;
    }

    /**
     * Returns the last full command line typed by the character.
     *
     * @return Whole last command line Type
     */
    public String getLastcommand() {
        return lastcommand;
    }

    /**
     * Sets the last command line typed.
     *
     * @param lastcommand THe last commanand line typed.
     */
    public void setLastcommand(String lastcommand) {
        this.lastcommand = lastcommand;
    }

    /**
     *  Returns a list of all Commands.
     *
     * @return List of Commands.
     */
    public List<Command> getCommands() {
        return commands;
    }

    @Override
    public void load(BufferedReader p_stream) throws IOException {
        if (!isPlayer() || isLoggedin()) {
            remove();
        }
        String temp = null;
        temp = p_stream.readLine();//[NAME]
        setName(p_stream.readLine());
        temp = p_stream.readLine();//[DESCRIPTION]
        setDescription(p_stream.readLine());
        temp = p_stream.readLine();//[ROOM]
        setRoom((Integer.parseInt(p_stream.readLine())));
        temp = p_stream.readLine();//[REGION]
        setRegion(Integer.parseInt(p_stream.readLine()));
        temp = p_stream.readLine();//[TEMPLATEID]
        setTemplateID(Integer.parseInt(p_stream.readLine()));
        temp = p_stream.readLine();//[ACCOUNT]
        setAccount(Integer.parseInt(p_stream.readLine()));
        temp = p_stream.readLine();//[QUIETMOD]
        setQuiet(Boolean.parseBoolean(p_stream.readLine()));
        temp = p_stream.readLine();//[VERBOSEMODE]
        setVerbose(Boolean.parseBoolean(p_stream.readLine()));
        getAttributes().load(p_stream);
        temp = p_stream.readLine();//[Commands]
        temp = p_stream.readLine();
        while (!temp.contains("[/COMMANDS]")) {
            if (addCommand(temp)) {
                Command command = commands.get(commands.size() - 1);
                command.Load(p_stream);
            } else {
                throw new IOException("Cannot load command: " + temp);
            }
            temp = p_stream.readLine();
        }
        getLogics().load(p_stream, this);
        temp = p_stream.readLine();//[ITEMS]

        temp = p_stream.readLine();//[ITEM]
        while (!temp.contains("[/ITEMS]")) {
            dbg.itemDB.LoadEntity(p_stream);
            temp = p_stream.readLine();//[/ITEM]
            temp = p_stream.readLine();//[ITEM]
        }
        if (!isPlayer() || isLoggedin()) {
            add();
        }
    }

    @Override
    public void save(PrintWriter p_stream) {
        p_stream.println("[NAME]");
        p_stream.println(getName());
        p_stream.println("[DESCRIPTION]");
        p_stream.println(getDescription());
        p_stream.println("[ROOM]");
        p_stream.println(Room().getID());
        p_stream.println("[REGION]");
        p_stream.println(Region().getID());
        p_stream.println("[TEMPLATEID]");
        p_stream.println(getTemplateID());
        p_stream.println("[ACCOUNT]");
        p_stream.println(account);
        p_stream.println("[QUIETMODE]");
        p_stream.println(quiet);
        p_stream.println("[VERBOSEMODE]");
        p_stream.println(verbose);

        // save my attributes to disk
        this.getAttributes().save(p_stream);
        // save my commands
        p_stream.println("[COMMANDS]");
        for (Command c : commands) {
            p_stream.println(c.getName());
            c.Save(p_stream);
        }
        p_stream.println("[/COMMANDS]");

        // save my logic
        getLogics().save(p_stream);


        // save my items
        p_stream.println("[ITEMS]");
        for (Integer in : items) {
            Item i = dbg.itemDB.get(in);
            p_stream.println("[ITEM]");
            dbg.itemDB.SaveEntity(p_stream, i);
            p_stream.println("[/ITEM]");
        }
        p_stream.println("[/ITEMS]");
    }

    @Override
    public Item seekItem(String name) {
        for (Integer k : getItems()) {
            Item item = dbg.itemDB.get(k);
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        if (name.length() < 3) {
            return null;
        }
        for (Integer k : getItems()) {
            Item item = dbg.itemDB.get(k);
            if (item.getName().toLowerCase().contains(name.toLowerCase())) {
                return item;
            }
        }
        return null;
    }

    @Override
    public Set<Item> Items() {
        Set<Item> itemset = new HashSet<Item>();
        for (Integer k : getItems()) {
            itemset.add(dbg.itemDB.get(k));
        }
        return itemset;
    }
}
