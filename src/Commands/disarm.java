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
public class disarm extends Command {

    public disarm(MudCharacter mob) {
        super(mob, "disarm", "\"disarm <item type>\"",
                "Attempts to disarm an item");
    }

    @Override
    public void Execute(String args) {
        if (args.isEmpty()) {
            badUsage();
            return;
        }

        if (args.equalsIgnoreCase("weapon") || (args.toLowerCase().startsWith(args.toLowerCase()) && args.length() > 2)) {
            Mob().DoAction(ActionType.doaction, 0, 0, 1, 0, "disarm");
        }
    }
}
