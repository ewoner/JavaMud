package LogicCollection;

import Logics.*;
import Actions.Action;
import Entities.bases.Entity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * The basic Room Entity of the game.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public class LogicCollection {

    private HashMap<String, Logic> collection = new HashMap<String, Logic>();

    /**
     * Creates an empty Logic Collaction.
     */
    public LogicCollection() {
        super();
    }

    /**
     * Gets a the Logic Module
     *
     * @param logicname Nmae of the module.
     * @return The Logic Module
     */
    public Logic get(String logicname) {
        return collection.get(logicname);
    }

    /**
     * Adds Existing Logic Module.  Used internallin in the collection and to
     * add Handlers (see server package).
     *
     * @param logic Module to add
     */
    public void addExisting(Logic logic) {
        if (logic != null) {
            collection.put(logic.getName(), logic);
        }
    }

    /**
     * Delets Logic Module based on name.
     * 
     * @param logicname Name to delete
     */
    public void delete(String logicname) {
        collection.remove(logicname);
    }

    /**
     * Checks to see if the Entity has a Logic Module based on it name.
     * Does a exact search. (capitals and all).
     *
     * @param logicname Name of Logic Module
     * @return TRUE if found else FALSE
     */
    public boolean hasLogic(String logicname) {
        return collection.containsKey(logicname);
    }

    /**
     * Gets an valued from the Logic Module attributes
     *
     * @param logicname Logic Module Name
     * @param attribute Attribute
     * @return Value of the Atrribute
     */
    public int getAttribute(String logicname, String attribute) {
        return collection.get(logicname).getAttribute(attribute);
    }

    /**
     * Sets an Logic Modules Attribute.  By DataBank's implementation, it will
     * also add a new attribute.
     *
     * @param logicname Logic Module Name
     * @param attribute Atrribute
     * @param value Value of the Atrribute
     */
    public void setAttribute(String logicname, String attribute, int value) {
        collection.get(logicname).setAttribute(attribute, value);
    }

    /**
     *Poresses the Actions to all the Logic modules on the Entity.  Stops
     * processing once a NON ZERO (x>0) resault is found.
     *
     * @param action Action to process.
     * @return Value of processed action from all Logic Modules.
     */
    public int DoAction(Action action) {
        ArrayList<Logic> list = new ArrayList<Logic>(collection.values());
        for (int i = 0; i < list.size(); i++) {
            int j = list.get(i).DoAction(action);
            if (j != 0) {
                return j;
            }
        }
        return 0;
    }

    /**
     * Loads a Logic Collection from a BufferedReader.
     *
     * @param reader BufferedRead to read
     * @param entity Entity that the Logic Module will belong to
     * @throws java.io.IOException
     */
    public void load(BufferedReader reader, Entity entity) throws IOException {
        String temp;
        temp = reader.readLine();       // chew up the "[LOGICS]"
        temp = reader.readLine();       // load in the first logic name
        // loop while there are logic modules available
        while (!temp.contains("/LOGICS")) {
            add(temp, entity);
            Logic c = get(temp);
            if (c != null) {
                c.Load(reader);
            } else {
                while (!temp.contains("/DATA")) {
                    temp = reader.readLine();
                }
            }
            temp = reader.readLine();       // try reading next logic name
        }
    }

    /**
     *Saves a Logic Collection to A PrintWriter
     *
     *  @param writer PrintWriter to save to.
     */
    public void save(PrintWriter writer) {
        writer.println("[LOGICS]");
        Set<String> keys = collection.keySet();
        for (String s : keys) {
            Logic l = collection.get(s);
            if (l.canSave()) {
                writer.println(l.getName());
                l.Save(writer);
            }
        }
        writer.println("[/LOGICS]");
    }

    /**
     * Adds a new logic module to an Entity.
     *
     * @param logicname Logic Module name to add
     * @param entity Entity to add Logic Module to.
     */
    public void add(String logicname, Entity entity) {
        Logic l = Mud.Mud.dbg.logicDB.generate(logicname, entity);
        addExisting(l);
    }

    /**
     * Gets all the names of logics in the collection.
     *
     * @return a Set of String containing all the logic names in the collection.
     */
    public Set<String> getLogicNames() {
        return this.collection.keySet();
    }
}
