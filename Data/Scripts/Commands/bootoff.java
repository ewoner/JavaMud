package Commands;

import Actions.Action;
import Actions.ActionType;
import Entities.MudCharacter;
import Mud.Mud;
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
public class bootoff extends Command {

    public bootoff(MudCharacter mob) {
        super(mob, "BootOff",
                "\"bootoff <user>\"",
                "This boots a user from the realm.");
    }

    @Override
    public void Execute(String parameters) {
        if (parameters == null) {
            badUsage();
            return;
        }// find a matching player
        MudCharacter c = Mud.dbg.characterDB.findPlayerFull(parameters);
        if (c == null) {
            Mob().DoAction(new Action(ActionType.error, 0, 0, 0, 0, "Cannot find user " + parameters));
            return;
        }
        Main.game.addActionAbsolute(0, ActionType.announce, 0, 0, 0, 0, c.getName() + " has been kicked");
        c.DoAction(new Action(ActionType.hangup));
    }
}
