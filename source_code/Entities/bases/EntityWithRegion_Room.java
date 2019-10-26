package Entities.bases;

import Entities.Room;
import Entities.interfaces.HasRoom;
import Entities.interfaces.HasTemplateID;

/**
 * The Third and final Stop to a fully Funtional Entity.  Also implements 'HasTemplateID'.
 * Item
 * Character
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
abstract public class EntityWithRegion_Room extends EntityWithRegion
        implements HasTemplateID, HasRoom {

    private int room;
    private int template_id;

    @Override
    public int getTemplateID() {
        return template_id;
    }

    @Override
    public void setTemplateID(int templateid) {
        template_id = templateid;
    }

    @Override
    public int getRoom() {
        return room;
    }

    @Override
    public Room Room() {
        return Mud.Mud.dbg.roomDB.get(room);
    }

    @Override
    public void setRoom(int roomid) {
        this.room = roomid;
    }
}
