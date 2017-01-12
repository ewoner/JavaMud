package Commands;

import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;
import Logics.Logic;

/**
 * This command is use to wear items like armor.  Items must have the "wearable"
 * logic and the attribute "worn 1".
 *
 * @author Brion Lang
 * Date: 8  Dec 2009
 *
 * Version 1.0.0
 */
public class wear extends Command {

    public wear(MudCharacter mob) {
        super(mob, "wear", "\"wear <item>\"",
                "Attempts to wear an item (ie armor and not a weapon)");
    }

    @Override
    public void Execute(String args) {
        if (args.isEmpty()) {
            badUsage();
            return;
        }

        Item item = Mob().seekItem(args);
        if (item == null) {
            badUsage();
            return;
        }

        if (Mob().DoAction(ActionType.query, item.getID(), 0, 0, 0, "canwear") == Logic.FALSE) {
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "Cannot wear item: " + item.getName() + "!");
            return;
        }
        Mob().DoAction(ActionType.doaction, 0, 0, item.getID(), 0, "wear");
    }
}
