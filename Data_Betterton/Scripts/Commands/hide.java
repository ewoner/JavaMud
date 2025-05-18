package Commands;

import Actions.ActionType;
import Entities.MudCharacter;
import Mud.D_and_D_Mud.MudData;
import Server.Main;
//import utilities.Dice;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.0.0
 *
 */
public class hide extends Command {

    private int hideCheck = 0;

    public hide(MudCharacter mob) {
        super(mob, "hide", "\"hide\"",
                "This command attempts to hide the character from veiw.");
    }

    @Override
    public void Execute(String args) {
        if (Mob().getAttribute(MudData.HIDING) != 1) {
            //hideCheck = Dice.Roll_d20() + Mob().getAttribute(MudData.HIDESKILL);
            Mob().setAttribute(MudData.HIDING, 1);
            Main.game.doAction(ActionType.announce, 0, 0, 0, 0, Mob().getName() + " attempts to fade into the shadows....");
        }else {
        Mob().getAttributes().del(MudData.HIDING);
        Main.game.doAction(ActionType.announce, 0, 0, 0, 0, Mob().getName() +" is no longer trying to hide.");
        }
    }
}
