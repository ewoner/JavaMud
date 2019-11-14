package Databases;

import Databases.InstancesDatabses.CharacterInstanceDatabase;
import Databases.TemplateDatabases.CharacterTemplateDatabase;
import Databases.TemplateDatabases.TemplateInstanceDatabase;
import Entities.MudCharacter;
import Entities.Templates.CharacterTemplate;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Character Database.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 * Version 1.0.1
 * 1.  Changed references fix errors when underlying database structure changes.  No fundimental changes.
 *  Version 1.0.0
 */
public class CharacterDatabase extends TemplateInstanceDatabase<MudCharacter, CharacterTemplate> {

    private final String playerfolder = "data/players/";
    private final String templatefolder = "data/templates/characters/";

    /**
     * Creates a new instance and template databases.
     */
    public CharacterDatabase() {
        createDatabases(new CharacterTemplateDatabase(),new CharacterInstanceDatabase());
        //templates = new CharacterTemplateDatabase();
        //instances = new CharacterInstanceDatabase();
    }

    /**
     * Tries to locate a player from a full name match.
     * 
     * @param name Name to search for
     * @return NULL if not found or player.
     */
    public MudCharacter findPlayerFull(String name) {
        for (MudCharacter c : getInstances().getContainer().values()) {
            if (c.getName().toLowerCase().equals(name.toLowerCase())) {
                return c;
            }
        }
        return null;
    }

    /**
     * Tries to locate a player with a partical name match using "starts with"
     * 
     * @param name Name to search
     * @return Player or NULL if not found.
     */
    public MudCharacter findPlayerPart(String name) {
        if (name.length() >= 3) {
            for (MudCharacter c : getInstances().getContainer().values()) {
                if (c.getName().toLowerCase().startsWith(name.toLowerCase())) {
                    return c;
                }
            }
        }
        return null;
    }

    /**
     * Saves all players only.  Not instnaces that are not players or templates.
     */
    public void savePlayers() {
        //DataOutputStream playerfile = null;
        PrintWriter playerfile = null;
        try {
            // save all the players to disk
            for (MudCharacter c : getInstances().getContainer().values()) {
                if (c.isPlayer()) {
                    String playerfilename = playerfolder + c.getName() + ".data";
                    //playerfile = new DataOutputStream(new FileOutputStream(playerfilename));
                    playerfile = new PrintWriter(new BufferedWriter(new FileWriter(playerfilename)));
                    getInstances().saveEntity(playerfile, c);
                    playerfile.close();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(CharacterDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            playerfile.close();
        }
    }

    /**
     * Loads players from the player folder.
     */
    public void loadPlayers() {
        // load all the players
        getInstances().loadDirectory(playerfolder);
    }

    /**
     * Loads templates from the template folder.
     */
    public void loadTemplates() {
        getTemplates().loadDirectory(templatefolder);
    }

    /**
     * Loads a template from the template folder.
     *
     * @param filename Name of the template to load
     */
    public void loadTemplate(String filename) {
        getTemplates().loadFile(templatefolder + filename);
    }

    /**
     * Loads a player from the player folder.
     *
     * @param filename Name of the player to load
     */
    public void loadPlayer(String filename) {
        getInstances().loadFile(playerfolder + filename);
    }


}
