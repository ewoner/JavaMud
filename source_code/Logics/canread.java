package Logics;

import Actions.Action;
import Actions.ActionType;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.0.0
 *
 */
public class canread extends Logic {

    public canread() {
        super("canread");
    }

    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.query && action.getData().equals("canread")) {
            return TRUE;
        }
        return FALSE;
    }
}
