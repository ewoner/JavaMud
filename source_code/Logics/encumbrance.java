package Logics;

import Actions.Action;
import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;

/**
 * This logic adds encumbrance to the mob.  Maxium weight carried is added to
 * the mob's attributes as is the current weight.  This logic then tracks
 * encumbrance not allowing a mob to get an item that will overload its weight
 * limit.
 *
 * @author Brion Lang
 *  Date: 08 Dec 2009
 *
 *  Version 1.0.0
 */
public class encumbrance extends Logic {

    public encumbrance() {
        super("encumbrance");
    }

    /**
     * Must always have this method
     *
     * @param action
     * @return 0 = succuess.  other numbers very by logic.
     */
    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.canleaveroom) {
            if (me().getAttribute("encumbrance") > me().getAttribute("maxencumbrance")) {
                me().DoAction(ActionType.error, 0, 0, 0, 0, "You cannot move! You're too heavy! Drop something first!");
                return NO;
            }
            return YES;
        }
        if (action.getType() == ActionType.getitem) {
            if (action.arg1 == me().getID()) {
                Item item = dbg.itemDB.get(action.arg2);
                int weight = weight(item, action.arg3);
                me().setAttribute("encumbrance", me().getAttribute("encumbrance") + weight);
            }
            return DEFAULT;
        }
        if (action.getType() == ActionType.dropitem) {
            if (action.arg1 == me().getID()) {
                Item item = dbg.itemDB.get(action.arg2);
                int weight = weight(item, action.arg3);
                me().setAttribute("encumbrance", me().getAttribute("encumbrance") - weight);
            }
            return DEFAULT;
        }
        if (action.getType() == ActionType.destroyitem) {
            Item item = dbg.itemDB.get(action.arg1);
            int weight = weight(item, item.getQuantity());
            me().setAttribute("encumbrance", me().getAttribute("encumbrance") - weight);
            return DEFAULT;
        }
        if (action.getType() == ActionType.giveitem) {
            if (action.arg1 == me().getID()) {
                Item item = dbg.itemDB.get(action.arg3);
                int weight = weight(item, action.arg4);
                me().setAttribute("encumbrance", me().getAttribute("encumbrance") - weight);
            }
            if (action.arg2 == me().getID()) {
                Item item = dbg.itemDB.get(action.arg3);
                int weight = weight(item, action.arg4);
                me().setAttribute("encumbrance", me().getAttribute("encumbrance") + weight);
            }
            return DEFAULT;
        }
        if (action.getType() == ActionType.spawnitem) {
            Item item = dbg.itemDB.get(action.arg1);
            int weight = weight(item, item.getQuantity());
            me().setAttribute("encumbrance", me().getAttribute("encumbrance") + weight);
            return DEFAULT;
        }
        if (action.getType() == ActionType.cangetitem) {
            Item item = dbg.itemDB.get(action.arg2);
            int weight = weight(item, action.arg3);
            if (weight + me().getAttribute("encumbrance") > me().getAttribute("maxencumbrance")) {
                me().DoAction(ActionType.error, 0, 0, 0, 0, "You can't pick up " + item.getName() + " because it's too heavy for you to carry!");
                return NO;
            }
            return YES;
        }
        if (action.getType() == ActionType.canreceiveitem) {
            MudCharacter g = dbg.characterDB.get(action.arg1);
            Item item = dbg.itemDB.get(action.arg3);
            int weight = weight(item, action.arg4);
            if (weight + me().getAttribute("encumbrance") > me().getAttribute("maxencumbrance")) {
                me().DoAction(ActionType.error, 0, 0, 0, 0, g.getName() + " tried to give you " + item.getName() + " but it's too heavy for you to carry!");
                g.DoAction(ActionType.error, 0, 0, 0, 0, "You can't give " + me().getName() + " the " + item.getName() + " because it is too heavy!");
                return NO;
            }
            return YES;
        }
        return DEFAULT;
    }

    private int weight(Item item, int quantity) {
        if (item.isQuantity()) {
            return quantity * item.getAttribute("weight");
        } else {
            return item.getAttribute("weight");
        }

    }
}
