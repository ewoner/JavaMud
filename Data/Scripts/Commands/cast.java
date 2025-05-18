package Commands;

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
public class cast extends Command {

    public cast(MudCharacter mob) {
        super(mob, "cast", "cast <spell name> <|target>",
                "Casts a spell.");
    }

    @Override
    public void Execute(String args) {
        if (args.isEmpty()) {
            badUsage();
            return;
        }
        String[] parms = Mud.Mud.splitString(args);
        String parametes = "";
        for (int i = 1; i < parms.length; i++) {
            parametes += "\"" + parms[i] + "\"";
        }
        Mob().DoAction(ActionType.spell, Mob().getID(), 0, 0, 0, parametes);
        Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Not yet implemented.");
    }
}
