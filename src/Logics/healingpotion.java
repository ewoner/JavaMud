package Logics;

import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;
import Server.Main;

/**
 * This is the basic logic that makes a healingpotion work.  Currently set up
 * only for small healing potions.
 *
 * @author Brion Lang
 *  Date: 08 Dec 2009
 *
 *  Version 1.0.0
 */
public class healingpotion extends usableitem {

    @Override
    public void doUse(MudCharacter c, Item i) {
        int cured = 0;
        if (i.getName().toLowerCase().startsWith("small")) {
            cured = 20;
            c.setAttribute("hitpoints", c.getAttribute("hitpoints" )+cured);
            if (c.getAttribute("hitpoints") > c.getAttribute("maxhitpoints")) {
                 cured -= c.getAttribute("hitpoints") - c.getAttribute("maxhitpoints");
                 c.setAttribute("hitpoints", c.getAttribute("maxhitpoints"));
            }
        }
        Main.game.addActionAbsolute(0, ActionType.vision, c.getRoom(), 0, 0, 0, c.getName() + " drinks a " + i.getName() + "!");
        Main.game.addActionAbsolute(1, ActionType.destroyitem, i.getID(), 0, 0, 0, "");
        c.DoAction(ActionType.announce, 0, 0, 0, 0, "You are cured : "+cured+" hitpoints from drinking the " + i.getName() + "!");
    }
}
