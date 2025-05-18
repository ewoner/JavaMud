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
public class dragonborn extends Logic {

    public dragonborn() {
        super("dragonborn");

    }

    @Override
    public void init() {
        if (!me().hasAttribute(MudData.DRAGONBORN)) {
            me().addAttribute(MudData.STR_NATURAL, me().getAttribute(MudData.STR_NATURAL) + 2);
            me().addAttribute(MudData.STR, me().getAttribute(MudData.STR) + 2);
            me().addAttribute(MudData.CHA_NATURAL, me().getAttribute(MudData.CHA_NATURAL) + 2);
            me().addAttribute(MudData.CHA, me().getAttribute(MudData.CHA) + 2);
            me().addAttribute(MudData.SIZE, MudData.MEDIUM);
            me().addAttribute(MudData.SPEED, 6);
            me().addAttribute(MudData.DRAGONBORN, 1);
            me().addAttribute(MudData.LANGUAGE_COMMON, 1);
            me().addAttribute(MudData.LANGUAGE_DRACONIC, 1);
        }
    }

    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.query && action.getData().equals(MudData.DRAGONBORN)) {
            return TRUE;
        }
        return DEFAULT;
    }
}
