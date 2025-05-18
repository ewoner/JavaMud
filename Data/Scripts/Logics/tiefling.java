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
public class tiefling extends Logic {

    public tiefling() {
        super("tiefling");

    }

    @Override
    public void init() {
        if (!me().hasAttribute(MudData.TIEFLING)) {
            me().addAttribute(MudData.INT, me().getAttribute(MudData.INT) + 2);
            me().addAttribute(MudData.CHA, me().getAttribute(MudData.CHA) + 2);
            me().addAttribute(MudData.INT_NATURAL, me().getAttribute(MudData.INT_NATURAL) + 2);
            me().addAttribute(MudData.CHA_NATURAL, me().getAttribute(MudData.CHA_NATURAL) + 2);
            me().addAttribute(MudData.SIZE, MudData.MEDIUM);
            me().addAttribute(MudData.SPEED, 6);
            me().addAttribute(MudData.LOWLIGHTVISION, 1);
            me().addAttribute(MudData.TIEFLING, 1);
            me().addAttribute(MudData.LANGUAGE_COMMON, 1);
        }
    }

    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.query && action.getData().equals(MudData.TIEFLING)) {
            return TRUE;
        }
        return DEFAULT;
    }
}
