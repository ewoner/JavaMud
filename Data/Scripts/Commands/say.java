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
public class say extends Command {

    public say(MudCharacter mob) {
        super(mob, "Say", "\"say <message>\"",
                "This sends a message to every character in the same room as you.");
    }

    @Override
    public void Execute(String p_parameters) {
        if (p_parameters == null) {
            badUsage();
            return;
        }
        Main.game.addActionAbsolute(0, ActionType.attemptsay, Mob().getID(), 0, 0, 0, p_parameters);
    }
}
