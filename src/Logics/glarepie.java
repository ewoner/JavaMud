package Logics;

import Actions.Action;
import Actions.ActionType;
import Entities.Item;
import Server.Main;

/**
 * This logic causes a mob to say a line of text if a pie is picked up in the
 * room that the mob with this logic is in.
 *
 * @author Brion Lang
 *  Date: 08 Dec 2009
 *
 * Version 1.0.1
 * 1.  Changed the the templateId that was check to the new pie templateID 
 * number.
 * Version 1.0.0
 */
public class glarepie extends Logic {

    public glarepie() {
        super("glarepie");

    }

    /**
     * Must always have this method
     *
     * @param action
     * @return reults of the action
     */
    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.getitem) {
            Item item = dbg.itemDB.get(action.arg2);
            if (item.getTemplateID() == 51) {
                Main.game.addActionAbsolute(0, ActionType.attemptsay, me().getID(), 0, 0, 0, "Hey!!!! Thos-a Pies aren't-a FREE!");
            }
        }
        return DEFAULT;
    }
}
