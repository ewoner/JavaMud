package Logics;

import Actions.Action;
import Actions.ActionType;
import Mud.D_and_D_Mud.MudData;

/**
 * The basic logic that makes your race a "human".  Addes all the attributes to
 * Mob attributes to include modifing the Mob's ability scores.
 *
 * @author Brion Lang
 *  Date: 08 Dec 2009
 *
 *  Version 1.0.0
 */
public class human3rd extends Logic {

    public human3rd() {
        super("human");

    }

    @Override
    public void init() {
        if (!me().hasAttribute(MudData.HUMAN)) {
            me().addAttribute(MudData.SIZE, MudData.MEDIUM);
            me().addAttribute(MudData.SPEED, 30);
            me().addAttribute(MudData.HUMAN, 1);
        }
    }

    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.query && action.getData().equals(MudData.HUMAN)) {
            return TRUE;
        } else {
            return FALSE;
        }
    }
}
