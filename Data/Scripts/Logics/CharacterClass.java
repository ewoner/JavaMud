/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logics;

import Actions.Action;

/**
 *
 * @author Administrator
 */
abstract public class CharacterClass extends Logic {

    public static final int GOOD = 0;
    public static final int POOR = 1;
    public static final int AVERAGE = 2;
    public static final int[][] BASE_ATTACK_BONUS = {{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20}, {0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10}, {0, 1, 2, 3, 3, 4, 5, 6, 6, 7, 8, 9, 9, 10, 11, 12, 12, 13, 14, 15}};
    public static final int[][] BASE_SAVE = {{2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12}, {0, 0, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6}};
    public static final int NEW_FEATS = 3;
    public static final int ABILITY_SCORE_INCREASE = 4;

    public CharacterClass(String name) {
        super(name);
    }

    @Override
    abstract public int DoAction(Action action);

    private void levelUp() {
        return;
    }
}
