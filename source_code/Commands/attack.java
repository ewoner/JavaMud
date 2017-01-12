package Commands;

import Actions.ActionType;
import Entities.MudCharacter;
import Entities.Room;
import Logics.Logic;
/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 * Version 1.0.0
 *
 */
public class attack extends Command {

    public attack(MudCharacter mob) {
        super(mob, "attack", "\"attack <character>\"",
                "Initiates attack mode on a character");
    }

    @Override
    public void Execute(String args) {
        if (args.isEmpty()) {
            badUsage();
            return;
        }

        Room r = Mob().Room();
        MudCharacter target = r.seekCharacter(args);
        if (target == null) {
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "Can not find target: \"" + args + "\" in this room.");
            return;
        }

        if (target.DoAction(ActionType.query, Mob().getID(), 0, 0, 0, "canattack") == Logic.FALSE) {
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "\"" + target.getName() + "\" can not be attacked.");
            return;
        }

        Mob().DoAction(ActionType.doaction, 0, 0, target.getID(), 0, "initattack");


    }
}
