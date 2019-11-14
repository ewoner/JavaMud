/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logics;

import Actions.Action;
import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;

/**
 *
 * @author Administrator
 */
public class usableitem extends Logic {

    public usableitem() {
        super("usableitem");
    }

    public usableitem(String name) {
        super(name);
    }

    public void doUse(MudCharacter c, Item i) {
    }

    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.query && action.getData().equals("canuse")) {
            return TRUE;
        }
        if (action.getType() == ActionType.doaction && action.getData().equalsIgnoreCase("use")) {
            doUse(dbg.characterDB.get(action.arg3), (Item) me());
            return DEFAULT;
        }
        return DEFAULT;
    }
}
