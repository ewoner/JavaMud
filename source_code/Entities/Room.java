package Entities;

import Entities.bases.EntityWithCharacters_Items_Portals;
import Entities.interfaces.HasRegion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The basic Room Entity of the game.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public class Room extends EntityWithCharacters_Items_Portals implements
        HasRegion {

    private int region;

    /**
     * Creates a bare bones Room Entity.
     *
     * DOES NOT ADD IT TO THE DATABASE.
     */
    public Room() {
        super();
    }

    private Room(int roomid) {
        this();
        setID(roomid);
    }

    /**
     * Addes the Room to its Region.
     */
    public void add() {
        // add the room to its region:
        if (region != 0 && getID() != 0) {
            Region().addRoom(getID());
        }
    }

    /**
     * Removes the Room from its Region.
     */
    public void remove() {
        // remove the room from its region
        if (region != 0 && getID() != 0) {
            Region().removeRoom(getID());
        }
    }

    @Override
    public Entities.Region Region() {
        return Mud.Mud.dbg.regionDB.get(region);
    }

    @Override
    public void setRegion(int regionid) {
        this.region = regionid;
    }

    @Override
    public void load(BufferedReader reader) throws IOException {
        // if room is somewhere, then remove it first
        remove();
        reader.readLine();
        region = Integer.parseInt(reader.readLine());
        reader.readLine();
        setName(reader.readLine().trim());
        reader.readLine();
        setDescription(reader.readLine().trim());
        // load attributes
        getAttributes().load(reader);
        // load my logic module
        getLogics().load(reader, this);
        add();
    }

    @Override
    public void save(PrintWriter writer) {
        writer.println("[REGION]");
        writer.println(region);
        writer.println("[NAME]");
        writer.println(getName());
        writer.println("[DESCRIPTION]");
        writer.println(getDescription());
        // save my attributes to disk
        getAttributes().save(writer);
        // save my logic
        getLogics().save(writer);
    }

    @Override
    public int getRegion() {
        return region;
    }
}
