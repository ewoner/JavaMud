package Logics;

import Actions.Action;
import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.0.1
 * 1.  Removed the variable "me" and changed the support to the inherited "me()"
 * method.
 * Version 1.0.0
 *
 */
public class cantreceiveitems extends Logic {

    public cantreceiveitems() {
        super("cantreceiveitems");
    }

    /**
     * Must always have this method
     *
     * @param action
     * @return results of the action
     */
    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.canreceiveitem) {
            MudCharacter g = Mud.Mud.dbg.characterDB.get(action.arg1);
            if (!g.isPlayer()) {
                return YES;
            }
            Item i = Mud.Mud.dbg.itemDB.get(action.arg3);
            me().DoAction(ActionType.error, 0, 0, 0, 0, g.getName() + " tried to give you " + i.getName() + " but you have item receiving turned off. Type \"/receive on\" to turn receiving back on.");
            g.DoAction(ActionType.error, 0, 0, 0, 0, me().getName() + " refuses to take " + i.getName() + "!");
            return NO;
        }
        return DEFAULT;
    }
}
