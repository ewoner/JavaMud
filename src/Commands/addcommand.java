package Commands;

import Actions.Action;
import Actions.ActionType;
import Entities.MudCharacter;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 * Version 1.0.0
 *
 */
public class addcommand extends Command {

    public addcommand(MudCharacter mob) {
        super(mob, "AddCommand", "\"addcommand <player name> <command name>\"",
                "This allows an administrator to give a play a new command.");
    }

    @Override
    public void Execute(String parameters) {
        if (parameters.isEmpty()) {
            badUsage();
            return;
        } 
        String[] parms = Mud.Mud.splitString(parameters);
        if (parms.length!=2){
            badUsage(); return;
        }
        String playerName = parms[0];
        String commandName = parms[1];

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
