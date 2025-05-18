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
public class ne extends Command {

    public ne(MudCharacter mob) {
        super(mob, "ne", "\"ne\"",
                "This movement command lets you go northeast without having to use the go command.");
    }

    @Override
    public void Execute(String p_parameters) {
        try {
            Main.game.doAction(new Action(ActionType.command, Mob().getID(), 0, 0, 0, "/go northeast"));
        } catch (Exception ex) {
            Logger.getLogger(ne.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
