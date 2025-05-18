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

/**
 *
 * @author Administrator
 */
public class uberweight extends Logic {

    private Item me;

    public uberweight() {
        super("uberweight");
    }

    /**
     * Must always have this method
     *
     * @param action
     * @return results of the action
     */
    @Override
    public int DoAction(Action action) {
        me = (Item) me();
        if (action.getType() == ActionType.cangetitem) {
            MudCharacter c = dbg.characterDB.get(action.arg1);
            Main.game.addActionAbsolute(0, ActionType.vision, c.getRoom(), 0, 0, 0, c.getName() + " struggles like a madman trying to pull " + me().getName() + " off the ground, but it's stuck!");
            return NO;
        }
        if (action.getType() == ActionType.messagelogic) {
            if (action.getData().equals("uberweight remove")) {
                Main.game.addActionAbsolute(0, ActionType.dellogic, 1, me().getID(), 0, 0, "uberweight");

                Main.game.addActionAbsolute(0, ActionType.vision, me.getRoom(), 0, 0, 0, "The uberweight on " + me.getName() + " wears off!");
            }
        }
        return DEFAULT;
    }
}
