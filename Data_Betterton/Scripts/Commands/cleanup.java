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
public class cleanup extends Command {

    public cleanup(MudCharacter mob) {
        super(mob, "cleanup", "\"cleanup\"",
                "Performs a manual database cleanup");
    }

    @Override
    public void Execute(String parameters) {
        Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Beginning Cleanup");
        Main.game.addActionAbsolute(0, ActionType.cleanup, 0, 0, 0, 0, "");

    }
}
