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
public class dwarf extends Logic {

    public dwarf() {
        super("dwarf");

    }

    @Override
    public void init() {
        if (!me().hasAttribute(MudData.DWARF)) {
            me().addAttribute(MudData.WIS, me().getAttribute(MudData.WIS) + 2);
            me().addAttribute(MudData.CON, me().getAttribute(MudData.CON) + 2);
            me().addAttribute(MudData.WIS_NATURAL, me().getAttribute(MudData.WIS_NATURAL) + 2);
            me().addAttribute(MudData.CON_NATURAL, me().getAttribute(MudData.CON_NATURAL) + 2);
            me().addAttribute(MudData.SIZE, MudData.MEDIUM);
            me().addAttribute(MudData.SPEED, 5);
            me().addAttribute(MudData.LOWLIGHTVISION, 1);
            me().addAttribute(MudData.DWARF, 1);
            me().addAttribute(MudData.LANGUAGE_COMMON, 1);
            me().addAttribute(MudData.LANGUAGE_DWARVEN, 1);
        }
    }

    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.query && action.getData().equals(MudData.DWARF)) {
            return TRUE;
        }
        return DEFAULT;
    }
}
