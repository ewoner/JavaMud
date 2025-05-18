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
 * Version 1.0.1
 * 1.  Fixed an error with the "mob" option that was sending the wrong paramater
 * to the "seekCharacter" method that resulted in no mob being found.
 * Version 1.0.0
 *
 */
public class addattribute extends Command {

    public addattribute(MudCharacter mob) {
        super(mob, "addAttribute", "\"addAttribute <ITEM/MOB/ROOM/REGION> <name/ID> <attribute> <int value>\"",
                "Attempts to add an Attribute an item, character, region or room.  If an atrribute by the name already exsists, it's value is changed.  You must be located in the same room as the object you are modifing.");
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
            Item item = Mob().Room().seekItem(params[1]);
            if (item == null) {
                badUsage("Item not found.");
                return;
            }
            item.addAttribute(params[2], value);
            Mob().DoAction(new Action(ActionType.announce, 0, 0, 0, 0, item.getName() + "'s attribute " + params[2] + " has been changed to " + value + "."));
        } else if (params[0].equalsIgnoreCase("mob")) {
            MudCharacter mob = Mob().Room().seekCharacter(params[1]);
            if (mob == null) {
                badUsage("Mob not found");
                return;
            }
            mob.addAttribute(params[2], value);
            Mob().DoAction(new Action(ActionType.announce, 0, 0, 0, 0, mob.getName() + "'s attribute " + params[2] + " has been changed to " + value + "."));
        } else if (params[0].equalsIgnoreCase("room")) {
            Room room = Mob().Room();
            room.addAttribute(params[2], value);
            Mob().DoAction(new Action(ActionType.announce, 0, 0, 0, 0, room.getName() + "'s attribute " + params[2] + " has been changed to " + value + "."));
        } else if (params[0].equalsIgnoreCase("region")) {
            Region reg = Mob().Region();
            reg.addAttribute(params[2], value);
            Mob().DoAction(new Action(ActionType.announce, 0, 0, 0, 0, reg.getName() + "'s attribute " + params[2] + " has been changed to " + value + "."));
        } else {
            badUsage();
            return;
        }
    }
}
