package Entities.bases;

import Entities.interfaces.HasRegion;

/**
 * A Second Step to fully funtional Entitis.
 * Item
 * Character
 *
 * A Final Step for some.
 * Portal
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
abstract public class EntityWithRegion extends EntityWithData_Logic
        implements HasRegion {

    private int region;

    @Override
    public Entities.Region Region() {
        return Mud.Mud.dbg.regionDB.get(region);
    }

    
    @Override
    public void setRegion(int regionid) {
        this.region = regionid;
    }

    @Override
    public int getRegion() {
        return region;
    }
}
