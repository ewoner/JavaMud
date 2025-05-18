package Logics;

import Actions.Action;
import Actions.ActionType;
import Entities.MudCharacter;
import Server.Main;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.0.0
 *
 */
public class cantget extends Logic {

    public cantget() {
        super("cantget");

    }

    /**
     * Must always have this method
     *
     * @param action
     * @return results of the action
     */
    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.cangetitem) {
            MudCharacter c = Mud.Mud.dbg.characterDB.get(action.arg1);
            try {
                Main.game.doAction(new Action(ActionType.vision, c.getRoom(), 0, 0, 0, c.getName() + " almost has a hernia, trying to pull " + me().getName() + " out of the ground!"));
            } catch (Exception ex) {
                Logger.getLogger(cantget.class.getName()).log(Level.SEVERE, null, ex);
            }
            return NO;
        }
        return YES;
    }
}
