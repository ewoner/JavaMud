/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logics;

import Actions.Action;
import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;
import Server.Main;

/**
 *
 * @author Administrator
 */
public class spellscroll extends Logic {

    public spellscroll() {
        super("spellscroll");
    }

    public spellscroll(String name) {
        super(name);
    }

    public void doRead(MudCharacter c, Item i, String name) {

        if (c.hasCommand(name)) {
            c.DoAction(ActionType.error, 0, 0, 0, 0, "You already know this spell!");
            return;
        }
        c.addCommand(name);
        Main.game.addActionAbsolute(0, ActionType.vision, c.getRoom(), 0, 0, 0, c.getName() + " reads " + i.getName() + "!");
        Main.game.addActionAbsolute(1, ActionType.destroyitem, i.getID(), 0, 0, 0, "");
        c.DoAction(ActionType.announce, 0, 0, 0, 0, "You now know the spell " + name + "!");
        c.DoAction(ActionType.announce, 0, 0, 0, 0, "The " + i.getName() + " disappears in a bright flash of flame!");
    }
}
