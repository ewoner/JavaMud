package Entities;

import Entities.bases.EntityWithCharacters_Items_Portals;
import Entities.interfaces.HasRooms;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * The basic Region Entity of the game.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public class Region extends EntityWithCharacters_Items_Portals
        implements HasRooms {

    private Set<Integer> rooms = new HashSet<Integer>();
    private String diskname;

    public Region() {
        super();
    }

    private Region(int id) {
        this();
        setID(id);
    }

    /**
     * Overrides the Entity setName() method to set the disk name of the
     * region
     *
     * @param name Region's Name
     */
    @Override
    public void setName(String name) {
        super.setName(name);
        setDiskName(name.replaceAll(" ", ""));
    }

    @Override
    public void addRoom(int roomid) {
        rooms.add(roomid);
    }

    @Override
    public void removeRoom(int roomid) {
        rooms.remove(roomid);
    }

    @Override
    public int numRooms() {
        return rooms.size();
    }

    @Override
    public Set<Integer> getRooms() {
        return rooms;
    }

    @Override
    public void load(BufferedReader reader) throws IOException {
        String temp = reader.readLine();//[NANE]
        setName(reader.readLine());
        temp = reader.readLine();//[DESCRIPTION]
        setDescription(reader.readLine());
        // load attributes
        getAttributes().load(reader);
        // load my logic module
        getLogics().load(reader, this);
    }

    @Override
    public void save(PrintWriter writer) {
        writer.println("[NAME]");
        writer.println(getName());
        writer.println("[DESCRIPTION]");
        writer.println(getDescription());
        // save my attributes to disk
        getAttributes().save(writer);
        // save my logic
        getLogics().save(writer);
    }

    /**
     * Gets the Regions DiskName.
     *
     * @return the Tegion's Disk Name
     */
    public String getDiskName() {
        return diskname;
    }

    /**
     * Sets the Regions Disk Name.  A Disk Name does not contain any
     * spacing.
     *
     * @param diskname The Name of the region minus spaces.
     */
    public void setDiskName(String diskname) {
        this.diskname = diskname;
    }

    @Override
    public Set<Room> Rooms() {
        Set<Room> itemset = new HashSet<Room>();
        for (Integer k : getRooms()) {
            itemset.add(dbg.roomDB.get(k));
        }
        return itemset;
    }
}
    
