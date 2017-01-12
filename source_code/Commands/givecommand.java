package Commands;

import Actions.Action;
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
public class givecommand extends Command {

    public givecommand(MudCharacter mob) {
        super(mob, "GiveCommand", "\"givecommand <player name>/<command name>\"",
                "This allows an administrator to give a play a new command.");
    }

    @Override
    public void Execute(String parameters) {
        if (parameters == null || parameters.equals("")) {
            badUsage();
            return;
        } else if (!parameters.contains(" ")) {
            badUsage();
            return;
        }
        String playerName = parameters.substring(0, parameters.indexOf(" "));
        String commandName = parameters.substring(parameters.indexOf(' ') + 1);

        boolean worked = false;
        MudCharacter player = Mud.Mud.dbg.characterDB.findPlayerPart(playerName);
        if (player == null) {
            Mob().DoAction(new Action(ActionType.error, 0, 0, 0, 0, "Usage: " + getUsage() + "  Player not found."));
        } else {
            worked = player.addCommand(commandName);
            if (!worked && player.hasCommand(commandName)) {
                Mob().DoAction(new Action(ActionType.error, 0, 0, 0, 0, "Player already has that command."));
                return;
            }
        }
        if (worked) {
            Mob().DoAction(new Action(ActionType.announce, 0, 0, 0, 0, player.getName() + " has been give the command: " + commandName + "."));
        } else {
            Mob().DoAction(new Action(ActionType.error, 0, 0, 0, 0, "Usage: " + getUsage() + "  Command not found."));
        }
    }
}
