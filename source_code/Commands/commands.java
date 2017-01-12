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
public class commands extends Command {

    public commands(MudCharacter mob) {
        super(mob,
                "Commands",
                "\"commands\"",
                "Lists your commands");
    }

    @Override
    public void Execute(String parameters) {
        Mob().DoAction(new Action(ActionType.announce, 0, 0, 0, 0,
                "<#FFFFFF>-------------------------------------------------------------------------------"));
        Mob().DoAction(new Action(ActionType.announce, 0, 0, 0, 0,
                "<#FFFFFF> Command                          | Usage"));
        Mob().DoAction(new Action(ActionType.announce, 0, 0, 0, 0,
                "<#FFFFFF>-------------------------------------------------------------------------------"));

        for (Command c : Mob().getCommands()) {
            Mob().DoAction(new Action(ActionType.announce, 0, 0, 0, 0,
                    "<$reset> " + c.getName() + "  |  " + c.getUsage()));
        }
    }
}
