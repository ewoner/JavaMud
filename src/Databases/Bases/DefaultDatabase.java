package Databases.Bases;

import Entities.bases.Entity;
import java.util.Map;
import java.util.TreeMap;

/**
 * Modeled off the BetterMud's VectorDatabase, later changed its contatiner
 * to a Map<Integer, E>.  This class only implements 'Databases<E>' methodes,
 * and adds a single getter method for the container.
 *
 * @param <E> Entity to store.
 *
 *  @author Brion Lang
 *  Date: 17 Jan 2009
 *  Version 1.1.0
 * 1. Makes container private and adds a single getter method to return the current container.
 *
 *  Version 1.0.0
 */
public abstract class DefaultDatabase<E extends Entity> extends Database<E> {

    private Map<Integer, E> container = new TreeMap<Integer, E>();

    @Override
    public E findname(String p_name) {
        for (E e : getContainer().values()) {
            if (e.getName().equalsIgnoreCase(p_name)) {
                return e;
            } else if (p_name.length() >= 3 && e.getName().toLowerCase().contains(p_name.toLowerCase())) {
                return e;
            }
        }
        return null;
    }

    @Override
    public E get(int id) {
        return getContainer().get(id);
    }

    @Override
    public int size() {
        return getContainer().size();
    }

    @Override
    public void purge() {
        getContainer().clear();
    }

    @Override
    public boolean isValid(int id) {
        return getContainer().containsKey(id) && id != 0;
    }

    /**
     * Returns the current container.
     *
     * @return the container
     */
    public Map<Integer, E> getContainer() {
        return container;
    }
}
