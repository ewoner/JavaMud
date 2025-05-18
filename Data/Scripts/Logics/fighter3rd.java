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
public class fighter3rd extends CharacterClass {

    private MudCharacter me;
    private static final int HD = 10;
    private static final int Skill_Points = 1;
    private static final String[] Class_Skills = {Skills.CLIMB, Skills.JUMP, Skills.SWIM};
    private static final int BASE_ATTACK = GOOD;
    private static final int FORT_SAVES = GOOD;
    private static final int REF_SAVES = POOR;
    private static final int WILL_SAVES = POOR;

    public fighter3rd() {
        super("fighter");
    }

    @Override
    public void init() {
        if (me().getAttribute(MudData.CHARARTER_LEVEL) == 0) {
            me().setAttribute(MudData.MAX_HITPOINTS, HD + MudData.getBonus(me().getAttribute(MudData.CON)));
            me().setAttribute(MudData.HITPOINTS, me().getAttribute(MudData.MAX_HITPOINTS));
            me().setAttribute(MudData.FIGHTER, 1);
            me().setAttribute(MudData.BASETOHIT, me().getAttribute(MudData.BASETOHIT) + 1);
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
        me().setAttribute(MudData.BASETOHIT, me().getAttribute(MudData.BASETOHIT) + 1);
        me().setAttribute(MudData.FIGHTER, me().getAttribute(MudData.FIGHTER) + 1);
        //me().setAttribute(MudData.MAX_HITPOINTS, me().getAttribute(MudData.MAX_HITPOINTS) + Dice.Roll_Multi(HD, 1) + MudData.getBonus(me().getAttribute(MudData.CON)));
        me().setAttribute(MudData.HITPOINTS, me().getAttribute(MudData.MAX_HITPOINTS));
        me().setAttribute(MudData.NEXT_LEVEL_XP, me().getAttribute(MudData.NEXT_LEVEL_XP) + (me().getAttribute(MudData.FIGHTER)) * 1000);
    }
}
