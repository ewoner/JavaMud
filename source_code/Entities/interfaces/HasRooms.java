package Entities.interfaces;

import Entities.Room;
import java.util.Set;

/**
 * Functionality for any Entity that Has multiple rooms.
 * Region
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public interface HasRooms {

    //////////////////////////////////////////
    //               BetterMud              //
    //////////////////////////////////////////
    /**
     * Adds room to Entity
     *
     * @param roomid
     */
    public void addRoom(int roomid);

    /**
     * Removes Room from Entity
     *
     * @param roomid
     */
    public void removeRoom(int roomid);

    /**
     * Returns number of room in Entity
     *
     * @return Number of Rooms
     */
    public int numRooms();

    //////////////////////////////////////////
    //               Accessors              //
    //////////////////////////////////////////
    /**
     * Gets all the Room ID's in Entity.
     *
     * @return Set of ID's
     */
    public Set<Integer> getRooms();

    /**
     * Gets all the Rooms' in the Entity.
     *
     * @return Set of Rooms based on ID's in Entity
     */
    public Set<Room> Rooms();
}
