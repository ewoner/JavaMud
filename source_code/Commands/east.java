package Commands;

import Actions.Action;
import Actions.ActionType;
import Entities.MudCharacter;
import Server.Main;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.0.0
 *
 */
public class east extends Command {

    public east(MudCharacter mob) {
        super(mob, "east", "\"east\"",
                "Attempts to move east");
    }

    @Override
    public void Execute(String parameters) {
        try {
            Main.game.doAction(new Action(ActionType.command, Mob().getID(), 0, 0, 0, "/go east"));
        } catch (Exception ex) {
            Logger.getLogger(east.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
