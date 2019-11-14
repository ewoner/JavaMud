package Logics;

import Actions.Action;
import Actions.ActionType;
import Mud.D_and_D_Mud.MudData;

/**
 * The basic logic that makes your race an "elf".  Addes all the attributes to
 * Mob attributes to include modifing the Mob's ability scores.
 *
 * @author Brion Lang
 *  Date: 08 Dec 2009
 *
 *  Version 1.0.0
 */
public class elf3rd extends Logic {

    public elf3rd() {
        super("elf");

    }

    @Override
    public void init() {
        if (!me().hasAttribute(MudData.ELF)) {
            me().addAttribute(MudData.DEX, me().getAttribute(MudData.DEX) + 2);
            me().addAttribute(MudData.CON, me().getAttribute(MudData.CON) - 2);
            me().addAttribute(MudData.SIZE, 4);
            me().addAttribute(MudData.SPEED, 30);
            me().addAttribute(MudData.FAVOREDCLASS, 11);
            me().addAttribute(MudData.ELF, 1);
        }
    }

    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.query && action.getData().equals(MudData.ELF)) {
            return TRUE;
        } else {
            if (action.getType() == ActionType.query && action.getData().equals(MudData.IMMUNITYTOSLEEP)) {
                return TRUE;
            } else {
                return FALSE;
            }
        }
    }
}
