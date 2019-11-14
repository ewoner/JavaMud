package Logics;

import Actions.Action;
import Actions.ActionType;
import Entities.MudCharacter;
import Entities.Room;
import Server.Main;

/**
 * This logic marks a mob as an "evilmonster".  This logic will cause it to
 * to attack a player who enters the room it is located.
 *
 * @author Brion Lang
 *  Date: 08 Dec 2009
 *
 * Version 1.0.1
 * 1.  Removed the "me" variable in favor of the "me()" method.  Note, kept the
 * the "me" variable when the entity has to be a "MudCharacter" class.
 * 2.  Added the check to see if the "entity" is a player to ensure the mob this
 * logic is attached to does not attack only players.
 * Version 1.0.0
 */
public class evilmonster extends Logic {

    private MudCharacter me;

    public evilmonster() {
        super("evilmonster");
    }

    /**
     * Must always have this method
     *
     * @param action
     * @return results of the action
     */
    @Override
    public int DoAction(Action action) {
        //Attack anyone who enters the room
        if (action.getType() == ActionType.enterroom) {
            MudCharacter mob = dbg.characterDB.get(action.arg1);
            if (mob != me() && (mob.isPlayer())) {
                Main.game.addActionAbsolute(0, ActionType.doaction, 0, me().getID(), action.arg1, 0, "initattack");
            }
            return DEFAULT;
        }
        // you killed someone find another target!
        if (action.getType() == ActionType.doaction && action.getData().equalsIgnoreCase("killed")) {
            me = (MudCharacter) me();
            Room r = me.Room();
            for (MudCharacter c : r.Characters()) {
                if (c.getID() != action.arg3) {
                    Main.game.addActionRelative(0, ActionType.doaction, 0, me().getID(), c.getID(), 0, "initattack");
                    return DEFAULT;
                }
            }
            return DEFAULT;
        }
        return DEFAULT;
    }
}


