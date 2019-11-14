package Commands;

import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;
import Logics.Logic;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 * Version 1.0.0
 *
 */
public class arm extends Command {

    public arm(MudCharacter mob) {
        super(mob, "arm", "\"arm <item>\"",
                "Attempts to arm an item");
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

        if (Mob().DoAction(ActionType.query, item.getID(), 0, 0, 0, "canarm") == Logic.FALSE) {
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "Cannot arm item: " + item.getName() + "!");
            return;
        }
        Mob().DoAction(ActionType.doaction, 0, 0, item.getID(), 0, "arm");
    }
}
