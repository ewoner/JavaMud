package Logics;

import Actions.Action;
import Actions.ActionType;
import Mud.D_and_D_Mud.MudData;

/**
 * The basic logic that makes your race a "dwarf".  Addes all the attributes to
 * Mob attributes to include modifing the Mob's ability scores.
 *
 * Updated to 4th Edition.
 *
 * @author Brion Lang
 *  Date: 08 Dec 2009
 *
 *  Version 2.0.0
 */
public class eladrin extends Logic {

    public eladrin() {
        super("eladrin");

    }

    @Override
    public void init() {
        if (!me().hasAttribute(MudData.ELADRIN)) {
            me().addAttribute(MudData.DEX, me().getAttribute(MudData.DEX) + 2);
            me().addAttribute(MudData.INT, me().getAttribute(MudData.INT) + 2);
            me().addAttribute(MudData.DEX_NATURAL, me().getAttribute(MudData.DEX_NATURAL) + 2);
            me().addAttribute(MudData.INT_NATURAL, me().getAttribute(MudData.INT_NATURAL) + 2);
            me().addAttribute(MudData.SIZE, MudData.MEDIUM);
            me().addAttribute(MudData.SPEED, 6);
            me().addAttribute(MudData.LOWLIGHTVISION, 1);
            me().addAttribute(MudData.ELADRIN, 1);
            me().addAttribute(MudData.LANGUAGE_COMMON, 1);
            me().addAttribute(MudData.LANGUAGE_ELVEN, 1);
        }
    }

    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.query && action.getData().equals(MudData.ELADRIN)) {
            return TRUE;
        }if (action.getType() == ActionType.query && action.getData().equals(MudData.FEYCREATURE)) {
            return TRUE;
        }
        return DEFAULT;
    }
}
