package Commands;

import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;
import Mud.Mud;

/**
 * Command used remove a worn item from the player.  The item must have the
 * "wearable" logic and the attribute "worn 1".
 *
 * @author Brion Lang
 * Date: 17 Jan 2009
 *
 * Version 1.0.0
 */
public class remove extends Command {

    public remove(MudCharacter mob) {
        super(mob, "remove", "\"remove <item type>\"",
                "Attempts to remove an item that you are wearing (ie. armor and not a weapon)");
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
        if (item.getAttribute("shield") == 1) {
            Mob().DoAction(ActionType.doaction, 0, 0, 2, 0, "unwear");
        } else if (item.getAttribute("armor") == 1) {
            Mob().DoAction(ActionType.doaction, 0, 0, 1, 0, "unwear");
        } else if (item.getAttribute("weapon") == 1) {
            Mob().DoAction(ActionType.doaction, 0, 0, 3, 0, "unwear");
        }
    }
}
