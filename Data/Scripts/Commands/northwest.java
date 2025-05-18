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
public class northwest extends Command {

    public northwest(MudCharacter mob) {
        super(mob, "northwest", "\"northwest\"",
                "This movement command lets you go northwest without having to use the go command.");
    }

    @Override
    public void Execute(String p_parameters) {
        try {
            Main.game.doAction(new Action(ActionType.command, Mob().getID(), 0, 0, 0, "/go northwest"));
        } catch (Exception ex) {
            Logger.getLogger(northwest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
