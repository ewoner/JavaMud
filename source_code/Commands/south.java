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
public class south extends Command {

    public south(MudCharacter mob) {
        super(mob, "south", "\"south\"",
                "This movement command lets you go south without having to use the go command.");
    }

    @Override
    public void Execute(String p_parameters) {
        try {
            Main.game.doAction(new Action(ActionType.command, Mob().getID(), 0, 0, 0, "/go south"));
        } catch (Exception ex) {
            Logger.getLogger(south.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
