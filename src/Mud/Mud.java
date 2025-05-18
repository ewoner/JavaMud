package Mud;

import Actions.ActionType;
import Actions.Action;
import Actions.TimedAction;
import Commands.Command;
import Entities.MudCharacter;
import Entities.Item;
import Entities.Portal;
import Entities.Region;
import Entities.Room;
import Entities.enums.EntityType;
import Entities.interfaces.HasItems;
import Logics.Logic;
import Databases.Group.DatabaseGroup;
import Mud.Exceptions.MudException;
import Scripts.ScriptReloadMode;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The meat of the game. This is the main game module. All Entities exist inside
 * of this module. In a nut shell, this is the game.
 *
 * @author Brion Lang Date: 17 Jam 2009
 *
 * Version 1.1.0 1. Removed the loading of players. Players will be kept in
 * Account files only now. Version 1.0.0 1. joinQuantities() was not removing
 * the item from the mud prior to adding the id to the cleanup list. In affect
 * leaving the item in the game, but deleted in the database. Version 1.0.0
 *
 */
public class Mud {

    /**
     * The main databased for the Mud game. Static and public to be shared with
     * all. Note-being static do not call two Mud classes in the same
     * application unless you wish them to share the same databases.
     */
    static final public DatabaseGroup dbg = new DatabaseGroup();
    //number of ticks since started.
    private long tickCount = 0;
    /**
     * The number of seconds a tick is (1 second = 1000).
     *
     * Currently not in use.
     */
    public final static long TIME_TICK = 4000;
    //if the mud is running or not
    private boolean Running = false;
    //list of character in the realm.  Appears to be only players right now.  Need to be removed if such.
    private List<MudCharacter> characters = new ArrayList<MudCharacter>();
    //list of players currently logged in
    private List<MudCharacter> players = new ArrayList<MudCharacter>();
    //The game time
    static private MyTimer gametime = new MyTimer();
    //Queue of TimedActions
    private PriorityQueue<TimedAction> timerregistry = new PriorityQueue<TimedAction>();

    /**
     * Startes the Mud.
     */
    public Mud() {
        Running = true;
    }

    /**
     * Processed a Game action. Creates a new Action and calls DoAction(Acton).
     *
     * @param action Action Type
     * @param arg1 Argument 1
     * @param arg2 Argument 2
     * @param arg3 Argument 3
     * @param arg4 Argument 4
     * @param data Action Data
     */
    public void doAction(ActionType action, int arg1, int arg2, int arg3, int arg4, String data) {
        doAction(new Action(action, arg1, arg2, arg3, arg4, data));
    }

    /**
     * Processes a game action.
     *
     * @param action Action To Process
     */
    public void doAction(Action action) {
        Logger.getLogger(Mud.class.getName()).info(action.getType().toString() + " " + action.arg1 + " " + action.arg2 + " " + action.arg3 + " " + action.arg4 + " " + action.getData());

        try {
            if (action.getType() == ActionType.chat || action.getType() == ActionType.announce) {
                actionForRealmPlayers(action);
            } else if (action.getType() == ActionType.doaction) {
                routeAction(action);
            } else if (action.getType() == ActionType.modifyattribute) {
                modifyAttribute(action);
            } else if (action.getType() == ActionType.vision) {
                actionForRoomCharacters(action, dbg.roomDB.get(action.arg1));
            } else if (action.getType() == ActionType.enterrealm) {
                login(action);
            } else if (action.getType() == ActionType.leaverealm) {
                logoout(action);
            } else if (action.getType() == ActionType.attemptsay) {
                say(action);
            } else if (action.getType() == ActionType.command) {
                doCommand(action);
            } else if (action.getType() == ActionType.spell) {
                doSpell(action);
            } else if (action.getType() == ActionType.attemptenterportal) {
                enterPortal(action);
            } else if (action.getType() == ActionType.attempttransport) {
                transport(action);
            } else if (action.getType() == ActionType.forcetransport) {
                forceTransport(action);
            } else if (action.getType() == ActionType.attemptgetitem) {
                getItem(action);
            } else if (action.getType() == ActionType.attemptdropitem) {
                dropItem(action);
            } else if (action.getType() == ActionType.attemptgiveitem) {
                giveItem(action);
            } else if (action.getType() == ActionType.spawnitem) {
                spawnItem(action);
            } else if (action.getType() == ActionType.spawncharacter) {
                spawnMob(action);
            } else if (action.getType() == ActionType.destroyitem) {
                destroyItem(action);
            } else if (action.getType() == ActionType.destroycharacter) {
                destroyCharacter(action);
            } else if (action.getType() == ActionType.cleanup) {
                cleanup();
            } else if (action.getType() == ActionType.savedatabases) {
                saveall();
            } else if (action.getType() == ActionType.saveregion) {
                saveRegion(action);
            } else if (action.getType() == ActionType.saveplayers) {
                savePlayers();
            } else if (action.getType() == ActionType.reloaditems) {
                reloadItemTemplates(action.getData());
            } else if (action.getType() == ActionType.reloadcharacters) {
                reloadCharacterTemplates(action.getData());
            } else if (action.getType() == ActionType.reloadregion) {
                reloadRegion(action.getData());
            } else if (action.getType() == ActionType.reloadcommandscript) {
                reloadCommandScript(action.getData(), ScriptReloadMode.values()[action.arg1]);
            } else if (action.getType() == ActionType.reloadlogicscript) {
                reloadLogicScript(action.getData(), ScriptReloadMode.values()[action.arg1]);
            } else if (action.getType() == ActionType.messagelogic) {
                logicAction(action);
            } else if (action.getType() == ActionType.addlogic) {
                addLogic(action);
            } else if (action.getType() == ActionType.dellogic) {
                delLogic(action);
            } else if (action.getType() == ActionType.shutdown) {
                shutdown();
            }
        } catch (MudException ex) {
            Logger.getLogger(ex.getClassName()).severe(ex.toString());
            ex.printStackTrace();
        }
    }

    /**
     * The main method of the game. Runs thru the queue and any actions that
     * need to be fired are fired.
     */
    public void executeLoop() {
        long t = getGameTime();
        while (timerregistry.size() > 0 && timerregistry.peek().getExecutionTime() <= t) {
            TimedAction a = timerregistry.poll();
            // perform the action if it's a valid one (ie: it hasn't been unhooked)
            if (a.isValid()) {
                a.unhook();                        // unhook it
                doAction(a.getActionEvent());
            }
        }
    }

    /**
     * Passes on a action to all players.
     *
     * @param action
     */
    private void actionForRealmPlayers(Action action) {
        // tell every player about the action
        for (MudCharacter p : players) {
            p.DoAction(action);
        }

    }

    /**
     * Passes on an action to all characters in the mud.
     *
     * @param action
     */
    private void actionRealmCharacters(Action action) {
        // tell every character about the action
        for (MudCharacter c : characters) {
            c.DoAction(action);
        }

    }

    /**
     * Cycles through all characters in a room and processes an action on each
     * one of them.
     */
    private void actionForRoomCharacters(Action action, Room room) {
        for (int i : room.getCharacters()) {
            MudCharacter mob = dbg.characterDB.get(i);
            mob.DoAction(action);
        }

    }

    /**
     * Cycles through all the Items in a Room and processes an action on each
     * one.
     */
    private void actionForRoomItems(Action action, Room room) {
        for (int j : room.getItems()) {
            Item item = dbg.itemDB.get(j);
            item.DoAction(action);
        }

    }

    /**
     * Routes an Action to the correct Entity. See documentation about actions
     * for more information.
     */
    private void routeAction(Action action) {
        EntityType type = EntityType.values()[action.arg1];
        switch (type) {
            case Character:
                dbg.characterDB.get(action.arg2).DoAction(action);
                break;

            case Item:
                dbg.itemDB.get(action.arg2).DoAction(action);
                break;

            case Room:
                dbg.roomDB.get(action.arg2).DoAction(action);
                break;

            case Portal:
                dbg.portalDB.get(action.arg2).DoAction(action);
                break;

            case Region:
                dbg.regionDB.get(action.arg2).DoAction(action);
                break;

        }

    }

    /**
     * Modifies an Atrrribute of an entity. Routes it accordinly.
     */
    private void modifyAttribute(Action action) {
        EntityType type = EntityType.values()[action.arg1];
        switch (type) {
            case Character:
                MudCharacter mob = dbg.characterDB.get(action.arg2);
                mob.setAttribute(action.getData(), action.arg3);
                mob.DoAction(action);
                break;

            case Item:
                Item item = dbg.itemDB.get(action.arg2);
                item.setAttribute(action.getData(), action.arg3);
                item.DoAction(action);
                break;

            case Room:
                Room room = dbg.roomDB.get(action.arg2);
                room.setAttribute(action.getData(), action.arg3);
                room.DoAction(action);
                break;

            case Portal:
                Portal portal = dbg.portalDB.get(action.arg2);
                portal.setAttribute(action.getData(), action.arg3);
                portal.DoAction(action);
                break;

            case Region:
                Region region = dbg.regionDB.get(action.arg2);
                region.setAttribute(action.getData(), action.arg3);
                region.DoAction(action);
                break;

        }

    }

    /**
     * Exucutes a command.
     */
    private void doCommand(Action action) {
        // get the player
        MudCharacter player = dbg.characterDB.get(action.arg1);
        String full = action.getData();
        // handle "repeating commands" here
        if (full.equals("/")) {
            full = player.getLastcommand();         // repeat last command
        } else {
            player.setLastcommand(full);       // set last command
        }

        String command = "";
        String args = "";
        if (full.contains(" ") || full.contains("\t")) {
            // get the first word  "the command"
            command = full.substring(0, full.indexOf(' ')).trim();
            // get the "the arguments"
            args = full.substring(full.indexOf(' ')).trim();
        } else {
            command = full;
        }
// in loud mode, commands must be prefixed with "/", everything else is
// local talking
// in quiet mode, what you say must be a command

        if (!player.isQuiet() && !command.startsWith("/")) {
            doAction(ActionType.attemptsay, player.getID(), 0, 0, 0, full);
            return;

        }
        // if a string starts with '/', it is always assumed to be a command,
// so erase the slash

        if (command.startsWith("/")) {
            command = command.substring(1);
        }
// the next part must be inside a try block in case anything fails, you
// don't want to bring down the whole game

        // try to find the command:
        Command com = player.findCommand(command);
        if (com == null) {
            player.DoAction(new Action(ActionType.error, 0, 0, 0, 0, "Unrecognized Command: " + command));
            return;
        }
        // try executing the command:
        com.Execute(args);
    }

    /**
     * In the Workds next
     */
    private void doSpell(Action action) {
    }

    /**
     * Character says somthing aloud to all in room.
     */
    private void say(Action action) {
        // ========================================================================
        //  PERFORM LOOKUPS
        // ========================================================================
        MudCharacter mob = dbg.characterDB.get(action.arg1);
        Room room = mob.Room();
        Region region = room.Region();
        int c = action.arg1;
        String text = action.getData();
        // ========================================================================
        //  PERFORM QUERIES
        // ========================================================================
        // query the character to see if he can do it first.
        if (mob.DoAction(new Action(ActionType.cansay, c, 0, 0, 0, text)) == Logic.TRUE) {
            return;
        }

        if (room.DoAction(new Action(ActionType.cansay, c, 0, 0, 0, text)) == Logic.TRUE) {
            return;
        }

        if (region.DoAction(new Action(ActionType.cansay, c, 0, 0, 0, text)) == Logic.TRUE) {
            return;
        }
// ========================================================================
//  PERFORM EVENT NOTIFICATIONS
// ========================================================================

        actionForRoomCharacters(new Action(ActionType.say, c, 0, 0, 0, text), mob.Room());
        room.DoAction(new Action(ActionType.say, c, 0, 0, 0, text));
        region.DoAction(new Action(ActionType.say, c, 0, 0, 0, text));
    }

    /**
     * Logs a player on into the mud.
     */
    private void login(Action action) {
        MudCharacter player = dbg.characterDB.get(action.arg1);
        Room room = player.Room();
        Region region = room.Region();
        int p = action.arg1;
        // add character to the game
        player.setLoggedin(true);
        addCharacter(player);
        addPlayer(player);
        region.addCharacter(player.getID());
        room.addCharacter(player.getID());
        actionForRealmPlayers(new Action(ActionType.enterrealm, p));
        region.DoAction(new Action(ActionType.enterregion, p));
        player.DoAction(new Action(ActionType.enterregion, p));
        room.DoAction(new Action(ActionType.enterroom, p, 0));
        actionForRoomCharacters(new Action(ActionType.enterroom, p, 0), room);
        actionForRoomItems(new Action(ActionType.enterroom, p, 0), room);
    }

    /**
     * Player logs out.
     */
    private void logoout(Action action) {
        int p = action.arg1;
        MudCharacter player = dbg.characterDB.get(action.arg1);
        Room room = player.Room();
        Region region = room.Region();
        // tell everyone about it
        actionForRoomItems(new Action(ActionType.leaveroom, p, 0), room);
        actionForRoomCharacters(new Action(ActionType.leaveroom, p, 0), room);
        room.DoAction(new Action(ActionType.leaveroom, p, 0));
        player.DoAction(new Action(ActionType.leaveregion, p));
        region.DoAction(new Action(ActionType.leaveregion, p));
        actionForRealmPlayers(new Action(ActionType.leaverealm, p));
        // remove him from the game
        room.removeCharacter(player.getID());
        region.removeCharacter(player.getID());
        removePlayer(player);
        removeCharacter(player);
        player.setLoggedin(false);
    }

    /**
     * Enters a portal
     */
    private void enterPortal(Action action) throws MudException {
        int c = action.arg1;
        int pt = action.arg2;
        MudCharacter mob = dbg.characterDB.get(action.arg1);
        Portal portal = dbg.portalDB.get(pt);
        Room oldroom = mob.Room();
        // make sure that character can enter portal from current room
        if (portal.seekStartRoom(oldroom.getID()) == null) {
            throw new MudException(new Exception(
                    "Character " + mob.getName() + " tried entering portal " + portal.getName()
                    + " but has no exit from room " + mob.Room().getID()), this.getClass().getName());
        }
// get the destination room

        Room newroom = portal.getDestination(oldroom);
        boolean changeregion = oldroom.Region() != newroom.Region();
        Region oldreg = oldroom.Region();
        Region newreg = newroom.Region();
        // "ask permission" of everyone to leave the room:
        if (changeregion) {
            if (oldreg.DoAction(new Action(ActionType.canleaveregion, c, oldreg.getID())) == Logic.NO) {
                return;
            }

            if (newreg.DoAction(new Action(ActionType.canenterregion, c, newreg.getID())) == Logic.NO) {
                return;
            }

            if (mob.DoAction(new Action(ActionType.canleaveregion, c, oldreg.getID())) == Logic.NO) {
                return;
            }

            if (mob.DoAction(new Action(ActionType.canenterregion, c, newreg.getID())) == Logic.NO) {
                return;
            }

        }
        if (oldroom.DoAction(new Action(ActionType.canleaveroom, c)) == Logic.NO) {
            return;
        }

        if (newroom.DoAction(new Action(ActionType.canenterroom, c)) == Logic.NO) {
            return;
        }

        if (mob.DoAction(new Action(ActionType.canleaveroom, c)) == Logic.NO) {
            return;
        }

        if (portal.DoAction(new Action(ActionType.canenterportal, c)) == Logic.NO) {
            return;
        }
// tell the room that the player is leaving

        if (changeregion) {
            oldreg.DoAction(new Action(ActionType.leaveregion, c, oldreg.getID()));
            mob.DoAction(new Action(ActionType.leaveregion, c, oldreg.getID()));
        }

        actionForRoomCharacters(new Action(ActionType.leaveroom, c, pt), mob.Room());
        actionForRoomItems(new Action(ActionType.leaveroom, c, pt), mob.Room());
        oldroom.DoAction(new Action(ActionType.leaveroom, c, pt));
        // now tell the portal that the player has actually entered
        portal.DoAction(new Action(ActionType.enterportal, c, portal.getID()));
        mob.DoAction(new Action(ActionType.enterportal, c, portal.getID()));
        // now move the character
        if (changeregion) {
            oldreg.removeCharacter(mob.getID());
            mob.setRegion(newreg.getID());
            newreg.addCharacter(mob.getID());
        }

        oldroom.removeCharacter(mob.getID());
        mob.setRoom(newroom.getID());
        newroom.addCharacter(mob.getID());
        // tell everyone in the room that the player has entered
        if (changeregion) {
            newreg.DoAction(new Action(ActionType.enterregion, c, newreg.getID()));
            mob.DoAction(new Action(ActionType.enterregion, c, newreg.getID()));
        }

        newroom.DoAction(new Action(ActionType.enterroom, c, pt));
        actionForRoomCharacters(new Action(ActionType.enterroom, c, pt), mob.Room());
        actionForRoomItems(new Action(ActionType.enterroom, c, pt), mob.Room());
    }

    /**
     * Transports a character from one loaction to another.
     */
    private void transport(Action action) {
        // ========================================================================
        //  PERFORM LOOKUPS
        // ========================================================================
        int c = action.arg1, r = action.arg2;
        MudCharacter mob = dbg.characterDB.get(c);
        Room oldroom = mob.Room();
        Room newroom = dbg.roomDB.get(r);
        if (newroom == null) {
            mob.DoAction(ActionType.error, 0, 0, 0, 0, "No such room id: " + action.arg2);
            return;
        }

        boolean changeregion = oldroom.Region() != newroom.Region();
        Region oldreg = oldroom.Region();
        Region newreg = newroom.Region();
        // ========================================================================
        //  PERFORM QUERIES
        // ========================================================================
        // "ask permission" of everyone to leave the room:
        if (changeregion) {
            if (oldreg.DoAction(new Action(ActionType.canleaveregion, c, oldreg.getID())) == Logic.NO) {
                return;
            }

            if (newreg.DoAction(new Action(ActionType.canenterregion, c, newreg.getID())) == Logic.NO) {
                return;
            }

            if (mob.DoAction(new Action(ActionType.canleaveregion, c, oldreg.getID())) == Logic.NO) {
                return;
            }

            if (mob.DoAction(new Action(ActionType.canenterregion, c, newreg.getID())) == Logic.NO) {
                return;
            }

        }
        if (oldroom.DoAction(new Action(ActionType.canleaveroom, c, oldroom.getID())) == Logic.NO) {
            return;
        }

        if (newroom.DoAction(new Action(ActionType.canenterroom, c, newroom.getID())) == Logic.NO) {
            return;
        }

        if (mob.DoAction(new Action(ActionType.canleaveroom, c, oldroom.getID())) == Logic.NO) {
            return;
        }

        if (mob.DoAction(new Action(ActionType.canenterroom, c, newroom.getID())) == Logic.NO) {
            return;
        }
// ========================================================================
//  PHYSICAL MOVEMENT
// ========================================================================
// now move the character

        if (changeregion) {
            oldreg.removeCharacter(mob.getID());
            mob.setRegion(newreg.getID());
            newreg.addCharacter(mob.getID());
        }

        oldroom.removeCharacter(mob.getID());
        mob.setRoom(newroom.getID());
        newroom.addCharacter(mob.getID());
        // ========================================================================
        //  PERFORM EVENT NOTIFICATIONS
        // ========================================================================
        // tell the room that the player is leaving
        if (changeregion) {
            oldreg.DoAction(new Action(ActionType.leaveregion, c, oldreg.getID()));
            mob.DoAction(new Action(ActionType.leaveregion, c, oldreg.getID()));
        }

        oldroom.DoAction(new Action(ActionType.leaveroom, c, 0));
        mob.DoAction(new Action(ActionType.leaveroom, c, 0));
        actionForRoomCharacters(new Action(ActionType.leaveroom, c, 0), mob.Room());
        actionForRoomItems(new Action(ActionType.leaveroom, c, 0), mob.Room());
        // tell everyone in the room that the player has entered
        if (changeregion) {
            newreg.DoAction(new Action(ActionType.enterregion, c, newreg.getID()));
            mob.DoAction(new Action(ActionType.enterregion, c, newreg.getID()));
        }

        newroom.DoAction(new Action(ActionType.enterroom, c, 0));
        actionForRoomCharacters(new Action(ActionType.enterroom, c, 0), mob.Room());
        actionForRoomItems(new Action(ActionType.enterroom, c, 0), mob.Room());
    }

    /**
     * A forced transport. Does not query if it is allowed.
     */
    private void forceTransport(Action action) {
        // ========================================================================
        //  PERFORM LOOKUPS
        // ========================================================================
        int c = action.arg1, r = action.arg2;
        MudCharacter mob = dbg.characterDB.get(c);
        Room oldroom = mob.Room();
        Room newroom = dbg.roomDB.get(r);
        boolean changeregion = oldroom.Region() != newroom.Region();
        Region oldreg = oldroom.Region();
        Region newreg = newroom.Region();
        // ========================================================================
        //  PHYSICAL MOVEMENT
        // ========================================================================
        // now move the character
        if (changeregion) {
            oldreg.removeCharacter(mob.getID());
            mob.setRegion(newreg.getID());
            newreg.addCharacter(mob.getID());
        }

        oldroom.removeCharacter(mob.getID());
        mob.setRoom(newroom.getID());
        newroom.addCharacter(mob.getID());
        // ========================================================================
        //  PERFORM EVENT NOTIFICATIONS
        // ========================================================================
        // tell the room that the player is leaving
        if (changeregion) {
            oldreg.DoAction(new Action(ActionType.leaveregion, c, oldreg.getID()));
            mob.DoAction(new Action(ActionType.leaveregion, c, oldreg.getID()));
        }

        oldroom.DoAction(new Action(ActionType.leaveroom, c, 0));
        mob.DoAction(new Action(ActionType.leaveroom, c, 0));
        actionForRoomCharacters(new Action(ActionType.leaveroom, c, 0), mob.Room());
        actionForRoomItems(new Action(ActionType.leaveroom, c, 0), mob.Room());
        // tell everyone in the room that the player has entered
        if (changeregion) {
            newreg.DoAction(new Action(ActionType.enterregion, c, newreg.getID()));
            mob.DoAction(new Action(ActionType.enterregion, c, newreg.getID()));
        }

        newroom.DoAction(new Action(ActionType.enterroom, c, 0));
        actionForRoomCharacters(new Action(ActionType.enterroom, c, 0), mob.Room());
        actionForRoomItems(new Action(ActionType.enterroom, c, 0), mob.Room());
    }

    /**
     * Gets an Item.
     */
    private void getItem(Action action) throws MudException {
        // ========================================================================
        //  PERFORM LOOKUPS
        // ========================================================================
        int c = action.arg1, i = action.arg2, quantity = action.arg3;
        MudCharacter mob = dbg.characterDB.get(c);
        Item item = dbg.itemDB.get(i);
        Room room = mob.Room();
        Region region = room.Region();
        // ========================================================================
        //  PERFORM STABILITY CHECKS
        // ========================================================================
        // make sure item and character are in the same room
        if (item.Room() != mob.Room() || item.Region() == null) {
            throw new MudException(new Exception(
                    "Character " + mob.getName() + " tried picking up item " + item.getName()
                    + " but they are not in the same room."), this.getClass().getName());
        }

        if (item.isQuantity() && quantity < 1) {
            quantity = 1;
            /*mob.DoAction(new Action(ActionType.error, 0, 0, 0, 0,
            "You can't get " + p_quantity +
            " of those, it's just not physically possible! FOOL!"));
            return;*/
        }

        if (item.isQuantity() && quantity > item.getQuantity()) {
            mob.DoAction(new Action(ActionType.error, 0, 0, 0, 0,
                    "You can't get " + quantity
                    + ", there are only " + item.getQuantity() + "!"));
            return;

        }
        // ========================================================================
//  PERFORM QUERIES
// ========================================================================

        if (item.DoAction(new Action(ActionType.cangetitem, c, i, quantity)) == Logic.NO) {
            return;
        }

        if (room.DoAction(new Action(ActionType.cangetitem, c, i, quantity)) == Logic.NO) {
            return;
        }

        if (region.DoAction(new Action(ActionType.cangetitem, c, i, quantity)) == Logic.NO) {
            return;
        }

        if (mob.DoAction(new Action(ActionType.cangetitem, c, i, quantity)) == Logic.NO) {
            return;
        }
// ========================================================================
//  PHYSICAL MOVEMENT
// ========================================================================

        Item newitem;
        if (item.isQuantity() && quantity != item.getQuantity()) {
            newitem = dbg.itemDB.generate(item.getTemplateID());      // generate new item
            newitem.setQuantity(quantity);        // set quantity
            item.setQuantity(item.getQuantity() - quantity);      // reset old quantity
        } else {
            // normal transfer, delete from old room
            room.removeItem(item.getID());
            region.removeItem(item.getID());
            newitem
                    = item;
        }

        int newitemid = newitem.getID();
        // now move the item to the player
        newitem.setRoom(mob.getID());
        newitem.setRegion(0);
        mob.addItem(newitem.getID());
        // ========================================================================
        //  PERFORM EVENT NOTIFICATIONS
        // ========================================================================
        room.DoAction(new Action(ActionType.getitem, c, newitemid, quantity));
        newitem.DoAction(new Action(ActionType.getitem, c, newitemid, quantity));
        actionForRoomCharacters(new Action(ActionType.getitem, c, newitemid, quantity), mob.Room());
        actionForRoomItems(new Action(ActionType.getitem, c, newitemid, quantity), mob.Room());
        // ========================================================================
        //  JOIN QUANTITY ITEMS IF NEEDED
        // ========================================================================
        if (newitem.isQuantity()) {
            joinQuantities(mob, newitem);
        }

    }

    /**
     * Drops an item.
     */
    private void dropItem(Action action) throws MudException {
        // ========================================================================
        //  PERFORM LOOKUPS
        // ========================================================================
        int c = action.arg1;
        int i = action.arg2;
        int quantity = action.arg3;
        MudCharacter mob = dbg.characterDB.get(c);
        Item item = dbg.itemDB.get(i);
        Room room = mob.Room();
        Region region = room.Region();
        // ========================================================================
        //  PERFORM STABILITY CHECKS
        // ========================================================================
        if (item.getRoom() != mob.getID() || item.getRegion() != 0) {
            throw new MudException(new Exception(
                    "Character " + mob.getName() + " tried dropping item " + item.getName()
                    + " but he does not own it."), this.getClass().getName());
        }

        if (item.isQuantity() && quantity < 1) {
            mob.DoAction(new Action(ActionType.error, 0, 0, 0, 0,
                    "You can't drop " + quantity
                    + " of those, it's just not physically possible! FOOL!"));
            return;

        }

        if (item.isQuantity() && quantity > item.getQuantity()) {
            mob.DoAction(new Action(ActionType.error, 0, 0, 0, 0,
                    "You can't drop " + quantity
                    + ", there are only " + item.getQuantity() + "!"));
            return;

        }
        // ========================================================================
//  PERFORM QUERIES
// ========================================================================

        if (item.DoAction(new Action(ActionType.candropitem, c, i)) == Logic.NO) {
            return;
        }

        if (room.DoAction(new Action(ActionType.candropitem, c, i)) == Logic.NO) {
            return;
        }

        if (mob.DoAction(new Action(ActionType.candropitem, c, i)) == Logic.NO) {
            return;
        }
// ========================================================================
//  PHYSICAL MOVEMENT
// ========================================================================

        Item newitem;
        if (item.isQuantity() && quantity != item.getQuantity()) {
            newitem = dbg.itemDB.generate(item.getTemplateID());      // generate new item
            newitem.setQuantity(quantity);        // set quantity
            item.setQuantity(item.getQuantity() - quantity);      // reset old quantity
        } else {
            // normal transfer, delete from old player
            mob.removeItem(item.getID());
            newitem
                    = item;
        }
// now move the item to the room

        int newitemid = newitem.getID();
        newitem.setRoom(room.getID());
        newitem.setRegion(region.getID());
        room.addItem(newitem.getID());
        region.addItem(newitem.getID());
        // ========================================================================
        //  PERFORM EVENT NOTIFICATIONS
        // ========================================================================
        room.DoAction(new Action(ActionType.dropitem, c, newitemid, quantity));
        actionForRoomCharacters(new Action(ActionType.dropitem, c, newitemid, quantity), mob.Room());
        actionForRoomItems(new Action(ActionType.dropitem, c, newitemid, quantity), mob.Room());
        // ========================================================================
        //  JOIN QUANTITY ITEMS IF NEEDED
        // ========================================================================
        if (newitem.isQuantity()) {
            joinQuantities(room, newitem);
        }

    }

    /**
     * Give an item to another character
     */
    private void giveItem(Action action) throws MudException {
        // ========================================================================
        //  PERFORM LOOKUPS
        // ========================================================================
        int giverID = action.arg1;
        int receiverID = action.arg2;
        int i = action.arg3;
        int quantity = action.arg4;
        MudCharacter giver = dbg.characterDB.get(giverID);
        MudCharacter receiver = dbg.characterDB.get(receiverID);
        Item item = dbg.itemDB.get(i);
        // ========================================================================
        //  PERFORM STABILITY CHECKS
        // ========================================================================
        if (giver.Room() != receiver.Room()) {
            throw new MudException(new Exception(
                    "Character " + giver.getName() + " tried giving item " + item.getName()
                    + " to " + receiver.getName() + " but they are not in the same room."), this.getClass().getName());
        }

        if (item.isQuantity() && quantity < 1) {
            giver.DoAction(new Action(ActionType.error, 0, 0, 0, 0,
                    "You can't give away " + quantity
                    + " of those, it's just not physically possible! FOOL!"));
            return;

        }

        if (item.isQuantity() && quantity > item.getQuantity()) {
            giver.DoAction(new Action(ActionType.error, 0, 0, 0, 0,
                    "You can't give away " + quantity
                    + ", you only have " + item.getQuantity() + "!"));
            return;

        }
        // ========================================================================
//  PERFORM QUERIES
// ========================================================================

        if (item.DoAction(new Action(ActionType.candropitem, giverID, i, quantity)) == Logic.NO
                || giver.DoAction(new Action(ActionType.candropitem, giverID, i, quantity)) == Logic.NO
                || item.DoAction(new Action(ActionType.canreceiveitem, giverID, receiverID, i, quantity)) == Logic.NO
                || receiver.DoAction(new Action(ActionType.canreceiveitem, giverID, receiverID, i, quantity)) == Logic.NO) {
            return;
        }
// ========================================================================
//  PHYSICAL MOVEMENT
// ========================================================================

        Item newitem;
        if (item.isQuantity() && quantity != item.getQuantity()) {
            newitem = dbg.itemDB.generate(item.getTemplateID());      // generate new item
            newitem.setQuantity(quantity);        // set quantity
            item.setQuantity(item.getQuantity() - quantity);      // reset old quantity
        } else {
            // normal transfer, delete from old player
            giver.removeItem(item.getID());
            newitem
                    = item;
        }
// now move the item to the other player

        int newitemid = newitem.getID();
        newitem.setRoom(receiver.getID());
        receiver.addItem(newitem.getID());
        // ========================================================================
        //  PERFORM EVENT NOTIFICATIONS
        // ========================================================================
        actionForRoomCharacters(new Action(ActionType.giveitem, giverID, receiverID, newitemid, quantity), giver.Room());
        // ========================================================================
        //  JOIN QUANTITY ITEMS IF NEEDED
        // ========================================================================
        if (newitem.isQuantity()) {
            joinQuantities(receiver, newitem);
            joinQuantities(giver, item);
        }

    }

    /**
     * Joins the quanties of two items into one new item. Deletes any but the
     * item instance passed.
     */
    private void joinQuantities(HasItems entity, Item keeper) {
        // go through the items, finding any to merge with "keep"
        for (int i : entity.getItems()) {
            Item item = dbg.itemDB.get(i);
            if (item.getID() == keeper.getID()) {
                continue;
            }
            if (item.getTemplateID() == keeper.getTemplateID()) {
                keeper.setQuantity(keeper.getQuantity() + item.getQuantity());
                item.remove();
                dbg.itemDB.erase(item.getID());
                return;
            }
        }
    }

    /**
     * Spawns a new Item.
     */
    private void spawnItem(Action action) {
        int t = action.arg1;
        int loc = action.arg2;
        int entityType = action.arg3;//0=room
        int quantity = action.arg4;
        Item item = dbg.itemDB.generate(t);
        int newitemID = item.getID();
        if (quantity == 0) {
            quantity = 1;
        }

        item.setQuantity(quantity);
        if (entityType == 0) {
            // load up the room and region
            Room room = dbg.roomDB.get(loc);
            Region region = room.Region();
            // physically place it into the realm
            item.setRoom(room.getID());
            item.setRegion(room.Region().getID());
            room.addItem(item.getID());
            region.addItem(item.getID());
            // tell the room and the region about the new item
            room.DoAction(new Action(ActionType.spawnitem, newitemID));
            region.DoAction(new Action(ActionType.spawnitem, newitemID));
            if (item.isQuantity()) {
                joinQuantities(room, item);
            }

        } else {
            // load up the character
            MudCharacter mob = dbg.characterDB.get(loc);
            // physically place it into the player
            item.setRegion(0);
            item.setRoom(mob.getID());
            mob.addItem(item.getID());
            // tell the characterabout the new item
            mob.DoAction(new Action(ActionType.spawnitem, newitemID));
            if (item.isQuantity()) {
                joinQuantities(mob, item);
            }

        }
    }

    /**
     * Destroys an item.
     */
    private void destroyItem(Action action) {
        int i = action.arg1;
        Item item = dbg.itemDB.get(i);
        if (item.Region() == null) {
            MudCharacter mob = dbg.characterDB.get(item.getRoom());
            mob.DoAction(new Action(ActionType.destroyitem, i));
            item.DoAction(new Action(ActionType.destroyitem, i));
        } else {
            Room room = item.Room();
            Region region = item.Region();
            region.DoAction(new Action(ActionType.destroyitem, i));
            room.DoAction(new Action(ActionType.destroyitem, i));
            item.DoAction(new Action(ActionType.destroyitem, i));
        }

        deleteItem(item);
    }

    /**
     * Destroys a character.
     */
    private void destroyCharacter(Action action) throws MudException {
        int c = action.arg1;
        MudCharacter mob = dbg.characterDB.get(c);
        Room room = mob.Room();
        Region region = mob.Region();
        if (mob.isPlayer()) {
            throw new MudException(new Exception("Trying to delete a player"), this.getClass().getName());
        }

        region.DoAction(new Action(ActionType.destroycharacter, c));
        room.DoAction(new Action(ActionType.destroycharacter, c));
        mob.DoAction(new Action(ActionType.destroycharacter, c));
        // force the items into the room
        for (int k : mob.getItems()) {
            Item item = dbg.itemDB.get(k);
            room.addItem(item.getID());
            region.addItem(item.getID());
            item.setRegion(region.getID());
            item.setRoom(room.getID());
            room.DoAction(new Action(ActionType.dropitem, c, item.getID(), item.getQuantity()));
            region.DoAction(new Action(ActionType.dropitem, c, item.getID(), item.getQuantity()));
        }

        room.removeCharacter(mob.getID());
        region.removeCharacter(mob.getID());
        mob.clearHooks();
        mob.setRoom(0);
        mob.setRegion(0);
        dbg.characterDB.erase(mob.getID());
    }

    /**
     * Spawns a new character based on TEMPLETEID
     */
    private void spawnMob(Action action) {
        int templateID = action.arg1;
        int r = action.arg2;
        MudCharacter mob = dbg.characterDB.generate(templateID);
        int newchar = mob.getID();
        Room room = dbg.roomDB.get(r);
        Region region = room.Region();
        // physically place it into the realm
        mob.setRoom(room.getID());
        mob.setRegion(region.getID());
        room.addCharacter(mob.getID());
        region.addCharacter(mob.getID());
        // tell the room and the region about the new item
        room.DoAction(new Action(ActionType.spawncharacter, newchar));
        region.DoAction(new Action(ActionType.spawncharacter, newchar));
    }

    /**
     * Performs only to delivery logic messages.
     */
    private void logicAction(Action action) throws MudException {
        String logicname = action.getData().substring(0, action.getData().indexOf(' ')).trim();
        Logic logic = null;
        EntityType type = EntityType.values()[action.arg1];
        switch (type) {
            case Character:
                logic = dbg.characterDB.get(action.arg2).getLogic(logicname);
                break;

            case Item:
                logic = dbg.itemDB.get(action.arg2).getLogic(logicname);
                break;

            case Room:
                logic = dbg.roomDB.get(action.arg2).getLogic(logicname);
                break;

            case Portal:
                logic = dbg.portalDB.get(action.arg2).getLogic(logicname);
                break;

            case Region:
                logic = dbg.regionDB.get(action.arg2).getLogic(logicname);
                break;

        }

        if (logic == null) {
            throw new MudException(new Exception("Game::LogicAction: Cannot load logic " + logicname), this.getClass().getName());
        }

        logic.DoAction(action);
    }

    /**
     * Addes a logic to anything
     */
    private void addLogic(Action action) {
        EntityType type = EntityType.values()[action.arg1];
        switch (type) {
            case Character:
                dbg.characterDB.get(action.arg2).addLogic(action.getData());
                break;

            case Item:
                dbg.itemDB.get(action.arg2).addLogic(action.getData());
                break;

            case Room:
                dbg.roomDB.get(action.arg2).addLogic(action.getData());
                break;

            case Portal:
                dbg.portalDB.get(action.arg2).addLogic(action.getData());
                break;

            case Region:
                dbg.regionDB.get(action.arg2).addLogic(action.getData());
                break;

        }

    }

    /**
     * Removes a logic from anything
     */
    private void delLogic(Action action) {
        EntityType type = EntityType.values()[action.arg1];
        switch (type) {
            case Character:
                dbg.characterDB.get(action.arg2).delLogic(action.getData());
                break;

            case Item:
                dbg.itemDB.get(action.arg2).delLogic(action.getData());
                break;

            case Room:
                dbg.roomDB.get(action.arg2).delLogic(action.getData());
                break;

            case Portal:
                dbg.portalDB.get(action.arg2).delLogic(action.getData());
                break;

            case Region:
                dbg.regionDB.get(action.arg2).delLogic(action.getData());
                break;

        }

    }

    /**
     * Adds a character to the realm's list.
     */
    private void addCharacter(MudCharacter character) {
        characters.add(character);
    }

    /**
     * Adds a player to the realm's list.
     */
    private void addPlayer(MudCharacter player) {
        players.add(player);
    }

    /**
     * Removes a character from the realm's list.
     */
    private void removeCharacter(MudCharacter character) {
        characters.remove(character);
    }

    /**
     * Removes a player from the realm's list
     */
    private void removePlayer(MudCharacter player) {
        players.remove(player);
    }

    /**
     * Deletes an item. First removes it and it hooks, then "erases" the
     * database.
     */
    private void deleteItem(Item item) {
        if (item.Region() != null) {
            Region region = item.Region();
            region.removeItem(item.getID());
            Room room = item.Room();
            room.removeItem(item.getID());
        } else {
            MudCharacter mob = dbg.characterDB.get(item.getRoom());
            mob.removeItem(item.getID());
        }

        item.setRoom(0);
        item.setRegion(0);
        item.clearHooks();
        dbg.itemDB.erase(item.getID());
    }

    /**
     * saves a region.
     */
    private void saveRegion(Action action) {
        dbg.regionDB.saveRegion(dbg.regionDB.get(action.arg1));
    }

    /**
     * reloads all the item templates from a file.
     */
    private void reloadItemTemplates(String file) {
        dbg.itemDB.loadTemplate(file);
    }

    /**
     * reloads all character templates from a file.
     */
    private void reloadCharacterTemplates(String file) {
        dbg.characterDB.loadTemplate(file);
    }

    /**
     * reloads a region from a file.
     */
    private void reloadRegion(String name) {
        dbg.regionDB.loadRegion(name);
    }

    /**
     * reloads a command script from a file.
     */
    private void reloadCommandScript(String name, ScriptReloadMode mode) throws MudException {
        dbg.commandDB.reload(name, mode);
    }

    /**
     * reloads a logic script from a file.
     */
    private void reloadLogicScript(String name, ScriptReloadMode mode) {
        dbg.logicDB.reload(name, mode);
    }

    /**
     * saves all the game timers in the queue.
     */
    private void saveTimers() throws MudException {
        PrintWriter timerfile = null;
        try {
            timerfile = new PrintWriter(new FileWriter("data/timers/timers.data"));
            timerfile.println("[GAMETIME]");
            timerfile.println(getGameTime());
            timerfile.println("[NUM ACTIONS]");
            timerfile.println(timerregistry.size());
            // copy the timer queue
            while (timerregistry.size() > 0) {
                TimedAction a = timerregistry.remove();
                a.save(timerfile);
            }

        } catch (IOException ex) {
            throw new MudException(ex, this.getClass().getName());
        } finally {
            timerfile.close();
        }

    }

    /**
     * Loads all the game times from a file.
     */
    private void loadTimers() throws MudException {
        BufferedReader timerfile = null;
        try {
            timerfile = new BufferedReader(new FileReader("data/timers/timers.data"));
            timerfile.readLine();//[GAMETIME]
            long t = Long.parseLong(timerfile.readLine());
            gametime.reset(t);
            timerfile.readLine();//[NUM ACTIONS]
            int size = Integer.parseInt(timerfile.readLine());
            for (int i = 0; i < size; i++) {
                TimedAction a = new TimedAction();
                a.load(timerfile);
                addTimedAction(a);
            }
        } catch (IOException ex) {
            throw new MudException(ex, this.getClass().getName());
        } finally {
            try {
                timerfile.close();
            } catch (IOException ex) {
                throw new MudException(ex, this.getClass().getName());
            }
        }

    }
// ------------------------------------------------------------------------
// Logged-in-player accessors.
// ------------------------------------------------------------------------

    /**
     * Finds a player online based on a partial match.
     *
     * @param name Name to search
     * @return Player online or NULL
     */
    public MudCharacter findPlayerOnlinePart(
            String name) {
        for (MudCharacter p : players) {
            if (p.getName().toLowerCase().contains(name.toLowerCase())) {
                return p;
            }

        }
        return null;
    }

    /**
     * Returns a player online using a full match.
     *
     * @param name Name to match.
     * @return Player online or NULL
     */
    public MudCharacter findPlayerOnlineFull(
            String name) {
        for (MudCharacter p : players) {
            if (p.getName().toLowerCase().equals(name.toLowerCase())) {
                return p;
            }

        }
        return null;
    }

    /**
     * Finds a player from the databse using a partial match.
     *
     * @param name Name to match.
     * @return Returns the player or NULL.
     */
    public MudCharacter findPlayerPart(
            String name) {
        return dbg.characterDB.findPlayerPart(name);
    }

    /**
     * Finds a player from teh database using a full match.
     *
     * @param name Name to match.
     * @return Player or NULL
     */
    public MudCharacter findPlayerFull(
            String name) {
        return dbg.characterDB.findPlayerFull(name);
    }

    /**
     * Checks to see if the List of players contains this player.
     *
     * @param player Player to compare.
     * @return TRUE if on list.
     */
    public boolean hasPlayer(MudCharacter player) {
        return players.contains(player);
    }

    /**
     * gets the game time.
     *
     * @return Game Time.
     */
    public long getGameTime() {
        return gametime.getMS();
    }

    /**
     * Resets the game Time.
     */
    public void resetTime() {
        gametime.reset();
    }

    /**
     * Adds an action to the queue. Action is timed relative from when it was
     * being added.
     *
     * @param time Offset from the action being added to the queue.
     * @param action Action to perfom
     */
    public void addActionRelative(long time, Action action) {
        addTimedAction(new TimedAction(time + getGameTime(), action));
    }

    /**
     * Adds an action to the queue. Action is timed relative from when it was
     * being added.
     *
     * @param time Offset from when Added to queue.
     * @param actiontype Action information
     * @param data1 Action information
     * @param data2 Action information
     * @param data3 Action information
     * @param data4 Action information
     * @param data Action information
     */
    public void addActionRelative(long time, ActionType actiontype, int data1, int data2, int data3, int data4, String data) {
        addTimedAction(new TimedAction(time + getGameTime(), new Action(actiontype, data1, data2, data3, data4, data)));
    }

    /**
     * Action added to the queue with an absolute running time.
     *
     * @param time Time to run.
     * @param action Action to run.
     */
    public void addActionAbsolute(long time, Action action) {
        addTimedAction(new TimedAction(time, action));
    }

    /**
     * Action added to the queue with an absolute running time.
     *
     * @param time Time to run
     * @param actiontype Action Information
     * @param data1 Action Information
     * @param data2 Action Information
     * @param data3 Action Information
     * @param data4 Action Information
     * @param data Action Information
     */
    public void addActionAbsolute(long time, ActionType actiontype, int data1, int data2, int data3, int data4, String data) {
        addTimedAction(new TimedAction(time, new Action(actiontype, data1, data2, data3, data4, data)));
    }

    /**
     * Addes a timed action to the queue. Called by other "addActions" methos.
     *
     * @param action To add.
     */
    private void addTimedAction(TimedAction action) {
        timerregistry.add(action);
        action.hook();
    }

    /**
     * Manual cleans up the databases.
     */
    private void cleanup() {
        dbg.characterDB.cleanup();
        dbg.itemDB.cleanup();
    }

    /**
     * Saves only players.
     */
    private void savePlayers() {
        dbg.accountDB.save();
        dbg.characterDB.savePlayers();
    }

    /**
     * Is the mud running or not.
     *
     * @return TRUE if running.
     */
    public boolean isRunning() {
        return Running;
    }

    /**
     * Loads all data from file. Normally called at start of game only. Else it
     * is really reloading is it not?
     *
     * @throws MudException
     */
    public void loadall() throws MudException {
        System.out.println("Loading.......");
        Running
                = true;
        // load the templates and accounts first; they depend on nothing else
        System.out.println("<--Loading mob templates.......");
        dbg.characterDB.loadTemplates();
        System.out.println("<--Loading item templates.......");
        dbg.itemDB.loadTemplates();
        // load the scripts now
        System.out.println("<--Loading commands.......");
        dbg.commandDB.loadDB();
        System.out.println("<--Loading logics.......");
        dbg.logicDB.loadDB();
        // load the regions
        System.out.println("<--Loading regions.......");
        dbg.regionDB.loadAll();
// <editor-fold defaultstate="collapsed" desc="removed due to a bug that kept loading players twice">
        //System.out.println("<--Loading players.......");
        //dbg.characterDB.loadPlayers();// </editor-fold>
        System.out.println("<--Loading timers.......");
        loadTimers();

        System.out.println("<--Loading accounts.......");
        dbg.accountDB.load();
    }

    /**
     * saves all data to disk.
     *
     * @throws MudException
     */
    public void saveall() throws MudException {
        dbg.accountDB.save();
        dbg.characterDB.savePlayers();
        dbg.regionDB.saveAll();
        saveTimers();

    }

    /**
     * shutsdown the mud.
     */
    public void shutdown() {
        System.out.println("Mud Shutting down...........");
        Running
                = false;
    }

    /**
     * Updates the ticker. As of yet does nothing.
     */
    public void tickUpdate() {
        System.out.println("Running a tick update: " + (++tickCount));
    }

    /**
     * Takes a string in and spits it based on quotation marks(") or spaces. If
     * quotation marks are deteted, it spilts the string into substring based
     * solely on the marks. Else it splits using Strings own split() method.
     *
     * Example using quotaion marks command "I love this mud" It's great.
     *
     * That becomes two strings = "I Love this mud" and "It's great".
     *
     * UPDATED 080224 - Uses Pattern and Matcher now to do substrings.
     *
     * @param data String to spilt
     * @return An arry of the spilt strings.
     */
    static public String[] splitString(String data) {
        Pattern qmarks = Pattern.compile("\\G\"");
        Pattern end = Pattern.compile("\\G\\z");
        Pattern word = Pattern.compile("\\G\\w+");
        Pattern punct = Pattern.compile("\\G\\p{Punct}");
        Pattern space = Pattern.compile("\\G\\s");
        Pattern number = Pattern.compile("\\G\\d+\\.?\\d*");
        data = data.toLowerCase().trim();

        try {
            Matcher mat = end.matcher(data);
            List<String> tokens = new ArrayList<String>();
            String token = null;
            String superToken = "";
            boolean inMarks = false;
            boolean ending = false;

            do {
                if (inMarks == false) {
                    superToken = "";
                }

                mat.usePattern(space).find();
                if (mat.usePattern(qmarks).find()) {
                    token = mat.group();
                    inMarks
                            = !inMarks;
                } else if (mat.usePattern(number).find()) {
                    token = mat.group();
                } else if (mat.usePattern(word).find()) {
                    token = mat.group();
                } else if (mat.usePattern(punct).find()) {
                    token = mat.group();
                } else if (mat.usePattern(end).find()) {
                    token = mat.group();
                    ending
                            = true;
                    if (inMarks) {
                        inMarks = false;
                    }

                } else {
                    token = null;

                }

                if (token == null) {
                    Logger.getLogger(Mud.class.getName()).severe("Error in tolkenizing command String");
                    //throw new MudException (new Exception("Error in tolkenizing command string!"),Mud.class.getName());
                }

                if (inMarks && !token.equals("\"")) {
                    superToken = superToken + " " + token;
                }

                if (superToken.length() > 0) {
                    token = superToken.trim();
                }

                if ((token.length() > 0) && (!inMarks)) {
                    tokens.add(token);
                }

            } while (token.length() != 0 && !ending);
            return tokens.toArray(new String[tokens.size()]);
        } catch (Exception ex) {
            Logger.getLogger(Mud.class.getName()).severe(ex.toString());
            //throw new MudException(ex, Mud.class.getName());
        }
        return new String[]{data};
    }

    /**
     * Takes a string in and spits it based on quotation marks(") or spaces. If
     * quotation marks are deteted, it spilts the string into substring based
     * solely on the marks. Else it splits using Strings own split() method.
     *
     * Example using quotaion marks command "I love this mud" It's great.
     *
     * That becomes two strings = "I Love this mud" and "It's great".
     *
     * @param data String to spilt
     * @return An arry of the spilt strings.
     */
    static public String[] splitString2(String data) {
        int count = 0;
        if (data.contains("\"")) {
            char[] string = data.toCharArray();
            for (char c : string) {
                if (c == '\"') {
                    count++;
                }

            }
            if (count % 2 != 0) {
                return new String[]{data};
            }

            String[] values = data.split("\"");
            String[] answer = null;
            if (data.startsWith("\"")) {
                answer = new String[values.length - 1];
                for (int i = 1; i
                        < values.length; i++) {
                    answer[i - 1] = values[i];
                }

                values = answer;
            }

            count = 0;
            for (int i = 0; i
                    < values.length; i++) {
                values[i] = values[i].trim();
                if (values[i].isEmpty()) {
                    count++;
                }

            }
            if (count == 0) {
                return values;
            }

            answer = new String[answer.length - count];
            count
                    = 0;
            for (int i = 0; i
                    < values.length; i++) {
                if (!values[i].isEmpty()) {
                    answer[count++] = values[i];
                }

            }
            return answer;
        } else {
            return data.split("\\s*");
        }
    }
}
