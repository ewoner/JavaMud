package Entities.interfaces;

import Entities.Portal;
import java.util.Set;

/**
 * Funtionality for any Entity with portals.
 * Room
 * Region
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public interface HasPortals {

    //////////////////////////////////////////
    //               BetterMud              //
    //////////////////////////////////////////
    /**
     * Adds a portal to the Entity
     *
     * @param portalid
     */
    public void addPortal(int portalid);

    /**
     * Removes a portal from the Entity.
     *
     * @param portalid
     */
    public void removePortal(int portalid);

    /**
     * Number of Portals on the Entity.
     *
     * @return Number of portals
     */
    public int numPortals();

    //////////////////////////////////////////
    //               Origanals              //
    //////////////////////////////////////////
    /**
     * All the portal ID's on the Entity.
     *
     * @return Set of Portal ID's
     */
    public Set<Integer> getPortals();

    /**
     * Convience method to get all the Portals on the Entity from thier
     * ID's.
     *
     * @return Set of Portals on the Entity.
     */
    public Set<Portal> Portals();

    /**
     * Seeks a portal in the Entity.  First checks in following order for a portal.
     * 1. Direction Name (full)
     * 2. Portal Name (full)
     * 3. Direction Name (partial)
     * 4. Portal Name (partial)
     * Ensures that the start room is this Enity.
     *
     * @param portalname
     * @return Portal or NULL if none found.
     */
    public Portal seekPortal(String portalname);
}
