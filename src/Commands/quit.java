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
public class quit extends Command {

    public quit(MudCharacter mob) {
        super(mob, "Quit", "\"quit\"",
                "This command removers you from the game and takes you back to the main menu.");
        loadable = false;
    }

    @Override
    public void Execute(String p_parameters) {
        Mob().DoAction(new Action(ActionType.leave, Mob().getID()));

    }
}
