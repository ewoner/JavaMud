package Commands;

import Actions.ActionType;
import Entities.MudCharacter;
import Entities.Room;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.0.0
 *
 */
public class list extends Command {

    public list(MudCharacter mob) {
        super(mob, "list", "\"list <merchant>\"",
                "Gets a list of the merchants wares");
    }

    @Override
    public void Execute(String args) {
        if (args.isEmpty()) {
            badUsage();
            return;
        }

        Room r = Mob().Room();
        MudCharacter m = r.seekCharacter(args);

        if (m != null) {
            m.DoAction(ActionType.doaction, 0, 0, Mob().getID(), 0, "list");
        } else {
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "No merchant by the name of" + args + " was found.");
        }


    }
}
