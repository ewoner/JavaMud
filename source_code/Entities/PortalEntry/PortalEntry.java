package Entities.PortalEntry;

import Mud.Mud;
import Databases.Group.DatabaseGroup;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The PortalEntry class represents one complete starting location
 * and destination in a portal.  A portal may have one or more of
 * theses and they only work one direction.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public class PortalEntry {

    //////////////////////////////////////////
    //               Origanals              //
    //////////////////////////////////////////
    private DatabaseGroup dbg = Mud.dbg;
    //////////////////////////////////////////
    //               BetterMud              //
    //////////////////////////////////////////
    private int startroom;// starting room
    private String directionname;// name of the direction
    private int destinationroom;       // ending room

    //////////////////////////////////////////
    //               BetterMud              //
    //////////////////////////////////////////
    /**
     * Loades a entry from a BufferedReader.
     * 
     * @param reader BufferedReader to use
     * @throws java.io.IOException Calling method needs to handle
     */
    public void load(BufferedReader reader) throws IOException {
        reader.readLine();//chews up [STARTROOM]
        startroom = (Integer.parseInt(reader.readLine()));
        reader.readLine();//chews up [DIRECTION]
        this.directionname = reader.readLine();
        reader.readLine();//chews up [DESTINATION]
        destinationroom = (Integer.parseInt(reader.readLine()));
    }

    /**
     * Saves an entry to a writer.
     * 
     * @param writer PrintWriter to use.
     */
    public void save(PrintWriter writer) {
        writer.println("[START ROOM");
        writer.println(startroom);
        writer.println("[DIRECTION]");
        writer.println(directionname);
        writer.println("[DEST ROOM]");
        writer.println(destinationroom);
    }

    //////////////////////////////////////////
    //               Origanals              //
    //////////////////////////////////////////
    /**
     * Returns the id of the starting room
     *
     * @return Rooms ID
     */
    public int getStartRoom() {
        return startroom;
    }

    /**
     * Sets the Starting Room's id
     * 
     * @param id ID of the new Starting room
     */
    public void setStartRoom(int id) {
        startroom = id;
    }

    /**
     * Gets the direction's name.
     *
     * @return The Direction
     */
    public String getDirectionName() {
        return directionname;
    }

    /**
     * Sets the directions's name
     *
     * @param directionname The new name of the direction.
     */
    public void setDirectionName(String directionname) {
        this.directionname = directionname;
    }

    /**
     * Gets the id of the Destination room.
     *
     * @return Room's ID
     */
    public int getDestinationRoom() {
        return destinationroom;
    }

    /**
     * Sets the Destinion Room to a new ID.
     *
     * @param id Room's Id
     */
    public void setDestinationRoom(int id) {
        this.destinationroom = id;
    }
}
