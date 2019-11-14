package Commands;

import Actions.ActionType;
import Entities.MudCharacter;
import Entities.Room;
import Server.Main;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.0.0
 *
 */
public class pies extends Command {

    public pies(MudCharacter mob) {
        super(mob, "pies", "\"pies <|character>\"",
                "Silly action");
    }

    @Override
    public void Execute(String args) {
        String name;
        Room r = Mob().Room();
        if (args.isEmpty()) {
            name = Mob().getName();
        } else {
            MudCharacter c = r.seekCharacter(args);
            if (c == null) {
                Mob().DoAction(ActionType.error, 0, 0, 0, 0, "Cannot find character: " + args);
                return;
            }
            name = c.getName();
        }

        Main.game.addActionAbsolute(0, ActionType.vision, r.getID(), 0, 0, 0, "<#00FF00>OMG!!! " + Mob().getName() + " just threw a huge <#FFFF00>CUSTARD PIE<#00FF00> at " + name + "!!!!");
    }
}
