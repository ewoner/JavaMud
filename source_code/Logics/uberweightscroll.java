/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logics;

import Actions.Action;
import Actions.ActionType;
import Entities.Item;

/**
 *
 * @author Administrator
 */
public class uberweightscroll extends spellscroll {

    public uberweightscroll() {
        super("uberweightscroll");
    }

    /**
     * Must always have this method
     *
     * @param action
     * @return results of the action
     */
    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.doaction && action.getData().equalsIgnoreCase("read")){
            doRead( dbg.characterDB.get(action.arg3), (Item)me(), "uberweight" );
            return DEFAULT;}
        return DEFAULT;
    }
}
