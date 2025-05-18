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
public class uberweight extends Command{

    public uberweight(MudCharacter mob) {
        super(mob, "uberweight", "\"uberweight\"",
                "Casts the uberweight spell.");
    }@Override
    public void Execute(String parameters) {
        Mob().DoAction(new Action(ActionType.announce,"You cast Uberweight."));
    }

}
