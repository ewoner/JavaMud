package Databases;

import Databases.Bases.DefaultDatabase;
import Entities.Item;
import Entities.MudCharacter;
import Entities.Portal;
import Entities.Region;
import Entities.Room;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Region Database.
 *
 * @author Brion Lang Date: 17 Jam 2009
 *
 * Version 1.0.0
 */
public class RegionDatabase extends DefaultDatabase<Region> {

    private final String manifestfile = "data/regions/manifest";
    private final String regionsfolder = "data/regions/";
    private final String regiondatafile = "region.data";
    private final String roomdatafile = "rooms.data";
    private final String portaldatafile = "portals.data";
    private final String itemdatafile = "items.data";
    private final String characterdatafile = "characters.data";

    /**
     * Loads all regions from file.
     */
    public void loadAll() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(manifestfile));
            String name;
            while (reader.ready()) {
                name = reader.readLine();
                System.out.println("<--Loading region " + name + ".......");
                loadRegion(name);
            }
        } catch (IOException ex) {
            Logger.getLogger(RegionDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(RegionDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Loads a single region from file
     *
     * @param name Name of the Region to load
     */
    public void loadRegion(String name) {
        BufferedReader reader = null;
        try {
            String regionfolder = regionsfolder + name + "/";
            String regionfilename = regionfolder + regiondatafile;
            reader = new BufferedReader(new FileReader(regionfilename));
            System.out.println("<--<--Loading region entity.......");
            loadEntity(reader);
            System.out.println("<--<--Loading region rooms.......");
            Mud.Mud.dbg.roomDB.loadFile(regionfolder + roomdatafile);
            System.out.println("<--<--Loading region portals.......");
            Mud.Mud.dbg.portalDB.loadFile(regionfolder + portaldatafile);
            System.out.println("<--<--Loading region characters.......");
            Mud.Mud.dbg.characterDB.loadFile(regionfolder + characterdatafile);
            System.out.println("<--<--Loading region items.......");
            Mud.Mud.dbg.itemDB.loadFile(regionfolder + itemdatafile);
            reader.close();
        } catch (Exception ex) {
            Logger.getLogger(RegionDatabase.class.getName()).log(Level.SEVERE, "Error loading Refion: " + name, ex);
            System.exit(100);
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {
                Logger.getLogger(RegionDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Saves a region to file
     *
     * @param region
     */
    public void saveRegion(Region region) {
        //DataOutputStream regionfile = null;
        PrintWriter regionwriter = null;
        //DataOutputStream roomsfile;
        PrintWriter roomwriter = null;
        //DataOutputStream portalsfile;
        PrintWriter portalwriter = null;
        //DataOutputStream charactersfile;
        PrintWriter characterwriter = null;
        //DataOutputStream itemsfile;
        PrintWriter itemwriter = null;
        try {//region.getName().replaceAll(" ", "");
            String regfile = region.getDiskName();
            String regionfolder = regionsfolder + regfile + "/";
            String regionfilename = regionfolder + regiondatafile;
            regionwriter = new PrintWriter(new BufferedWriter(new FileWriter(regionfilename)));
            saveEntity(regionwriter, region);
            String roomsfilename = regionfolder + roomdatafile;
            roomwriter = new PrintWriter(new BufferedWriter(new FileWriter(roomsfilename)));
            for (Integer i : region.getRooms()) {
                Room room = Mud.Mud.dbg.roomDB.get(i);
                Mud.Mud.dbg.roomDB.saveEntity(roomwriter, room);
            }
            String portalsfilename = regionfolder + portaldatafile;
            portalwriter = new PrintWriter(new BufferedWriter(new FileWriter(portalsfilename)));
            for (Integer i : region.getPortals()) {
                Portal p = Mud.Mud.dbg.portalDB.get(i);
                Mud.Mud.dbg.portalDB.saveEntity(portalwriter, p);
            }
            String charactersfilename = regionfolder + characterdatafile;
            characterwriter = new PrintWriter(new BufferedWriter(new FileWriter(charactersfilename)));
            for (Integer i : region.getCharacters()) {
                MudCharacter p = Mud.Mud.dbg.characterDB.get(i);
                if (!p.isPlayer()) {
                    Mud.Mud.dbg.characterDB.SaveEntity(characterwriter, p);
                }
            }
            String itemfilename = regionfolder + itemdatafile;
            itemwriter = new PrintWriter(new BufferedWriter(new FileWriter(itemfilename)));
            for (Integer i : region.getItems()) {
                Item p = Mud.Mud.dbg.itemDB.get(i);
                Mud.Mud.dbg.itemDB.SaveEntity(itemwriter, p);
            }
        } catch (Exception ex) {
            Logger.getLogger(RegionDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            regionwriter.close();
            roomwriter.close();
            itemwriter.close();
            characterwriter.close();
            portalwriter.close();
        }
    }

    /**
     * Saves a region give only its ID.
     *
     * @param id Region ID
     */
    public void saveRegion(int id) {
        saveRegion(get(id));
    }

    /**
     * Saves all regions.
     */
    public void saveAll() {
        for (Region r : getContainer().values()) {
            if (r.getID() != 0) {
                saveRegion(r.getID());
            }
        }
    }

    @Override
    public Region create(int id) {
        Region template = new Region();
        getContainer().put(id, template);
        getContainer().get(id).setID(id);
        return getContainer().get(id);
    }
}
