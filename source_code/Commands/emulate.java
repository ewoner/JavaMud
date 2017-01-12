package Commands;

import Actions.ActionType;
import Entities.MudCharacter;
import Server.Main;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.0.0
 *
 */
public class emulate extends Command {

    public emulate(MudCharacter mob) {
        super(mob, "emulate", "\"emulate <player> <text>\"",
                "This tells the game to pretend that <player> typed in <text>");
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

        MudCharacter c = Main.game.findPlayerOnlinePart(parms[0]);
        if (c == null) {
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "Cannot find player: " + parms[0]);
            return;
        }
        Main.game.addActionAbsolute(0, ActionType.command, c.getID(), 0, 0, 0, parms[1]);
    }
}
