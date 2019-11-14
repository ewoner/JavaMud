package Databases.Group;

import Databases.*;

/**
 * A conventy class that brings all the Mud's databases in to one place to
 * easily call on and use.
 *
 *  @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public class DatabaseGroup {

    /**
     * Stores all the acounts.
     */
    public AccountDatabase accountDB = new AccountDatabase();
    /**
     * Stores all the characters and character templates.
     */
    public CharacterDatabase characterDB = new CharacterDatabase();
    /**
     * Stores al the items and item templates.
     */
    public ItemDatabase itemDB = new ItemDatabase();
    /**
     * Stores all the portals.
     */
    public PortalDatabase portalDB = new PortalDatabase();
    /**
     * Stores all the rooms.
     */
    public RoomDatabase roomDB = new RoomDatabase();
    /**
     * Stores all the regions.
     */
    public RegionDatabase regionDB = new RegionDatabase();
    /**
     * Stores all the Logics.
     */
    public LogicDatabase logicDB = new LogicDatabase();
    /**
     * Stores all the Commands.
     */
    public CommandDatabase commandDB = new CommandDatabase();

    /**
     * Purges all the databases but Commnds and Logics.
     */
    public void purge() {
        characterDB.purge();
        itemDB.purge();
        accountDB.purge();
        roomDB.purge();
        portalDB.purge();
        regionDB.purge();
    }
}
