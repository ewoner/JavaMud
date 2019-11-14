package Logics;

import Actions.Action;
import Actions.ActionType;
import Mud.D_and_D_Mud.MudData;

/**
 * The basic logic that makes your race a "human".  Addes all the attributes to
 * Mob attributes to include modifing the Mob's ability scores.
 *
 * Updated to 4th Edition.
 *
 * @author Brion Lang
 *  Date: 08 Dec 2009
 *
 *  Version 2.0.0
 */
public class human extends Logic {

    public human() {
        super("human");

    }

    @Override
    public void init() {
        if (!me().hasAttribute(MudData.HUMAN)) {
            me().addAttribute(MudData.SIZE, MudData.MEDIUM);
            me().addAttribute(MudData.SPEED, 6);
            me().addAttribute(MudData.HUMAN, 1);
            me().addAttribute(MudData.LANGUAGE_COMMON, 1);
        }
    }

    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.query && action.getData().equals(MudData.HUMAN)) {
            return TRUE;
        }
        return DEFAULT;

    }
}
