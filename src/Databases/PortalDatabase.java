package Databases;

import Databases.Bases.DefaultDatabase;
import Entities.Portal;

/**
 * The Portal Database.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public class PortalDatabase extends DefaultDatabase<Portal> {

    @Override
    public Portal create(int id) {
        Portal template = new Portal();
        getContainer().put(id, template);
        getContainer().get(id).setID(id);
        return getContainer().get(id);
    }

    /**
     * Creates a new portal after finding the last ID of the portals loaded.
     *
     * @return the New portal created.
     */
    public Portal create(){
        Portal template = new Portal();
        int newID = getContainer().size()+1;
        template.setID(newID);
        getContainer().put(newID, template);
        return getContainer().get(newID);
    }
}
