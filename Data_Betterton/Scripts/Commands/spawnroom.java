package Commands;

import Actions.Action;
import Actions.ActionType;
import Entities.MudCharacter;
import Entities.Portal;
import Entities.Room;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.0.0
 *
 */
public class spawnroom extends Command {

    public spawnroom(MudCharacter mob) {
        super(mob, "spawnroom", "\"spawnroom <direction name> \"",
                "Spawns a new room in the direction you choose");
    }

    @Override
    public void Execute(String argus) {
        if (argus.isEmpty()) {
            badUsage();
            return;
        }
        Room room = Mob().Room();
        Portal direction = room.seekPortal(argus);
        if (direction != null) {
            Mob().DoAction(new Action(ActionType.error, "Can not spawn new room in that direction.  An exit already exists.  To add other destinations, use the \"modify\" command."));
            return;
        }
        Portal newdirection;
    }
}
