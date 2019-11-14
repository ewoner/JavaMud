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
public class elf extends Logic {

    public elf() {
        super("elf");

    }

    @Override
    public void init() {
        if (!me().hasAttribute(MudData.ELF)) {
            me().addAttribute(MudData.DEX, me().getAttribute(MudData.DEX) + 2);
            me().addAttribute(MudData.WIS, me().getAttribute(MudData.WIS) + 2);
            me().addAttribute(MudData.DEX_NATURAL, me().getAttribute(MudData.DEX_NATURAL) + 2);
            me().addAttribute(MudData.WIS_NATURAL, me().getAttribute(MudData.WIS_NATURAL) + 2);
            me().addAttribute(MudData.SIZE, MudData.MEDIUM);
            me().addAttribute(MudData.SPEED, 7);
            me().addAttribute(MudData.ELF, 1);
            me().addAttribute(MudData.LOWLIGHTVISION, 1);
            me().addAttribute(MudData.LANGUAGE_COMMON, 1);
            me().addAttribute(MudData.LANGUAGE_ELVEN, 1);
        }
    }

    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.query && action.getData().equals(MudData.ELF)) {
            return TRUE;
        }if (action.getType() == ActionType.query && action.getData().equals(MudData.FEYCREATURE)) {
            return TRUE;
        }
        return DEFAULT;
    }
}
