package Logics;

import Actions.Action;
import Actions.ActionType;
import Mud.D_and_D_Mud.MudData;

/**
 * The basic logic that makes your race a "dwarf".  Addes all the attributes to
 * Mob attributes to include modifing the Mob's ability scores.
 *
 * @author Brion Lang
 *  Date: 08 Dec 2009
 *
 *  Version 1.0.0
 */
public class dwarf3rd extends Logic {

    public dwarf3rd() {
        super("dwarf");

    }

    @Override
    public void init() {
        if (!me().hasAttribute(MudData.DWARF)) {
            me().addAttribute(MudData.CHA, me().getAttribute(MudData.CHA) - 2);
            me().addAttribute(MudData.CON, me().getAttribute(MudData.CON) + 2);
            me().addAttribute(MudData.SIZE, MudData.MEDIUM);
            me().addAttribute(MudData.SPEED, 20);
            me().addAttribute(MudData.FAVOREDCLASS, MudData.FIGHTER_NUM);
            me().addAttribute(MudData.DARKVISION, 1);
            me().addAttribute(MudData.DWARF, 1);
        }
    }

    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.query && action.getData().equals(MudData.DWARF)) {
            return TRUE;
        } else {
            return FALSE;
        }
    }
}
