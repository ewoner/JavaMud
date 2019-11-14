package Commands;

import Actions.ActionType;
import Entities.MudCharacter;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 * Version 1.0.0
 *
 */
public class delcommand extends Command {

    public delcommand(MudCharacter mob) {
        super(mob, "delcommand", "\"delcommand <player> <commandname>\"",
                "This removes a command from a character.");
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
        MudCharacter t = Mud.Mud.dbg.characterDB.findPlayerPart(parms[0]);
        if (t == null) {
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "Cannot find player: " + parms[0]);
            return;
        }
        if (!t.delCommand(parms[1])) {
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "Could not delete command: " + parms[1]);
            return;
        }
        Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Successfully removed " + t.getName() + "s command " + parms[1] + ".");
        t.DoAction(ActionType.announce, 0, 0, 0, 0, Mob().getName() + " deleted your command: " + parms[1] + "!");
    }
}
