package Commands;

import Actions.Action;
import Actions.ActionType;
import Entities.MudCharacter;
import Entities.Portal;
import Entities.Room;
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
public class go extends Command {

    public go(MudCharacter mob) {
        super(mob, "Go",
                "\"go <exit>\"",
                "Tries to move your character into a portal");
    }

    @Override
    public void Execute(String p_parameters) {
        int i = 1;
        if (p_parameters.isEmpty()) {
            Mob().DoAction(new Action(ActionType.error, 0, 0, 0, 0, "Usage: " + getUsage()));
            return;
        }

        Room r = Mob().Room();
        Portal p = r.seekPortal(p_parameters);
        if (p == null) {
            Mob().DoAction(new Action(ActionType.error, 0, 0, 0, 0, "You don't see that exit here!"));
            return;
        }

        Main.game.addActionAbsolute(0l, new Action(ActionType.attemptenterportal, Mob().getID(), p.getID()));

    }
}
