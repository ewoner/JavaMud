package Logics;

import Actions.Action;
import Actions.ActionType;
import Mud.D_and_D_Mud.MudData;

/**
 * The basic logic that makes your race an "elf".  Addes all the attributes to
 * Mob attributes to include modifing the Mob's ability scores.
 *
 * Update to 4th Edition.
 *
 * @author Brion Lang
 *  Date: 08 Dec 2009
 *
 *  Version 2.0.0
 */
public class halfelf extends Logic {

    public halfelf() {
        super("halfelf");

    }

    @Override
    public void init() {
        if (!me().hasAttribute(MudData.HALFELF)) {
            me().addAttribute(MudData.CON, me().getAttribute(MudData.CON) + 2);
            me().addAttribute(MudData.CHA, me().getAttribute(MudData.CHA) + 2);
            me().addAttribute(MudData.CON_NATURAL, me().getAttribute(MudData.CON_NATURAL) + 2);
            me().addAttribute(MudData.CHA_NATURAL, me().getAttribute(MudData.CHA_NATURAL) + 2);
            me().addAttribute(MudData.SIZE, MudData.MEDIUM);
            me().addAttribute(MudData.SPEED, 6);
            me().addAttribute(MudData.HALFELF, 1);
            me().addAttribute(MudData.LOWLIGHTVISION, 1);
            me().addAttribute(MudData.LANGUAGE_COMMON, 1);
            me().addAttribute(MudData.LANGUAGE_ELVEN, 1);
        }
    }

    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.query && action.getData().equals(MudData.HALFELF)) {
            return TRUE;
        }if (action.getType() == ActionType.query && action.getData().equals(MudData.FEYCREATURE)) {
            return TRUE;
        }
        return DEFAULT;
    }
}
