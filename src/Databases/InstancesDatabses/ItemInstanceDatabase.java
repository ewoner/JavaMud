package Databases.InstancesDatabses;

import Databases.Bases.DefaultInstanceDatabase;
import Entities.Item;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Instance database for all items.  Part of the ItemDatabase.
 *
 *  @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public class ItemInstanceDatabase extends DefaultInstanceDatabase<Item> {

    @Override
    public Item create(int id) {
        Item a = new Item();
        a.setID(id);
        if (getContainer().containsKey(id)) {
            int newid = findOpenID();
            Logger.getLogger(ItemInstanceDatabase.class.getName()).log(Level.SEVERE, "Item id " + id + " was already in use.  Using " + newid);
            id = newid;
        }
        getContainer().put(id, a);
        return a;
    }
}
