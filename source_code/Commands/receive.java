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
public class receive extends Command {

    public receive(MudCharacter mob) {
        super(mob, "receive", "\"receive <on|off>\"",
                "Turns your item receiving mode on or off");
    }

    @Override
    public void Execute(String args) {
        if (args.isEmpty()) {
            badUsage();
            return;
        }

        if (!args.equalsIgnoreCase("on") && !args.equalsIgnoreCase("off")) {
            badUsage();
            return;
        }
        if (args.equalsIgnoreCase("on")) {
            if (Mob().hasLogic("cantreceiveitems")) {
                Mob().delLogic("cantreceiveitems");
                Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Receiving mode is now ON");
            } else {
                Mob().DoAction(ActionType.error, 0, 0, 0, 0, "You are already in receiving mode!");
            }
        } else {
            if (!Mob().hasLogic("cantreceiveitems")) {
                Mob().addLogic("cantreceiveitems");
                Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Receiving mode is now OFF");
            } else {
                Mob().DoAction(ActionType.error, 0, 0, 0, 0, "You are already in non-receiving mode!");
            }
        }
    }
}
