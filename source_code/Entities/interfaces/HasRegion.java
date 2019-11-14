package Entities.interfaces;

import Entities.Region;

/**
 * Functionality for any Entity that will hold a Region
 * Portal
 * Item
 * Character
 * Room
 *
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public interface HasRegion {

    //////////////////////////////////////////
    //               Origanals              //
    //////////////////////////////////////////
    /**
     * Returns the accual Region based on the Entity's Region ID
     *
     * @return Region of Entity
     */
    public Region Region();

    //////////////////////////////////////////
    //               BetterMud              //
    //////////////////////////////////////////
    /**
     * Gets the Entity's region's ID
     *
     * @return Region ID
     */
    public int getRegion();

    /**
     *  Sets the Entity's Region's ID.
     *
     * @param regionid
     */
    public void setRegion(int regionid);
}
