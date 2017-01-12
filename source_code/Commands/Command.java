/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Commands;

import Actions.Action;
import Actions.ActionType;
import Entities.MudCharacter;
import Mud.Mud;
import Databases.Group.DatabaseGroup;
import Scripts.Script;

/**
 *  This is the base class for all Commands in the game.
 *  It is loadable by defualt which needs to be over riden in
 *  non-scriptalbe files.  This is by design to help streamline scripts.
 *
 *  @author Brion Lang
 *  Date: 17 Jam 2009
 *  Version 1.1.1
 *  1.  Added badUsage(String) to help add amplification data to a bad usage.
 * Version 1.0.1
 * 1.  Removed an unused constrotor (Command(MOB)) that was not in use.
 * 2.  Minor corrections to the JavaDocs.
 *  Version 1.0.0
 */
public abstract class Command extends Script {

    private String name,  usage,  description;
    private MudCharacter mob;
    private static DatabaseGroup dbg = Mud.dbg;
    /**
     * If the Command can be reloaded on the fly or not.  Default is TRUE.
     * All hard coded commands are false to help prevent erronious errors
     * that while cause the Mud to have to be reloaded.
     *
     */
    protected boolean loadable = true;

    /*
     * Basic Constructor.
     *
     * Currently deleted due to lack of use.
     */
//    public Command() {
//        this(null, "NONE", " ", "No Command");
//    }

    /*
     * Basic Constructor.  Sets its owner and some default strings.
     *
     * Deleted 26 Nov 09 due to lack of use.
     *
     * Currently used to load a command from file using NULL.
     *
     * @param mob The owner.
     */
    /*public Command(MudCharacter mob) {
    this(mob, "NONE", " ", "No Command");
    }*/
    /**
     * Standard general use constructor.
     *
     * Note about the USAGE String:
     * Items needed are in pointed brackets ie '>' and '<'.  Optional items
     * have "|" between the bracket and item name.  Some Commands may have
     * multiple type of items ie spawnitem can take an name of an item template
     * or its number.  Different options are seperated using '/'.  Further
     * infomation can be from in the documentation.  Words in all CAPS are
     * literals.
     *
     * @param mob The owner of the Command.
     * @param name The Name of the command.
     * @param usage The usage string of teh command.
     * @param description Basuc description of what the command is used for.
     */
    public Command(MudCharacter mob, String name, String usage, String description) {
        this.name = name;
        this.mob = mob;
        this.description = description;
        this.usage = usage;
        setScriptName();
    }

    /**
     * Retruns the commands name.
     *
     * @return Command's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Convience method that returns the correct usage of the command to the user.
     */
    protected void badUsage() {
        Mob().DoAction(new Action(ActionType.error, 0, 0, 0, 0, "Usage: " + getUsage()));
    }

    /**
     * Convience method to report bad usage.
     * @param string Further error message.
     */
    protected void badUsage(String string) {
        Mob().DoAction(new Action(ActionType.error, 0, 0, 0, 0, "Usage: " + getUsage() + " --- " + string));
    }

    /**
     * Command to exute the command.
     *
     * @param parameters A whole string representing the command line typed in
     * minus the command's name.
     */
    abstract public void Execute(String parameters);

    /**
     * Returnes the usage String.
     *
     * @return The correct usage.
     */
    public String getUsage() {
        return usage;
    }

    /**
     * Returns the commands description.
     *
     * @return The command description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Convient method to return the commands owner.
     *
     * @return The mob
     */
    public MudCharacter Mob() {
        return mob;
    }

    /**
     * Sets teh Script's Name.
     *
     * Current not in widely use.
     */
    @Override
    public void setScriptName() {
        String className = this.getClass().getName();
        className = className.substring(className.indexOf('.') + 1);
        scriptName = className;
    }

    /**
     * Is the script/command loadable.
     *
     * @return TRUE if loadable, FALSE else.
     */
    public boolean isLoadable() {
        return loadable;
    }
}
