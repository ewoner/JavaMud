/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logics;

import Actions.Action;
import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;
import Mud.D_and_D_Mud.MudData;

/**
 *
 * @author Administrator
 * version 1.1.0
 * 1. Changed the way tat the character's race is being checked to handle the new logic modules.
 *
 * Version 1.0.0
 */
public class noelves extends Logic {

    public noelves() {
        super("noelves");

    }

    /**
     * Must always have this method
     *
     * @param action
     * @return results of action
     */
    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.canenterregion) {
            MudCharacter character = dbg.characterDB.get(action.arg1);
            if ((character.DoAction(ActionType.query, 0, 0, 0, 0, "elf") == TRUE)) { //.getTemplateID() == 2) {
                Item item = character.seekItem("Dwarven Mine Pass");
                if (item != null) {
                    return YES;
                }
                character.DoAction(ActionType.error, 0, 0, 0, 0, "As an elf, you are morally obligated to not enter these mines!");
                return NO;
            }
        }
        return DEFAULT;
    }
}
