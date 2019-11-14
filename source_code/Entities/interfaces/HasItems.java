package Entities.interfaces;

import Entities.Item;
import java.util.Set;

/**
 * Basic functionality for any Entity that hold Itens,
 * Character
 * Room
 * Region
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public interface HasItems {

    //////////////////////////////////////////
    //               BetterMud              //
    //////////////////////////////////////////
    /**
     * Addes Item to Entity
     *
     * @param itemid
     */
    public void addItem(int itemid);

    /**
     * Removes Item from Entity
     *
     * @param itemid
     */
    public void removeItem(int itemid);

    /**
     *  Number of Items in Entity
     *
     * @return Number of Items.
     */
    public int numberItems();

    //////////////////////////////////////////
    //               Origanals              //
    //////////////////////////////////////////
    /**
     * Set of all items' id in Enity.
     *
     * @return Set of IDs
     */
    public Set<Integer> getItems();

    /**
     * Convience method to get all Items in Enity based on
     * their ID's in the Entity.
     *
     * @return Set of Items
     */
    public Set<Item> Items();

    //////////////////////////////////////////
    //               Accessors              //
    //////////////////////////////////////////
    /**
     * Gets an item if in Entity.
     *
     * @param name Name of item.  Partial matches work.
     * @return Item seeked or null if not found.
     */
    public Item seekItem(String name);
}
