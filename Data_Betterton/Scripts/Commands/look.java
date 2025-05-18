package Commands;

import Actions.Action;
import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;
import Entities.Portal;
import Entities.PortalEntry.PortalEntry;
import Entities.Room;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.1.0
 * 1.  In the process of adding support to "look" at other objects.  This 
 * version adds the ability to "look" at a portal.
 * Version 1.0.0
 *
 */
public class look extends Command {

    public look(MudCharacter mob) {
        super(mob,
                "Look",
                "\"look <|object>\"",
                "Looks at the room (default), or at an optional object within the room");
        loadable = false;
    }

    @Override
    public void Execute(String p_parameters) {
        if (p_parameters != null && !p_parameters.equals("")) {
            String object = "";
            if (p_parameters.contains(" ")) {
                object = p_parameters.substring(0, p_parameters.indexOf(" "));
            } else {
                object = p_parameters;
            }
            for (Integer j : Mob().getItems()){
                Item item = Mud.Mud.dbg.itemDB.get(j);
                if (item.getName().toLowerCase().contains(object.toLowerCase())){
                    Mob().DoAction(new Action (ActionType.seeitem,item.getID()));
                    return;
                }
            }
            Room room = Mob().Room();
            for (Integer j : room.getItems()) {
                Item item = Mud.Mud.dbg.itemDB.get(j);
                if (item.getName().toLowerCase().contains(object.toLowerCase())) {
                    Mob().DoAction(new Action(ActionType.seeitem, item.getID()));
                    return;
                }
            }
            for (Integer i : room.getCharacters()) {
                MudCharacter mob = Mud.Mud.dbg.characterDB.get(i);
                if (mob.getName().toLowerCase().contains(object.toLowerCase())) {
                    Mob().DoAction(new Action(ActionType.seemob, mob.getID()));
                    return;
                }
            }
            for (Portal p : room.Portals()){
                if (p.getName().toLowerCase().contains(object.toLowerCase())){
                    Mob().DoAction(new Action(ActionType.seeportal,p.getID()));
                    return;
                }
            }
            for (Portal p: room.Portals()){
                for (PortalEntry pe: p.getPortalEntries()){
                    if (pe.getDirectionName().toLowerCase().contains(object.toLowerCase())&&Mob().getRoom()==pe.getStartRoom()){
                        Mob().DoAction(new Action(ActionType.seeportal,p.getID()));
                        Mob().DoAction(new Action(ActionType.seeroom, pe.getDestinationRoom()));
                        return;
                    }
                }
            }
        }

        Mob().DoAction(new Action(ActionType.seeroom, Mob().Room().getID()));
    }
}
