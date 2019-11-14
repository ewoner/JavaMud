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
public class addplayerlogic extends Command {

    public addplayerlogic(MudCharacter mob) {
        super(mob, "addplayerlogic", "\"addplayerlogic <player> <logicname>\"",
                "This adds a logic module to a player.");
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
        if (!c.addLogic(parms[1])) {
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "Could not add logic: " + parms[1]);
            return;
        }
        Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Successfully gave " + c.getName() + " logic module " + parms[1] + ".");
        c.DoAction(ActionType.announce, 0, 0, 0, 0, Mob().getName() + " gave you a new logic module: " + parms[1] + "!");
    }
}
