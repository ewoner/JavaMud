package Commands;

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
 * Version 1.0.0
 *
 */
public class give extends Command {

    public give(MudCharacter mob) {
        super(mob, "give", "\"give <character> <|quantity> <item>\"",
                "This makes your character attempt give someone an item");
    }

    @Override
    public void Execute(String args) {

        if (args.isEmpty()) {
            badUsage();
            return;
        }
        String[] parms = args.split(" ");
        if (parms.length < 2) {
            badUsage();
            return;
        }

        Room r = Mob().Room();
        MudCharacter recipient = r.seekCharacter(parms[0]);
        if (recipient == null) {
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "You don't see " + parms[0] + " anywhere around.");
            return;
        }

        if (recipient.getID() == Mob().getID()) {
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "You can't give yourself an object!");
            return;
        }

        int quantity = 0;
        String item = parms[1];

        if (parms.length == 3) {
            item = parms[2];
            try {
                quantity = Integer.parseInt(parms[1]);
            } catch (Exception ex) {
                badUsage();
                return;
            }
        }
        Item i = Mob().seekItem(item);
        if (i == null) {
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "You can not give " + item + " if you don't have one!");
            return;
        }
        if (i.isQuantity() && quantity == 0) {
            quantity = i.getQuantity();
        }
        try {
            Main.game.doAction(ActionType.attemptgiveitem, Mob().getID(), recipient.getID(), i.getID(), quantity, "");
        } catch (Exception ex) {
            Logger.getLogger(give.class.getName()).log(Level.SEVERE, null, ex);
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "Failure to give item.  See SYS ADMIN.");
        }



    }
}
