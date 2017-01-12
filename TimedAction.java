
package Actions;

import Entities.Item;
import Entities.MudCharacter;
import Entities.Portal;
import Entities.Region;
import Entities.Room;
import Entities.enums.EntityType;
import Mud.Mud;
import Databases.Group.DatabaseGroup;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * An action that has a time delay.  They are add to the Mud's action queuy.
 *
 * @author Brion W. Lang
 * Date; 17 Jan 2009
 * Version 1.0.0
 */
public class TimedAction implements Comparable<TimedAction> {

    private static DatabaseGroup dbg = Mud.dbg;
    private long executiontime;
    private Action actionevent;
    private boolean valid;

    /**
     * A no nothing constructor.  Used only to load a previously saved
     * TimedAction.
     */
    public TimedAction() {
        this.valid = true;
    }

    /**
     * Basic constructor for TimedAction requiring a time to started the
     * action and the Action to perform.
     *
     * @param time The time to begin the action.
     * @param action The action to perform.
     */
    public TimedAction(long time, Action action) {
        this();
        executiontime = time;
        actionevent = action;
    }

    /**
     * Hooks the TimedAction to the appropriate Characters, Items, Rooms,
     * etc.
     */
    public void hook() {
        if (getActionEvent().getType() == ActionType.attemptsay ||
                getActionEvent().getType() == ActionType.command ||
                getActionEvent().getType() == ActionType.attemptenterportal ||
                getActionEvent().getType() == ActionType.attempttransport ||
                getActionEvent().getType() == ActionType.transport ||
                getActionEvent().getType() == ActionType.destroycharacter) {
            MudCharacter c = dbg.characterDB.get(getActionEvent().arg1);
            c.addHook(this);
        } else if (getActionEvent().getType() == ActionType.attemptgetitem ||
                getActionEvent().getType() == ActionType.attemptdropitem) {
            MudCharacter c = dbg.characterDB.get(getActionEvent().arg1);
            c.addHook(this);
            Item item = dbg.itemDB.get(getActionEvent().arg2);
            item.addHook(this);
        } else if (getActionEvent().getType() == ActionType.attemptgiveitem) {
            MudCharacter c = dbg.characterDB.get(getActionEvent().arg1);
            c.addHook(this);
            c = dbg.characterDB.get(getActionEvent().arg2);
            c.addHook(this);
            Item item = dbg.itemDB.get(getActionEvent().arg3);
            item.addHook(this);
        } else if (getActionEvent().getType() == ActionType.destroyitem) {
            Item item = dbg.itemDB.get(getActionEvent().arg1);
            item.addHook(this);
        } else if (getActionEvent().getType() == ActionType.messagelogic ||
                getActionEvent().getType() == ActionType.dellogic ||
                getActionEvent().getType() == ActionType.doaction ||
                getActionEvent().getType() == ActionType.modifyattribute) {
            EntityType entityType = EntityType.values()[getActionEvent().arg1];

            switch (entityType) {
                case Character:
                    MudCharacter c = dbg.characterDB.get(getActionEvent().arg2);
                    c.addHook(this);
                    break;
                case Item:
                    Item item = dbg.itemDB.get(getActionEvent().arg2);
                    item.addHook(this);
                    break;
                case Room:
                    Room room = dbg.roomDB.get(getActionEvent().arg2);
                    room.addHook(this);
                    break;
                case Portal:
                    Portal portal = dbg.portalDB.get(getActionEvent().arg2);
                    portal.addHook(this);
                    break;
                case Region:
                    Region region = dbg.regionDB.get(getActionEvent().arg2);
                    region.addHook(this);
                    break;
            }
        }
    }

    /**
     * Unhooks the TitmedAction from Character, Items, Rooms, etc.
     */
    public void unhook() {
        valid = false;
        if (getActionEvent().getType() == ActionType.attemptsay ||
                getActionEvent().getType() == ActionType.command ||
                getActionEvent().getType() == ActionType.attemptenterportal ||
                getActionEvent().getType() == ActionType.attempttransport ||
                getActionEvent().getType() == ActionType.transport ||
                getActionEvent().getType() == ActionType.destroycharacter) {
            MudCharacter c = dbg.characterDB.get(getActionEvent().arg1);
            c.delHook(this);
        } else if (getActionEvent().getType() == ActionType.attemptgetitem ||
                getActionEvent().getType() == ActionType.attemptdropitem) {
            MudCharacter c = dbg.characterDB.get(getActionEvent().arg1);
            c.delHook(this);
            Item item = dbg.itemDB.get(getActionEvent().arg2);
            item.delHook(this);
        } else if (getActionEvent().getType() == ActionType.attemptgiveitem) {
            MudCharacter c = dbg.characterDB.get(getActionEvent().arg1);
            c.delHook(this);
            c = dbg.characterDB.get(getActionEvent().arg2);
            c.delHook(this);
            Item item = dbg.itemDB.get(getActionEvent().arg3);
            item.delHook(this);
        } else if (getActionEvent().getType() == ActionType.destroyitem) {
            Item item = dbg.itemDB.get(getActionEvent().arg1);
            item.delHook(this);
        } else if (getActionEvent().getType() == ActionType.messagelogic ||
                getActionEvent().getType() == ActionType.dellogic ||
                getActionEvent().getType() == ActionType.doaction ||
                getActionEvent().getType() == ActionType.modifyattribute) {
            EntityType entityType = EntityType.values()[getActionEvent().arg1];

            switch (entityType) {
                case Character:
                    MudCharacter c = dbg.characterDB.get(getActionEvent().arg2);
                    c.delHook(this);
                    break;
                case Item:
                    Item item = dbg.itemDB.get(getActionEvent().arg2);
                    item.delHook(this);
                    break;
                case Room:
                    Room room = dbg.roomDB.get(getActionEvent().arg2);
                    room.delHook(this);
                    break;
                case Portal:
                    Portal portal = dbg.portalDB.get(getActionEvent().arg2);
                    portal.delHook(this);
                    break;
                case Region:
                    Region region = dbg.regionDB.get(getActionEvent().arg2);
                    region.delHook(this);
                    break;
            }
        }
    }

    /**
     * Saves the TimedAction to a PrintWriter which in turn is attached to a
     * file.
     *
     * @param p_stream The PrintWriter to save onto.
     */
    public void save(PrintWriter p_stream)   {
        if (!isValid()) {
            return;
        }
        p_stream.println("[TIMER]");
        p_stream.println("[TIME]");
        p_stream.println(getExecutionTime());
        p_stream.println("[NAME]");
        p_stream.println(getActionEvent().getType().toString());
        p_stream.println("[DATA1]");
        p_stream.println(getActionEvent().arg1);
        p_stream.println("[DATA2]");
        p_stream.println(getActionEvent().arg2);
        p_stream.println("[DATA3]");
        p_stream.println(getActionEvent().arg3);
        p_stream.println("[DATA4]");
        p_stream.println(getActionEvent().arg4);

        // set the string data to "0", so that SOMETHING is written out to disk.
        // writing nothing would be a disaster, because the load function assumes
        // that something is there (whitespace... blah!).
        // note that this is acceptible because if the parameter is "", then
        // it is assumed to be unused anyway, so saving and loading a "0" will
        // make it ignored
        p_stream.println("[STRING]");
        if (getActionEvent().getData().isEmpty() || getActionEvent().getData() == null) {
            p_stream.println("null");
        } else {
            p_stream.println(getActionEvent().getData());
        }
        p_stream.println("[/TIMER]");
    }

    /**
     * Loads a TimedAction from a BufferedReader.
     *
     * @param p_stream The BufferedReader to load from.
     * @throws java.io.IOException Sends I/O errors to calling method.
     */
    public void load(BufferedReader p_stream) throws IOException   {
        p_stream.readLine();//("[TIMER]\n");
        p_stream.readLine();//("[TIME]\n");
        executiontime = Long.parseLong(p_stream.readLine());
        p_stream.readLine();//("[NAME]\n");
        ActionType type = ActionType.valueOf(p_stream.readLine());
        p_stream.readLine();//"[DATA1]\n");
        int a1 = Integer.parseInt(p_stream.readLine());
        p_stream.readLine();//("[DATA2]\n");
        int a2 = Integer.parseInt(p_stream.readLine());
        p_stream.readLine();//("[DATA3]\n");
        int a3 = Integer.parseInt(p_stream.readLine());
        p_stream.readLine();//("[DATA4]\n");
        int a4 = Integer.parseInt(p_stream.readLine());

        // set the string data to "0", so that SOMETHING is written out to disk.
        // writing nothing would be a disaster, because the load function assumes
        // that something is there (whitespace... blah!).
        // note that this is acceptible because if the parameter is "", then
        // it is assumed to be unused anyway, so saving and loading a "0" will
        // make it ignored
        p_stream.readLine();//("[STRING]\n");
        String temp = p_stream.readLine();
        if (temp.equals("null")) {
            temp = null;
        }
        p_stream.readLine();//("[/TIMER]\n");
        actionevent = new Action(type, a1, a2, a3, a4, temp);
    }

    /**
     * Gets the Action associated with the TimedAction.
     *
     * @return The Action
     */
    public Action getActionEvent() {
        return actionevent;
    }

    /**
     * Gets the string data from the Action of TimedAction
     *
     * @return String Arguement of Action
     */
    public String getData() {
        return getActionEvent().getData();
    }

    @Override
    public int compareTo(TimedAction o) {
        if (this.getExecutionTime() > o.getExecutionTime()) {
            return 1;
        } else if (this.getExecutionTime() < o.getExecutionTime()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Gets the time the Action is to be run.
     *
     * @return The execution time.
     */
    public long getExecutionTime() {
        return executiontime;
    }

    /**
     * Returnes if the Action is valid or not.
     *
     * @return TRUE if valid, else FALSE.
     */
    public boolean isValid() {
        return valid;
    }
}
