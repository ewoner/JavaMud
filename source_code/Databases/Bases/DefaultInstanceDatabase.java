package Databases.Bases;

import Entities.bases.Entity;
import java.util.Map;
import java.util.TreeMap;

/**
 * A database that uses a Map<Integer, E> to store its data.
 *
 * @param <E> Entity type to store.
 *
 *  @author Brion Lang
 *  Date: 17 Jam 2009
 *
 * Version 2.0.1
 * 1.  There was an issue with the "findOpenID" method.  It has been redone to
 * now correctly return the first number in the database that is not in use.
 * Version 2.0.0
 * 1.  Renamed to "DefaultInstanceDatabase" from "MapDatabase".
 * 2.  Changed the base class from Database<E> to DefaultDatabase<E>.
 *  Version 1.0.0
 */
public abstract class DefaultInstanceDatabase<E extends Entity> extends DefaultDatabase<E> {

    /**
     * Finds the next open ID in the database.  "0" is considered "null"
     * and never returned.
     *
     * @return Next open ID.
     */
    public int findOpenID() {
        int index = 1;
        while (this.get(index) != null) {
            index++;
        }
        return index;
    }

    /*
     * Finds the next open ID in the database.  "0" is considered "null"
     * and never returned.
     *
     * Removed due to errors in getting a new ID number
     * (never returned 1 for example)
     *
     * @return Next open ID.
     */
    private int oldfindOpenID() {
        if (getContainer().size() == 0) {
            return 1;
        }
        if (getContainer().size() == 1) {
            return 2;
        }
        int openid = 0;
        int previous = 0;

        while (openid == 0) {
            for (Integer I : getContainer().keySet()) {
                if (I != previous + 1) {
                    openid = previous + 1;
                    break;
                } else {
                    previous = I;
                }
            }
        }
        return openid;
    }

    /**
     * Removes the entity of ID from the database.  This is final action,
     * there is no way to recover the entity.
     *
     * @param id Entity's id to remove.
     */
    public void erase(int id) {
        getContainer().remove(id);
    }
}
