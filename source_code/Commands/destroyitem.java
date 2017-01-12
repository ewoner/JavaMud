package Commands;

import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;
import Server.Main;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 * Version 1.0.1
 * 1.  Added a call to Mud.Mud.splitString() to get arguments in case they were any " marks.
 * Version 1.0.0
 */
public class destroyitem extends Command {

    public destroyitem(MudCharacter mob) {
        super(mob, "destroyitem", "\"destroyitem <item>\"",
                "This destroys an item, searching your inventory first, then the room.");
    }

    @Override
    public void Execute(String args) {
        if (args.isEmpty()) {
            badUsage();
            return;
        }
        args = Mud.Mud.splitString(args)[0];
        Item item = Mob().seekItem(args);
        if (item == null) {
            item = Mob().Room().seekItem(args);
        }
        if (item == null) {
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "Cannot find item: " + args);
            return;
        }
        Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Destroying Item: " + item.getName());
        Main.game.addActionAbsolute(0, ActionType.destroyitem, item.getID(), 0, 0, 0, "");

    }
}
