package Commands;

import Actions.Action;
import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;
import Server.Main;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 * Version 1.1.0
 * 1.  Added support for the "all" option.
 * Version 1.0.0
 *
 */
public class drop extends Command {

    public drop(MudCharacter mob) {
        super(mob, "drop", "\"drop <ALL/item>\"",
                "This makes your character attempt to drop an item.  Using \"ALL\" will drop everything in the characters inventory.");
    }

    @Override
    public void Execute(String args) {
        if (args.isEmpty()) {
            badUsage();
            return;
        }

        String[] parms = Mud.Mud.splitString(args);
        int quantity = 0;
        String itemname = args;
        if (parms.length == 2) {
            try {
                quantity = Integer.parseInt(parms[0]);
            } catch (Exception ex) {
                Mob().DoAction(new Action(ActionType.error, 0, 0, 0, 0, "Error getting quantity.  Please try again."));
                Logger.getLogger(get.class.getName()).log(Level.SEVERE, null, new Exception("Error in \"get\" command paraing quantity.\"" + args + "\"."));
                return;
            }
            itemname = parms[1];
        } else if (parms.length != 1) {
            badUsage();
            return;
        }

        if (itemname.equalsIgnoreCase("all")) {
            for (Item i : Mob().Items()) {
                quantity = i.getQuantity();
                try {
                    Main.game.doAction(new Action(ActionType.attemptdropitem, Mob().getID(), i.getID(), quantity, 0));
                } catch (Exception ex) {
                    Logger.getLogger(drop.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return;
        }
        Item i = Mob().seekItem(itemname);
        if (i == null) {
            badUsage();
            return;
        }
        // if user didn't specify the quantity of a quantity item,
        // just get the entire amount.
        if (i.isQuantity() && quantity == 0) {
            quantity = i.getQuantity();
        }
        try {
            Main.game.doAction(ActionType.attemptdropitem, Mob().getID(), i.getID(), quantity, 0, "");
        } catch (Exception ex) {
            Logger.getLogger(drop.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}
