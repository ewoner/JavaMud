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
public class announce extends Command {

    public announce(MudCharacter mob) {
        super(mob, "announce", "\"announce <announcement>\"",
                "This shows a system-wide announcement");
    }

    @Override
    public void Execute(String parameters) {
        if (parameters.isEmpty()) {
            badUsage();
            return;
        }
        Main.game.addActionAbsolute(0, ActionType.announce, 0, 0, 0, 0, "Announcement by " + Mob().getName() + ": " + parameters);
    }
}
