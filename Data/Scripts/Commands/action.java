/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Commands;

import Actions.ActionType;
import Entities.MudCharacter;
import Server.Main;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 * Version 1.0.0
 *
 */
public class action extends Command {

    public action(MudCharacter mob) {
        super(mob, "action", "\"action <verb phrase>\"",
                "This executes a superflous action that doesn't affect game logic.");
    }

    @Override
    public void Execute(String args) {
        if (args.isEmpty()) {
            badUsage();
            return;
        }
        Main.game.addActionAbsolute(0, ActionType.announce, 0, 0, 0, 0, "<#FFFF00>" + Mob().getName() + " " + args);
    }
}
