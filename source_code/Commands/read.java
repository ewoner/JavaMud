package Commands;

import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;
import Logics.Logic;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.0.0
 *
 */
public class read extends Command {

    public read(MudCharacter mob) {
        super(mob, "read", "\"read <item>\"",
                "Tries to read an item");
    }

    @Override
    public void Execute(String parameters) {
        if (parameters.isEmpty()) {
            this.badUsage();
        }
        Item item;
        try {
            item = Mob().seekItem(parameters);
        } catch (Exception e) {
            item = Mob().seekItem(parameters);
        }
        if (item == null) {
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "Cannot find the item " + parameters + "!");
            return;
        }
        if (item.DoAction(ActionType.query, 0, 0, 0, 0, "canread") == Logic.FALSE) {
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "Cannot read " + item.getName() + "!");
            return;
        }

        item.DoAction(ActionType.doaction, 0, 0, Mob().getID(), 0, "read");
    }
}
