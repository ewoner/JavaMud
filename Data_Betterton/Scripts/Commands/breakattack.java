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
public class breakattack extends Command {

    public breakattack(MudCharacter mob) {
        super(mob, "breakattack", "\"breakattack\"",
                "Stops attacking your target");
    }

    @Override
    public void Execute(String args) {
        Mob().DoAction(ActionType.doaction, 0, 0, 0, 0, "breakattack");
    }
}
