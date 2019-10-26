package Databases.TemplateDatabases;

import Databases.Bases.DefaultDatabase;
import Databases.Bases.DefaultInstanceDatabase;
import Entities.bases.Entity;
import Entities.interfaces.Template;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A wrapper class around two databases for the CharacterDatabase and
 * ItemDatabase.  The Templates get a DefaultDatabse while the instances
 * get a MapDatabase.
 *
 * @param <E> Type of Instance Entity.  Must extend Entity,
 * @param <T> Type of Tempalte Entity.  Must extend Entity,
 * 
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.1.0
 * 1.  Changed "templates" and "instances" to private and added getter methods for them.
 * 2.  Add a method to create databases.
 * 
 * Version 1.0.0
 */
public abstract class TemplateInstanceDatabase<E extends Entity, T extends Entity> {

    /*
     * A defaultDatabase to store templates.  Used in extended classes and as
     * such is protected for now.
     */
    private DefaultDatabase<T> templates;
    /*
     * A MapDatabase to store instances.  Used in Command and Logic databases
     * and as such is currently public.
     */
    private DefaultInstanceDatabase<E> instances;
    private Set<Integer> cleanup = new HashSet<Integer>();

    /**
     * Gets a instance Entity of ID
     * 
     * @param id The id of the entity to return.
     * @return The Entity.
     */
    public E get(int id) {
        if (getCleanup().contains(id)) {
            try {
                throw new Exception("Template Instance Database: Cleaned Up Item Reference!");
            } catch (Exception ex) {
                Logger.getLogger(TemplateInstanceDatabase.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        return getInstances().get(id);
    }

    /**
     * Number of Instances in the database.
     *
     * @return The Size of the instanced only database.
     */
    public int size() {
        return getInstances().size();
    }

    /**
     * The number of templates in the database.
     *
     * @return Returns size of the template database only.
     */
    public int sizetemplates() {
        return getTemplates().size();
    }

    /**
     * Gets a template from the database.
     *
     * @param id The Template's ID
     * @return A Template of 'id'.
     */
    public T getTemplate(int id) {
        return getTemplates().get(id);
    }

    /**
     * Returns a template using its name.  Partial matches are okay.
     * 
     * @param name Name of the Template to find.
     * @return The template.
     */
    public T getTemplate(String name) {
        for (T t : getTemplates().getContainer().values()) {
            if (t.getName().equalsIgnoreCase(name)) {
                return t;
            }
        }
        if (name.length() < 3) {
            return null;
        }
        for (T t : getTemplates().getContainer().values()) {
            if (t.getName().toLowerCase().contains(name.toLowerCase())) {
                return t;
            }
        }
        return null;
    }

    /**
     * Generates a new instance based on a template.  Uses the entity's
     * loadTemplate method.
     * 
     * @param templateID ID of the template.
     * @return A new Instance of templateID.
     */
    public E generate(int templateID) {
        // find an open ID, and create an entity at that ID
        int id = getInstances().findOpenID();
        E e = getInstances().create(id);

        // copy the template over into the new item
        Template temp = (Template) getTemplates().get(templateID);
        e.loadTemplate(temp);
        return e;
    }

    /**
     * Adds an instance to the cleanup set.
     *
     * @param id The instances ID.
     */
    public void erase(int id) {
        getCleanup().add(id);
    }

    /**
     * Permently removes any instances from the database if their ID has
     * been added to the cleanup set.  Clears the cleanup set when finished.
     */
    public void cleanup() {
        for (Integer I : getCleanup()) {
            getInstances().erase(I);
        }
        getCleanup().clear();
    }

    /**
     * Wrapper method for the instance database's findname method.
     * 
     * @param name The name to search for.
     * @return The Entity or NULL if not found.
     */
    public E findname(String name) {
        return getInstances().findname(name);
    }

    /**
     * Wrapper fo template database loadEntity() method.
     *
     * @param reader BufferedReader to load from.
     * @return Returns the template loaded
     * @throws java.io.IOException
     */
    public T loadEntityTemplate(BufferedReader reader) throws IOException {
        return getTemplates().loadEntity(reader);
    }

    /**
     * Wrapper fo template database saveEntity() method.
     * 
     * @param writer PriteWriter to use.
     * @param entity Entity to save.
     */
    public void saveEntityTemplate(PrintWriter writer, E entity) {
        getTemplates().saveEntity(writer, entity);
    }

    /**
     * Wrapper fo instance database loadEntity() method.
     * 
     * @param reader BufferedReader to load from.
     * @return The entity loaded
     * @throws java.io.IOException
     */
    public E LoadEntity(BufferedReader reader) throws IOException {
        return getInstances().loadEntity(reader);
    }

    /**
     * Wrapper fo instance database saveEntity() method.
     * 
     * @param writer PriteWriter to use.
     * @param entity Entity to save.
     */
    public void SaveEntity(PrintWriter writer, E entity) {
        getInstances().saveEntity(writer, entity);
    }

    /**
     * Wrapper around both templates and instances purge() methods.
     */
    public void purge() {
        getTemplates().purge();
        getInstances().purge();
    }

    /**
     * Wrapper aound the instances loadFile() method.
     * 
     * @param filename Name of the file to laod.
     */
    public void loadFile(String filename) {
        getInstances().loadFile(filename);
    }

    /**
     * Checks to see if the ID provided is valid.  An ID is valid if it is not
     * in the cleanup set, and if the instance database call to isValid() is 
     * true.
     * 
     * @param id ID to check
     * @return TRUE if valid
     */
    public boolean isValid(int id) {
        return (getCleanup().contains(id) &&
                getInstances().isValid(id));
    }

    /**
     * @return the templates
     */
    public DefaultDatabase<T> getTemplates() {
        return templates;
    }

    /**
     * @return the instances
     */
    public DefaultInstanceDatabase<E> getInstances() {
        return instances;
    }

    /**
     * @return the cleanup
     */
    public Set<Integer> getCleanup() {
        return cleanup;
    }
    /**
     *
     * @param templateDatabase
     * @param instanceDatabase
     */
    public void createDatabases(DefaultDatabase<T> templateDatabase , DefaultInstanceDatabase<E> instanceDatabase) {
       this.templates = templateDatabase;
       this.instances = instanceDatabase;
    }
}
