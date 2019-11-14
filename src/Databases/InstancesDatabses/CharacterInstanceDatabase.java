package Databases.InstancesDatabses;

import Databases.Bases.DefaultInstanceDatabase;
import Entities.MudCharacter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Instance database for all characters.  Part of the CharacterDatabase.
 *
 *  @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public class CharacterInstanceDatabase extends DefaultInstanceDatabase<MudCharacter> {

    @Override
    public MudCharacter create(int id) {
        MudCharacter a = new MudCharacter();
        a.setID(id);
        if (getContainer().containsKey(id)) {
            int newid = findOpenID();
            Logger.getLogger(CharacterInstanceDatabase.class.getName()).log(Level.SEVERE, "Character id " + id + " was already in use.  Using " + newid);
            id = newid;
        }
        getContainer().put(id, a);
        return a;
    }
}
