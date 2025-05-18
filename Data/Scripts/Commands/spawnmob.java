package Commands;

import Actions.ActionType;
import Entities.MudCharacter;
import Entities.Templates.CharacterTemplate;
import Mud.Mud;
import Server.Main;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.0.0
 *
 */
public class spawnmob extends Command {

    public spawnmob(MudCharacter mob) {
        super(mob, "spawnmob", "\"spawnmob <character template id/template name>\"",
                "Spawns a new character in your room");
    }

    @Override
    public void Execute(String args) {
        if (args.isEmpty()) {
            badUsage();
            return;
        }
        CharacterTemplate t;
        try {
            t = Mud.dbg.characterDB.getTemplate(Integer.parseInt(args));
        } catch (Exception ex) {
            t = Mud.dbg.characterDB.getTemplate(args);
        }
        Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Spawning Character...");
        Main.game.doAction(ActionType.spawncharacter, t.getID(), Mob().getRoom(), 0, 0, "");
        Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Success.");



    }
}
