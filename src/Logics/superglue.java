/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logics;

import Actions.Action;
import Actions.ActionType;
import Entities.MudCharacter;
import Server.Main;

/**
 *
 * @author Administrator
 */
public class superglue extends Logic {
    private MudCharacter me;

    public superglue() {
        super("superglue");

    }

    /**
     * Must always have this method
     *
     * @param action
     * @return results of the action
     */
    @Override
    public int DoAction(Action action) {
        me = (MudCharacter) me();
        if (action.getType() == ActionType.canleaveroom) {
            me.DoAction(ActionType.error, 0, 0, 0, 0, "You're superglued to the ground. Sit tight, you ain't goin nowhere.");
            return NO;
        }
        if (action.getType() == ActionType.messagelogic) {
            if (action.getData().equalsIgnoreCase("superglue remove")) {
                Main.game.addActionAbsolute(0, ActionType.dellogic, 0, me.getID(), 0, 0, "superglue");
                Main.game.addActionAbsolute(0, ActionType.vision, me.getRoom(), 0, 0, 0, "The superglue on " + me.getName() + " wears off!");
            }
        }
        return DEFAULT;
    }
}
