/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logics;

import Actions.Action;
import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;
import Server.Main;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class pies extends Logic {

    public pies() {
        super("pies");

    }

    /**
     * Must always have this method
     *
     * @param action
     * @return results of the action.
     */
    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.say && action.getData().contains("pies") && action.arg1 != me().getID()) {
            MudCharacter c = dbg.characterDB.get(action.arg1);
            Main.game.addActionAbsolute(0, ActionType.attemptsay, me().getID(), 0, 0, 0, c.getName() + ": YES!!! PIES!!!!!");
        }


        return DEFAULT;
    }
}
