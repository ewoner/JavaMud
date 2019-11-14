package Commands;

import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;
import Mud.D_and_D_Mud.MudData;
import Mud.Mud;
//import utilities.Dice;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 * Version 1.0.0
 *
 */
public class appraise extends Command {

    public appraise(MudCharacter mob) {
        super(mob, "appraise", "\"appraise <item><|glass><|scale>\"",
                "Attempts to appraise the value of an item");
    }

    @Override
    public void Execute(String parameters) {
        /*if (parameters.isEmpty()) {
            badUsage();
            return;
        }
        int bonus = 0;
        int DC = 12;
        String[] paras = Mud.splitString(parameters);
        Item item = Mob().seekItem(paras[0]);
        if (item == null) {
            badUsage();
            return;
        }
        if (item.getAttribute(Mob().getName() + "appraised") > 0) {
            Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "You appraise " + item.getName() + " at " + item.getAttribute(Mob().getName() + "appraised") + " copper coins.");
            return;
        } else if (item.getAttribute(Mob().getName() + "appraised") == -1) {
            Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "You still can not determine its value.");
            return;
        }
        if (item.getAttribute(MudData.RARE) == 1) {
            DC = 15;
        } else if (item.getAttribute(MudData.EXOTIC) == 1 || item.getAttribute(MudData.EXOTIC_WEAPON) == 1) {
            DC = 20;
        } else if (item.getAttribute(MudData.UNIQUE) == 1) {
            DC = 25;
        }
        if (Mob().getAttribute("diligent") == 1) {
            bonus += 2;
        }
        if ((item.getAttribute("stone") == 1 || item.getAttribute("metal") == 1) && (Mob().getAttribute(MudData.DWARF) == 1)) {
            bonus += 2;
        }
        if (paras.length > 1) {
            for (int i = 1; i < paras.length; i++) {
                if ((paras[i].equalsIgnoreCase("glass") && Mob().seekItem("Magnifying Glass") != null) || (paras[i].equalsIgnoreCase("scale") && Mob().seekItem("Merchant's Scale") != null)) {
                    bonus += 2;
                }
            }
        }
        int rank = Mob().getAttribute(MudData.APPRAISESKILL);
        boolean trained = false;
        if (rank < 0) {
            rank = (-rank) - 1;
            trained = true;
        }
        if (rank > 0) {
            trained = true;
        }
        Dice.setTroubleOutput(true);
        int roll = Dice.Roll_d20() + rank + bonus;
        if (roll >= DC) {
            if (trained) {
                Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "You appraise " + item.getName() + " at " + item.getAttribute("value") + " copper coins.");
                item.setAttribute(Mob().getName() + "appraised", item.getAttribute("value"));
            } else if (item.getAttribute("rare") == 1 || item.getAttribute("exotic") == 1) {
                int value = (int) ((item.getAttribute("value") * (Dice.Roll_2d() + 3) * 0.10));
                Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "You appraise " + item.getName() + " at " + value + " copper coins.");
                item.setAttribute(Mob().getName() + "appraised", value);
            } else {
                Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "You appraise " + item.getName() + " at " + item.getAttribute("value") + " copper coins.");
                item.setAttribute(Mob().getName() + "appraised", item.getAttribute("value"));
            }
        } else {
            if (trained) {
                int value = (int) ((item.getAttribute("value") * (Dice.Roll_2d() + 3) * 0.10));
                Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "You appraise " + item.getName() + " at " + value + " copper coins.");
                item.setAttribute(Mob().getName() + "appraised", value);
            } else {
                Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "You can not determine its value.");
                item.setAttribute(Mob().getName() + "appraised", -1);
            }
        }
        Dice.setTroubleOutput(false);
    */}
}
