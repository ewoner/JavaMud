package Logics;

import Actions.Action;
import Actions.ActionType;
import Server.Main;

/**
 * A logic module used to spawn mobs.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 * Version 1.0.0
 */
public class spawnmobroom extends Logic {

    private long time = 15000L;
    private int mobTemplateID = 201;

    /**
     * Creates a new spawnmobroom Logic Module.
     */
    public spawnmobroom() {
        super("spawnmobroom");
    }

    @Override
    public void init() {
    }

    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.destroycharacter) {
            Main.game.addActionRelative(0, ActionType.vision, me().getID(), 0, 0, 0, "A goblin dies!");
            Main.game.addActionRelative(time, ActionType.spawncharacter, mobTemplateID, me().getID(), 0, 0, "");
            Main.game.addActionRelative(time + 1, ActionType.vision, me().getID(), 0, 0, 0, "A goblin appears out of nowhere.");
        }
        return DEFAULT;
    }
}
