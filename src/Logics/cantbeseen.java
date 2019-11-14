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
public class cantbeseen extends Logic {

    public cantbeseen() {
        super("cantbeseen");

    }

    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.query) {
            return Logic.TRUE;
        } else {
            return Logic.DEFAULT;
        }
    }
}
