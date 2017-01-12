package Commands;

import Actions.Action;
import Actions.ActionType;
import Entities.MudCharacter;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.0.0
 *
 */
public class quiet extends Command {

    public quiet(MudCharacter mob) {
        super(mob, "Quiet",
                "\"quiet <on|off>\"",
                "Sets your quiet mode. When not quiet, unrecognized commands will be said as room-speech.");
        loadable = false;
    }

    @Override
    public void Execute(String p_parameters) {
        if (p_parameters.equals("on")) {
            Mob().setQuiet(true);
            Mob().DoAction(new Action(ActionType.announce, 0, 0, 0, 0, "You are now in QUIET mode"));
        } else if (p_parameters.equals("off")) {
            Mob().setQuiet(false);
            Mob().DoAction(new Action(ActionType.announce, 0, 0, 0, 0, "You are now in LOUD mode"));
        } else {
            badUsage();
        }
    }
}
