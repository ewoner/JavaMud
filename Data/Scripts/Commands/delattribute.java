package Commands;

import Actions.Action;
import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;
import Entities.Region;
import Entities.Room;
import Mud.Mud;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 * Version 1.0.0
 *
 */
public class delattribute extends Command {

    public delattribute(MudCharacter mob) {
        super(mob, "delAttribute", "\"delAttribute <ITEM/MOB/ROOM/REGION> <name/ID> <attribute> <int value>\"",
                "Attempts to delete an Attribute on an item, character region or room.  You must be located in the same room as the object you are modifing.");
    }

    @Override
    public void Execute(String args) {
        if (args.isEmpty()) {
            badUsage();
            return;
        }
        String[] params = Mud.splitString(args);
        if (params.length != 4) {
            badUsage();
            return;
        }
        int value;
        try {
            value = Integer.parseInt(params[3]);
        } catch (Exception ex) {
            badUsage("Value must ba an whole number only.");
            return;
        }
        if (params[0].equalsIgnoreCase("item")) {
            Item item = Mob().seekItem(params[1]);
            if (item == null) {
                badUsage("Item not found.");
                return;
            }
            item.delAttribute(params[2]);
            Mob().DoAction(new Action(ActionType.announce, 0, 0, 0, 0, item.getName() + "'s attribute " + params[2] + " has been changed to " + value + "."));
        } else if (params[0].equalsIgnoreCase("mob")) {
            MudCharacter mob = Mob().Room().seekCharacter(params[2]);
            if (mob == null) {
                badUsage("Mob not found");
                return;
            }
            mob.delAttribute(params[2]);
            Mob().DoAction(new Action(ActionType.announce, 0, 0, 0, 0, mob.getName() + "'s attribute " + params[2] + " has been changed to " + value + "."));
        } else if (params[0].equalsIgnoreCase("room")) {
            Room room = Mob().Room();
            room.delAttribute(params[2]);
            Mob().DoAction(new Action(ActionType.announce, 0, 0, 0, 0, room.getName() + "'s attribute " + params[2] + " has been changed to " + value + "."));
        } else if (params[0].equalsIgnoreCase("region")) {
            Region reg = Mob().Region();
            reg.delAttribute(params[2]);
            Mob().DoAction(new Action(ActionType.announce, 0, 0, 0, 0, reg.getName() + "'s attribute " + params[2] + " has been changed to " + value + "."));
        } else {
            badUsage();
            return;
        }
    }
}
