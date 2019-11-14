package Logics;

import Actions.Action;
import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;
import Server.Main;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.1.0
 * 1.  Adding support for the "outfitting" command.  Removed some of the checks
 * as they have been moved over to "wearables". (ie dropitem and destroyitem).
 * Version 1.0.0
 *
 */
public class armaments extends Logic {

    private MudCharacter me;

    public armaments() {
        super("armaments");
    }

    /**
     * Must always have this method
     *
     * @param action
     * @return resaults of trying to do Action
     */
    @Override
    public int DoAction(Action action) {
        me = (MudCharacter) me();
        if (action.getType() == ActionType.query && action.getData().equals("canarm")) {
            Item item = Mud.Mud.dbg.itemDB.get(action.arg1);
            if (item.getAttribute("arms") == 1) {
                return TRUE;
            }
            return FALSE;
        }
        if (action.getType() == ActionType.doaction && action.getData().equals("arm")) {
            Item item = Mud.Mud.dbg.itemDB.get(action.arg3);
            Disarm(1);
            Arm(item);
        }
        if (action.getType() == ActionType.doaction && action.getData().equals("disarm")) {
            Disarm(action.arg3);
        }
//        if (action.getType() == ActionType.dropitem && action.arg1 == me().getID()) {
//            Lose(action.arg2);
//        }
//        if (action.getType() == ActionType.giveitem && action.arg1 == me().getID()) {
//            Lose(action.arg3);
//        }
//        if (action.getType() == ActionType.destroyitem) {
//            Lose(action.arg1);
//        }
        return DEFAULT;
    }

    void Disarm(int itemtype) {
        if (itemtype == 1) {
            if (me.getAttribute("weapon") != 0) {
                Item weapon = Mud.Mud.dbg.itemDB.get(me().getAttribute("weapon"));
                me.setAttribute("weapon", 0);
                if (me.DoAction(ActionType.query, 0, 0, 0, 0, "cantbeseen") == Logic.FALSE) {
                    Main.game.addActionAbsolute(0, ActionType.vision, me.getRoom(), 0, 0, 0, me.getName() + " disarms " + weapon.getName() + ".");
                } else {
                    me.DoAction(ActionType.announce, me.getRoom(), 0, 0, 0, me.getName() + " disarms " + weapon.getName() + "!");
                }
            }
        }
    }

    private void Arm(Item item) {
        if (item.getAttribute("arms") == 1) {
            me.setAttribute("weapon", item.getID());
            if (me.DoAction(ActionType.query, 0, 0, 0, 0, "cantbeseen") == Logic.FALSE) {
                Main.game.addActionAbsolute(0, ActionType.vision, me.getRoom(), 0, 0, 0, me.getName() + " arms " + item.getName() + "!");
            } else {
                me.DoAction(ActionType.announce, me.getRoom(), 0, 0, 0, me.getName() + " arms " + item.getName() + "!");
            }
        }
    }

    private void Lose(int itemid) {
        if (me.getAttribute("weapon") == itemid) {
            Disarm(1);
        }
    }
}
