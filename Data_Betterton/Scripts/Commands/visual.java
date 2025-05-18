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
public class visual extends Command {

    public visual(MudCharacter mob) {
        super(mob, "visual", "\"visual <vision>\"",
                "shows some text in a room");
    }

    @Override
    public void Execute(String args) {
        if (args.isEmpty()) {
            badUsage();
            return;
        }
        Main.game.addActionAbsolute(0, ActionType.vision, Mob().getRoom(), 0, 0, 0, args);
    }
}
