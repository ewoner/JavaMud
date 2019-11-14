package Entities;

import Entities.PortalEntry.PortalEntry;
import Entities.bases.EntityWithRegion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * The basic Portal Entity of the game. (Not a PortalEntry)
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public class Portal extends EntityWithRegion {

    private Set<PortalEntry> portalentries = new HashSet<PortalEntry>();

    /**
     * Creates a basic bare bones Portal.
     *
     * DOES NOT ADD IT TO THE DATABASE.
     */
    public Portal() {
        super();
    }

    private Portal(int id) {
        this();
        setID(id);
    }

    /**
     * Addes the Portal to the Mud ensuring all Regions and rooms are linked
     * as needed.
     */
    public void add() {
        // add the portal to its region
        if (Region() != null) {
            Region().addPortal(getID());
        }

        // add each entry point to each room now
        for (PortalEntry p : getPortalEntries()) {
            Room room = dbg.roomDB.get(p.getStartRoom());
            room.addPortal((getID()));
        }
    }

    /**
     * Removes a portal from the Mud ensuring all regions and rooms as needed
     * are unlinked.
     */
    public void remove() {
        // remove the portal from its region
        if (Region() != null) {
            Region().removePortal(getID());
        }

        // remove portal from every room its in
        for (PortalEntry p : getPortalEntries()) {
            Room room = dbg.roomDB.get(p.getStartRoom());
            room.removePortal(getID());
        }
    }

    /**
     * Gets the destionation room from a starting room based
     * on the portal entries of the portal.
     *
     * @param startroom
     * @return Destination room or NULL if no entry found.
     */
    public Room getDestination(Room startroom) {
        for (PortalEntry pe : this.getPortalEntries()) {
            if (pe.getStartRoom() == startroom.getID()) {
                return dbg.roomDB.get(pe.getDestinationRoom());
            }
        }
        return null;
    }

    /**
     * Finds the PortalEntry assosiated with the starting room's id.
     *
     * @param startroomid
     * @return PortalEntry for Starting Room.
     */
    public PortalEntry seekStartRoom(int startroomid) {
        for (PortalEntry pe : this.getPortalEntries()) {
            if (pe.getStartRoom() == startroomid) {
                return pe;
            }

        }
        return null;
    }

    @Override
    public void load(BufferedReader p_stream) throws IOException {
        // find every room that this portal is in at the moment and remove it
        remove();
        p_stream.readLine();//[REGION}
        setRegion(Integer.parseInt(p_stream.readLine()));
        p_stream.readLine();//[NAME}
        setName(p_stream.readLine());
        p_stream.readLine();//[DESC}
        setDescription(p_stream.readLine());
        // now load all the portals
        getPortalEntries().clear();
        String temp = p_stream.readLine();// chew up "[/ENTRY]"
        while (!temp.equals("[/ENTRIES]")) {
            PortalEntry e = new PortalEntry();
            e.load(p_stream);
            p_stream.readLine();   // chew up "[/ENTRY]"
            temp =
                    p_stream.readLine();//chew up [ENTRY]
            getPortalEntries().add(e);
        }

// load attributes
        getAttributes().load(p_stream);
        // load my logic module
        getLogics().load(p_stream, this);
        // now insert the portal into every room that it's supposed to be in
        add();

    }

    @Override
    public void save(PrintWriter writer) {
        writer.println("[REGION]");
        writer.println(Region().getID());
        writer.println("[NAME]");
        writer.println(getName());
        writer.println("[DESCRIPTION]");
        writer.println(getDescription());
        // write out the entries
        for (PortalEntry p : getPortalEntries()) {
            writer.println("[ENTRY]");
            p.save(writer);
            writer.println("[/ENTRY]");
        }

        writer.println("[/ENTRIES]");
        // save my attributes to disk
        getAttributes().save(writer);

        // save my logic
        getLogics().save(writer);
    }

    /**
     * Gets a list of the PortalEntries.
     *
     * @return the portals Set of PortalEntries.
     */
    public Set<PortalEntry> getPortalEntries() {
        return portalentries;
    }
}
