package Databases.Bases;

import Entities.bases.Entity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The basic backbone of the database system. All other databases except the
 * Commands and Logics database start here. This class has no container for
 * storage, just neeeded methods.
 *
 * @param <E> The type of Entity to store. (Must be of the base type "Entity".
 *
 * @author Brion Lang Date: 17 Jan 2009
 *
 * Version 1.0.0
 */
public abstract class Database<E extends Entity> {

    /**
     * Seaches the database of an Entity by name.
     *
     * @param name Name of the entity to find.
     * @return Entoy with the provided name.
     */
    abstract public E findname(String name);

    /**
     * Creates a new entity. To place an entity in the database, you must call
     * this factory. It creates a new entity and ensures that the id is not
     * currently in use prior to adding it to the database.
     *
     * @param id The id wanted for the new entity.
     * @return A barebones Entiy with an id and saved in database.
     */
    abstract public E create(int id);

    /**
     * Gets an and enttiy from the database.
     *
     * @param id The entity's id to return
     * @return Entity with 'id
     */
    abstract public E get(int id);

    /**
     * The number of element of the databae's containter
     *
     * @return Size of containter.
     */
    abstract public int size();

    /**
     * Deletes everything in the database.
     */
    abstract public void purge();

    /**
     * Checks to make sure that the id is currently in use in the database.
     *
     * @param id The ID to validate.
     * @return TRUE if the container has that ID in it keys.
     */
    abstract public boolean isValid(int id);

    /**
     * Loads an entity for a the BufferedReader. Most of the work is done with
     * the Entity's own 'load()' method. First calls the 'create()' method.
     *
     * @param reader BufferedReader to load from.
     * @return Entity from the Bufferedreader.
     * @throws java.io.IOException calling method has to deal with in errors.
     */
    public E loadEntity(BufferedReader reader) throws IOException {
        int id;
        //String temp;
        String temp = reader.readLine();
        id = Integer.parseInt(reader.readLine());    // load the ID

        E e = create(id);  // load/create entity
        e.setID(id);
        e.load(reader);        // load it from the stream
        System.out.println("<------Loaded:" + e.getName());
        return e;
    }

    /**
     * Saves an entity to a PrintWriter. Most of the work is done by the
     * entity's own 'save()' mehtod.
     *
     * @param writer PrintWriter to use.
     * @param entity Entity to save.
     */
    public void saveEntity(PrintWriter writer, Entity entity) {
        // save the ID first:
        writer.println("[ID]");
        writer.println(entity.getID());

        // save the entity and give extra space for the next one
        entity.save(writer);
    }

    /**
     * Loads a whole directory from file. Creates a new file, checkes if it is a
     * directory and then loads individual files from the directory using a call
     * to 'loadFile()'.
     *
     * @param directory The diretory to load.
     */
    public void loadDirectory(String directory) {
        File dir = new File(directory);
        if (!dir.isDirectory()) {
            return;
        }
        for (File f : dir.listFiles()) {
            loadFile(f);
        }
    }

    /**
     * Loads a single file of the file name provide. Calls 'loadFile(File)' to
     * do the work.
     *
     * @param filename String of the file's name to laod.
     */
    public void loadFile(String filename) {
        this.loadFile(new File(filename));
    }

    /**
     * Loades a file. File must contain correct entity to load properly. Most of
     * the work is done by the loadEntity method.
     *
     * Handles all Exceptions from inner method calls.
     *
     * @param file File to load.
     */
    public void loadFile(File file) {
        int entityCount = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            
            while (reader.ready()) {
                E e = loadEntity(reader);
                entityCount++;
            }
        } catch (Exception ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, "Error while loading " + file.getAbsolutePath() + " Entity Count: " + entityCount, ex);
            System.exit(100);
        }
    }
}
