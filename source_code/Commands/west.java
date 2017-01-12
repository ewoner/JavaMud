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
public class west extends Command {

    public west(MudCharacter mob) {
        super(mob, "west", "\"west\"",
                "This movement command lets you go west without having to use the go command.");
    }

    @Override
    public void Execute(String p_parameters) {
        try {
            Main.game.doAction(new Action(ActionType.command, Mob().getID(), 0, 0, 0, "/go west"));
        } catch (Exception ex) {
            Logger.getLogger(west.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
