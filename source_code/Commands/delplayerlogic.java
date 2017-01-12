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
public class delplayerlogic extends Command {

    public delplayerlogic(MudCharacter mob) {
        super(mob, "delplayerlogic", "\"delplayerlogic <player> <logicname>\"",
                "This removes a logic module from a player");
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
        MudCharacter c = Mud.Mud.dbg.characterDB.findPlayerPart(parms[0]);
        if (c == null) {
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "Cannot find player: " + parms[0]);
            return;
        }
        if (!c.delLogic(parms[1])) {
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "Could not delete logic: " + parms[1]);
            return;
        }
        Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Successfully removed " + c.getName() + "s logic module " + parms[1] + ".");
        c.DoAction(ActionType.announce, 0, 0, 0, 0, Mob().getName() + " deleted your logic module: " + parms[1] + "!");
    }
}
