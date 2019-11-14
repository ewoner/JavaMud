package Logics;

import Actions.Action;
import Actions.ActionType;
import Entities.Item;
import Entities.Room;
import Server.Main;

/**
 * This logic module will cause a new pie to be "baked" in the room that a pie
 * is located.  Only one pie will be backed at a time.
 *
 * @author Brion Lang
 *  Date: 08 Dec 2009
 *
 * Version 1.1.0
 * 1.  Added support to only allow one pie to be baked at a time.
 * Version 1.0.0
 */
public class pieroom extends Logic {

    private boolean baking = false;

    public pieroom() {
        super("pieroom");

    }

    /**
     * Must always have this method
     *
     * @param action
     * @return results of the action
     */
    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.getitem) {
            Item item = dbg.itemDB.get(action.arg2);
            Room room = dbg.characterDB.get(item.getRoom()).Room();
            if (item.getTemplateID() == 51 && baking == false && room.seekItem("pie") == null) {
                baking = true;
                Main.game.addActionRelative(5000, ActionType.vision, me().getID(), 0, 0, 0, "The Baker puts a new pie into the oven!");
                Main.game.addActionRelative(120000, ActionType.spawnitem, 51, me().getID(), 0, 0, "");
                Main.game.addActionRelative(120001, ActionType.vision, me().getID(), 0, 0, 0, "A freshly baked Pie pops out of the oven!");
            }
        }
        if (action.getType() == ActionType.spawnitem) {
            Item item = dbg.itemDB.get(action.arg1);
            if (item != null) {
                if (item.getTemplateID() == 51) {
                    baking = false;
                }
            }
        }

        return DEFAULT;
    }
}
