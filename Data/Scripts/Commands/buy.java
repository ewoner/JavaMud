package Commands;

import Actions.ActionType;
import Entities.MudCharacter;
import Entities.Room;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 * Version 1.0.0
 *
 */
public class buy extends Command {

    public buy(MudCharacter mob) {
        super(mob, "buy", "\"buy <merchant> <item>\"",
                "buys an item from a merchant");
    }

    @Override
    public void Execute(String args) {
        if (args.isEmpty()) {
            badUsage();
            return;
        }
        String[] parms = Mud.Mud.splitString(args);

        if (parms.length != 2) {
            badUsage();
            return;
        }
        Room r = Mob().Room();
        MudCharacter m = r.seekCharacter(parms[0]);
        m.DoAction(ActionType.doaction, 0, 0, Mob().getID(), 0, "buy " + parms[1]);
    }
}
