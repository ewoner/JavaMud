package Commands;

import Actions.Action;
import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;
import Entities.Room;
import Server.Main;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.1.0
 * 1.  Added support for "get all"
 * Version 1.0.0
 *
 */
public class get extends Command {

    public get(MudCharacter mob) {
        super(mob, "Get", "\"get <|quantity> <ALL/item>\"",
                "This makes your character attempt to pick up an item.  Using \"ALL\" will attempt to pick up all items in the room.");
    }

    @Override
    public void Execute(String parameters) {

        if (parameters.isEmpty()) {
            Mob().DoAction(new Action(ActionType.error, 0, 0, 0, 0, getUsage() + ":  This command requires a target to get."));
            return;
        }

        Room r = Mob().Room();
        int quantity = 0;
        char c = parameters.charAt(0);
        String itemname = parameters;
        String[] parms = Mud.Mud.splitString(parameters);

        if (parms.length == 2) {
            try {
                quantity = Integer.parseInt(parms[0]);
            } catch (Exception ex) {
                Mob().DoAction(new Action(ActionType.error, 0, 0, 0, 0, "Error getting quantity.  Please try again."));
                Logger.getLogger(get.class.getName()).log(Level.SEVERE, null, new Exception("Error in \"get\" command paraing quantity.\"" + parameters + "\"."));
                return;
            }
            itemname = parms[1];
        } else if (parms.length != 1) {
            badUsage();
            return;
        }
        if (itemname.equalsIgnoreCase("all")) {
            for (Item i : Mob().Room().Items()) {
                try {
                    Main.game.doAction(ActionType.attemptgetitem, Mob().getID(), i.getID(), i.getQuantity(), 0, "");
                } catch (Exception ex) {
                    Logger.getLogger(get.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return;
        }
        Item item = r.seekItem(itemname);
        if (item == null) {
            Mob().DoAction(new Action(ActionType.error, 0, 0, 0, 0, "Sorry " + parameters + " could not be found."));
            return;
        }
        if (item.isQuantity() && quantity == 0) {
            quantity = item.getQuantity();
        }
        try {

            Main.game.doAction(ActionType.attemptgetitem, Mob().getID(), item.getID(), quantity, 0, "");
        } catch (Exception ex) {
            Logger.getLogger(get.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
