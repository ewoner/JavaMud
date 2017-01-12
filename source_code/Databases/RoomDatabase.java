package Databases;

import Databases.Bases.DefaultDatabase;
import Entities.Room;

/**
 * The Room Database.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public class RoomDatabase extends DefaultDatabase<Room> {

    @Override
    public Room create(int id) {
                Room template = new Room();
        getContainer().put(id,template);
        getContainer().get(id).setID(id);
        return getContainer().get(id);
    }
}
