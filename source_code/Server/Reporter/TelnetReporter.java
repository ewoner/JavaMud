package Server.Reporter;

import Actions.Action;
import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;
import Entities.Portal;
import Entities.PortalEntry.PortalEntry;
import Entities.Room;
import Logics.Logic;
import Server.NewConnection;
import Server.Protocol.Telnet;

/**
 * This class handles the output to a telnet client.  It is a logic and as such
 * will be attached to a player in the game.
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.1.0
 *  1.  Added support in the "DoAction" method to "look" at a portal closer.
 * crashing as well as added the "SeePortal" method to handdle the request.
 * Version 1.0.0
 */
public class TelnetReporter extends Logic implements Reporter {

    private boolean loadable = false;
    private MudCharacter player;
    private NewConnection<Telnet> connection;
    //static public DatabaseGroup dbg = Mud.dbg;

    public TelnetReporter(MudCharacter player, NewConnection<Telnet> conn) {
        this.player = player;
        this.connection = conn;
    }

    @Override
    public String name() {
        return "telnetreporter";
    }

    @Override
    public boolean canSave() {
        return false;
    }

    @Override
    public int DoAction(Action p_action) {
        int i = 1;
        // someone entered a room
        if (p_action.getType() == ActionType.enterroom) {
            EnterRoom(dbg.characterDB.get(p_action.arg1), dbg.portalDB.get(p_action.arg2));
        } // someone left a room
        else if (p_action.getType() == ActionType.leaveroom) {
            LeaveRoom(dbg.characterDB.get(p_action.arg1), dbg.portalDB.get(p_action.arg2));
        } // someone said something
        else if (p_action.getType() == ActionType.say) {
            MudCharacter c = dbg.characterDB.get(p_action.arg1);
            SendString("<$yellow>" + c.getName() + " says: <$reset>" + p_action.getData());
        } // you see a room
        else if (p_action.getType() == ActionType.seeroom) {
            SeeRoom(dbg.roomDB.get(p_action.arg1));
        } // you see a room name
        else if (p_action.getType() == ActionType.seeroomname) {
            SeeRoomName(dbg.roomDB.get(p_action.arg1));
        } // you see a room descrioption
        else if (p_action.getType() == ActionType.seeroomdescription) {
            SeeRoomDesc(dbg.roomDB.get(p_action.arg1));
        } // you see a room's exits
        else if (p_action.getType() == ActionType.seeroomexits) {
            SeeRoomExits(dbg.roomDB.get(p_action.arg1));
        } // you see a room's characters
        else if (p_action.getType() == ActionType.seeroomcharacters) {
            SeeRoomCharacters(dbg.roomDB.get(p_action.arg1));
        } // you see a room's items
        else if (p_action.getType() == ActionType.seeroomitems) {
            SeeRoomItems(dbg.roomDB.get(p_action.arg1));
        }// see mob up close
        else if (p_action.getType() == ActionType.seemob) {
            SeeMob(dbg.characterDB.get(p_action.arg1));
        }//see item up close
        else if (p_action.getType() == ActionType.seeitem) {
            SeeItem(dbg.itemDB.get(p_action.arg1));
        }//see protal up close
        else if (p_action.getType() == ActionType.seeportal) {
            SeePortal(dbg.portalDB.get(p_action.arg1));
        } // you left the game
        else if (p_action.getType() == ActionType.leave) {
            connection.removeHandler();
        } // you hung up
        else if (p_action.getType() == ActionType.hangup) {
            connection.close();
            connection.clearHandlers();
        } // you got an error
        else if (p_action.getType() == ActionType.error) {
            SendString("<$bold><$red>" + p_action.getData());
        } // you saw an announcement
        else if (p_action.getType() == ActionType.announce) {
            SendString("<$bold><$cyan>" + p_action.getData());
        } // you saw something
        else if (p_action.getType() == ActionType.vision) {
            SendString("<$bold><$green>" + p_action.getData());
        } // you heard someone chatting
        else if (p_action.getType() == ActionType.chat) {
            MudCharacter c = dbg.characterDB.get(p_action.arg1);
            SendString("<$bold><$magenta>" + c.getName() + " chats: <$reset>" + p_action.getData());
        } // someone whispered to you
        else if (p_action.getType() == ActionType.whisper) {
            MudCharacter c = dbg.characterDB.get(p_action.arg1);
            SendString("<$bold><$yellow>" + c.getName() + " whispers to you: <$reset>" + p_action.getData());
        } // someone entered the realm
        else if (p_action.getType() == ActionType.enterrealm) {
            MudCharacter c = dbg.characterDB.get(p_action.arg1);
            SendString("<$bold><$white>" + c.getName() + " enters the realm.");
        } // someone left the realm
        else if (p_action.getType() == ActionType.leaverealm) {
            MudCharacter c = dbg.characterDB.get(p_action.arg1);
            SendString("<$bold><$white>" + c.getName() + " leaves the realm.");
        } // some poor sap died
        else if (p_action.getType() == ActionType.die) {
            Died(dbg.characterDB.get(p_action.arg1));
        } // someone gave an item to someone else
        else if (p_action.getType() == ActionType.giveitem) {
            GaveItem(dbg.characterDB.get(p_action.arg1), dbg.characterDB.get(p_action.arg2), dbg.itemDB.get(p_action.arg3));
        } // someone dropped an item
        else if (p_action.getType() == ActionType.dropitem) {
            DropItem(dbg.characterDB.get(p_action.arg1), dbg.itemDB.get(p_action.arg2));
        } // someone picked up an item
        else if (p_action.getType() == ActionType.getitem) {
            GetItem(dbg.characterDB.get(p_action.arg1), dbg.itemDB.get(p_action.arg2));
        }
        return 0;
    }

    @Override
    public void SeeRoom(Room room) {
        SeeRoomName(room);
        if (player.isVerbose() == true) {
            SeeRoomDesc(room);
        }
        SeeRoomExits(room);
        SeeRoomCharacters(room);
        SeeRoomItems(room);
    }

    @Override
    public void SeeRoomName(Room room) {
        SendString("<#FFFFFF>" + room.getName());
    }

    @Override
    public void SeeRoomDesc(Room room) {
        SendString("<$reset>" + room.getDescription());
    }

    @Override
    public void SeeRoomExits(Room room) {
        if (room.getPortals().isEmpty()) {
            return;
        }
        String str = "<#FF00FF>Exits: <$reset>";

        for (Integer i : room.getPortals()) {
            Portal p = dbg.portalDB.get(i);
            PortalEntry pe = p.seekStartRoom(room.getID());
            if (pe != null) {
                str += pe.getDirectionName();
                str += " - ";
                str += p.getName();
                str += ", ";
            //str += "<#FF00FF>, <$reset>";
            }
        }
        SendString(str);
    }

    @Override
    public void SeeRoomCharacters(Room room) {

        if (room.getCharacters().isEmpty()) {
            return;
        }
        String str = "<#FFFF00>People: <$reset>";
        for (Integer i : room.getCharacters()) {
            MudCharacter c = dbg.characterDB.get(i);
            str += c.getName();
            str += ", ";
        //str += "<#FFFF00>, <$reset>";
        }
        SendString(str);
    }

    @Override
    public void SeeRoomItems(Room room) {
        if (room.getItems().isEmpty()) {
            return;
        }
        String str = "<#FFFF00>Items: <$reset>";
        for (Integer i : room.getItems()) {
            Item c = dbg.itemDB.get(i);
            str += c.getName();
            str += ", ";
        //str += "<#FFFF00>, <$reset>";
        }
        SendString(str);
    }

    @Override
    public void EnterRoom(MudCharacter mob, Portal portal) {

        if (mob == player) {
            SeeRoom(mob.Room());
            return;
        }
        if (portal == null) {
            SendString("<$bold><$white>" + mob.getName() + " appears from nowhere!");
        } else {
            SendString("<$bold><$white>" + mob.getName() + " enters from the " +
                    portal.getName() + ".");
        }
    }

    @Override
    public void LeaveRoom(MudCharacter mob, Portal p_portal) {

        if (mob == player) {
            if (p_portal == null) {
                SendString("<$bold><$white>You disappear!");
                return;
            }
            SendString("<$bold><$white>You enter the " + p_portal.getName());
            return;
        }
        if (p_portal == null) {
            SendString("<$bold><$white>" + mob.getName() + " disappears!");
            return;
        }
        SendString("<$bold><$white>" + mob.getName() + " enters the " +
                p_portal.getName() + ".");
    }

    @Override
    public void Died(MudCharacter mob) {
        String str = "<$bold><$red>";
        if (mob != player) {
            str += mob.getName();
            str += " HAS DIED!!!";
        } else {
            str += "YOU HAVE DIED!!!";
        }
        SendString(str);
    }

    @Override
    public void GetItem(MudCharacter mob, Item item) {
        String str = "<$bold><$yellow>";
        if (mob == player) {
            str += "You pick up ";
        } else {
            str += mob.getName();
            str += " picks up ";
        }
        str += item.getName();
        SendString(str);
    }

    @Override
    public void DropItem(MudCharacter mob, Item item) {
        String str = "<$bold><$yellow>";
        if (mob == player) {
            str += "You drop ";
        } else {
            str += mob.getName();
            str += " drops ";
        }
        str += item.getName();
        SendString(str);
    }

    @Override
    public void GaveItem(MudCharacter giver, MudCharacter receiver, Item item) {
        String str = "<$bold><$yellow>";
        if (giver == player) {
            str += "You give ";
        } else {
            str += giver.getName();
            str += " gives ";
        }
        str += item.getName();
        str += " to ";

        if (receiver == player) {
            str += "you.";
        } else {
            str += receiver.getName();
            str += ".";
        }
        SendString(str);
    }

    @Override
    public void SendString(String string) {
        connection.getProtocol().sendString(connection, string + "<$reset>\r\n");

    }

    private void SeeItem(Item item) {
        String str = "<#FFFF00>Item: <$reset>";
        if (item.isQuantity() && item.getQuantity() > 1) {
            str += item.getName() + " are here.  ";
        } else {
            str += "A " + item.getName() + " is here.  ";
        }
        str += item.getDescription();
        SendString(str);
    }

    private void SeeMob(MudCharacter mob) {
        String str = "<#FFFF00>Character: <$reset>";
        str += mob.getName() + " is standing here.  ";
        str += mob.getDescription() + "  ";
        str += "They are carring the following: ";
        for (Integer o : mob.getItems()) {
            Item item = dbg.itemDB.get(o);
            str += item.getName();
            str += "<#FFFF00>, <$reset>";
        }
        SendString(str);
    }

    public NewConnection<Telnet> getConnection() {
        return connection;
    }

    private void SeePortal(Portal portal) {
        String str = "<#FFFF00>Portal: <$reset>";
        str += portal.getName() + "\n";
        str += portal.getDescription() + "\n";
        str += "This portal has the following exits: <#FFFF00>";
        boolean comma = false;
        for (PortalEntry pe : portal.getPortalEntries()) {
            if (comma) {
                str += ", <#FFFF00>";
            }
            str += pe.getDirectionName()+"<$reset>";
            comma = true;
        }
        SendString(str);
    }
}
