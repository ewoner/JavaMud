package Commands;

import Actions.ActionType;
import Entities.MudCharacter;
import Entities.Room;
import Server.Main;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 * Versuib 1.0.1
 * 1.  The usage has been changed to read character instead of item.
 * Version 1.0.0
 *
 */
public class destroycharacter extends Command {

    public destroycharacter(MudCharacter mob) {
        super(mob, "destroycharacter", "\"destroycharacter <character>\"",
                "This destroys a character.");
    }

    @Override
    public void Execute(String args) {
        if (args.isEmpty()) {
            badUsage();
            return;
        }
        Room room = Mob().Room();
        MudCharacter character = room.seekCharacter(args);
        if (character == null) {
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "Cannot find character: " + args);
            return;
        }
        Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Destroying Character: " + character.getName());
        Main.game.addActionAbsolute(0, ActionType.destroycharacter, character.getID(), 0, 0, 0, "");

    }
}
