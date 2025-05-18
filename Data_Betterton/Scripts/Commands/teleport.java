package Commands;

import Actions.ActionType;
import Entities.MudCharacter;
import Entities.Region;
import Entities.Room;
import Mud.Mud;
import Server.Main;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.0.0
 *
 */
public class teleport extends Command {

    public teleport(MudCharacter mob) {
        super(mob, "Teleport", "\"teleport <region name/room name/room id>\"",
                "Attempts to teleport you to a room");
    }

    @Override
    public void Execute(String p_parameters) {
        if (p_parameters.isEmpty()) {
            badUsage();
            return;
        }
        Room oldroom = Mob().Room();
        int roomID = 0;
        try {
            roomID = Integer.parseInt(p_parameters);
        } catch (Exception ex) {
            Region newregion = Mud.dbg.regionDB.findname(p_parameters);
            if (newregion == null) {
                Room newroom = Mud.dbg.roomDB.findname(p_parameters);
                if (newroom == null) {
                    Mob().DoAction(ActionType.error, 0, 0, 0, 0, "Can not find: " + p_parameters);
                    return;
                } else {
                    roomID = newroom.getID();
                }
            } else {
                int size = newregion.Rooms().size();
                ArrayList<Integer> rooms = new ArrayList<Integer>(newregion.getRooms());
                roomID = rooms.get((int)(Math.random()*size));
            }
        }
        try {
            Main.game.doAction(ActionType.attempttransport, Mob().getID(), roomID, 0, 0, "");
        } catch (Exception ex) {
            Logger.getLogger(teleport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
