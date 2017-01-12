package Commands;

import Actions.Action;
import Actions.ActionType;
import Entities.MudCharacter;
import Server.Main;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This command is used to go up through exits.
 *
 * @author Brion Lang
 * Date: 17 Jan 2009
 *
 * Version 1.0.0
 */
public class up extends Command {

    public up(MudCharacter mob) {
        super(mob, "up", "\"up\"",
                "This movement command lets you go up without having to use the go command.");
    }

    @Override
    public void Execute(String p_parameters) {
        try {
            Main.game.doAction(new Action(ActionType.command, Mob().getID(), 0, 0, 0, "/go up"));
        } catch (Exception ex) {
            Logger.getLogger(up.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
