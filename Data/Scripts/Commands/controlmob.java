package Commands;

import Actions.ActionType;
import Entities.MudCharacter;
import Server.Main;
/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 * Version 1.0.0
 *
 */
public class controlmob extends Command {

    public controlmob(MudCharacter mob) {
        super(mob, "controlmob", "\"controlmob <mob> <text>\"",
                "This tells the game to pretend that <mob> typed in <text>.  Must be in same room.");
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

        MudCharacter c = Mob().Room().seekCharacter(parms[0]);
        if (c == null) {
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "Cannot find mob: " + parms[0]);
            return;
        }
        Main.game.addActionAbsolute(0, ActionType.command, c.getID(), 0, 0, 0, parms[1]);
    }
}
