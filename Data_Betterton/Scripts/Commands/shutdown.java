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
public class shutdown extends Command {

    public shutdown(MudCharacter mob) {
        super(mob,
                "Shutdown",
                "\"shutdown\"",
                "Shuts down the MUD");
    }

    @Override
    public void Execute(String p_parameters) {
        try {
            if (!p_parameters.isEmpty()) {
                Main.game.doAction(ActionType.announce, 0, 0, 0, 0, "The Server is shutting down: " + p_parameters);
            } else {
                Main.game.doAction(ActionType.announce, 0, 0, 0, 0, "The Server is shutting down");
            }
            Main.game.doAction(ActionType.shutdown, Mob().getID(), 0, 0, 0, null);
            Main.game.doAction(ActionType.savedatabases, 0, 0, 0, 0, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
