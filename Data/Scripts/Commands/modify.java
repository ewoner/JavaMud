package Commands;

import Actions.ActionType;
import Entities.MudCharacter;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.0.0
 *
 */
public class modify extends Command {

    public modify(MudCharacter mob) {
        super(mob, "modify", "\"modify\"",
                "Enters into modification mode.");
    }

    @Override
    public void Execute(String args) {
        if (args.isEmpty()) {
            badUsage();
            return;
        }
        Mob().addLogic("modify");
        Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Entering into modify mode....");


    }
}
