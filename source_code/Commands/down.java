package Commands;

import Actions.Action;
import Actions.ActionType;
import Entities.MudCharacter;
import Server.Main;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Command to go "down" as an exit.
 *
 * @author Brion Lang
 * Date: 17 Jan 2009
 * Version 1.0.1
 * 1.  Changed the go command from "\go below" to "\go down"
 * Version 1.0.0
 */
public class down extends Command {

    public down(MudCharacter mob) {
        super(mob, "down", "\"down\"",
                "This movement command lets you go down without having to use the go command.");
    }

    @Override
    public void Execute(String parameters) {
        try {
            Main.game.doAction(new Action(ActionType.command, Mob().getID(), 0, 0, 0, "/go down"));
        } catch (Exception ex) {
            Logger.getLogger(down.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
