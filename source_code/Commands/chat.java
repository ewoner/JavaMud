package Commands;

import Actions.ActionType;
import Entities.MudCharacter;
import Mud.MyTimer;
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
public class chat extends Command {

    public chat(MudCharacter mob) {
        super(mob, "Chat", "\"chat <message>\"",
                "This sends a message to every player who is currently logged into the game.");
    }

    @Override
    public void Execute(String parameters) {
        if (parameters.isEmpty()) {
            badUsage();
            return;
        }
        Main.game.addActionAbsolute(MyTimer.GetTimeMS(), ActionType.chat, Mob().getID(), 0, 0, 0, parameters);

    }
}
    
