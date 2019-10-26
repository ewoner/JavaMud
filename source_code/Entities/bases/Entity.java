package Entities.bases;

import Entities.interfaces.Template;
import Mud.Mud;
import Databases.Group.DatabaseGroup;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The Entity Class is the baic class all things in the mud are
 * dervided from.
 *
 * Any ID of 0 means that the Entity is not in the Mud anymore.  It is the
 * same as NULL.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
abstract public class Entity {

    //////////////////////////////////////////
    //               BetterMud              //
    //////////////////////////////////////////
    private int ID;
    private String name;
    private String description;
    private int reference_count;
    //////////////////////////////////////////
    //               Origanals              //
    //////////////////////////////////////////
    /**
     * Convience field to access all databases of the mud.
     */
    protected DatabaseGroup dbg = Mud.dbg;

    //////////////////////////////////////////
    //               BetterMud              //
    //////////////////////////////////////////
    /**
     * Gets the entity's name
     *
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the entity's description
     *
     * @return Description
     */
    public String getDescription() {
        return description;
    }

    /**
     * gets the Entity's id
     *
     * @return ID
     */
    public int getID() {
        return ID;
    }

    private void addRef() {
        reference_count++;
    }

    private void delRef() {
        reference_count--;
    }

    private int getRef() {
        return reference_count;
    }

    /**
     * Sets the entity's name
     * 
     * @param name Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the entity's description
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the entity's id.  DOES NOT CHANGE IT IN THE DATABASE!
     * 
     * @param id
     * 
     */
    public void setID(int id) {
        this.ID = id;
    }

    //////////////////////////////////////////
    //               Origanals              //
    //////////////////////////////////////////
    /**
     * Loads entity form a BufferedReader.
     *
     * @param reader BufferedReader to load from
     * @throws java.io.IOException
     */
    abstract public void load(BufferedReader reader) throws IOException;

    /**
     * Saves entity to a PrintWriter.
     * 
     * @param writer PrintWriter to save on.
     */
    abstract public void save(PrintWriter writer);

    /**
     * Loads entity form a template.  Note- this is an empty method
     * body.  It is on the inheriting classes to impliment this method
     * if needed.
     * 
     * @param template Template to laod.
     */
    public void loadTemplate(Template template) {
    }
}
