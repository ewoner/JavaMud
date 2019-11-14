package Logics;

import Actions.Action;
import Actions.ActionType;
import Entities.MudCharacter;
import Mud.D_and_D_Mud.MudData;
import Mud.D_and_D_Mud.Skills;
//import utilities.Dice;

/**
 * The basic fighter class for the game.  All fighters will have this logic.
 * The logic hands leveling up and add needed attributes to the Mob using the
 * "init" method.
 *
 * @author Brion Lang
 *  Date: 8 Dec 2009
 *
 *  Version 1.0.0
 */
public class fighter extends CharacterClass {

    private MudCharacter me;
    private static final int HPatFirst = 15;
    private static final int HPperLevel = 6;
    private static final int HealingSurges = 9;;
    private static final int Skill_Points = 1;
    private static final String[] Class_Skills = {Skills.ATHLETICS, Skills.ENDURANCE, Skills.HEAL, Skills.INTIMIDATE, Skills.STREETWISE};

    public fighter() {
        super("fighter");
    }

    @Override
    public void init() {
        if (me().getAttribute(MudData.CHARARTER_LEVEL) == 0) {
        }
    }

    /**
     * Must always have this method
     *
     * @param action
     * @return resaults of trying to do Action
     */
    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.doaction && action.getData().equals("levelup")) {
            levelUp();
        }
        return 0;
    }

    private void levelUp() {
        
    }
}
