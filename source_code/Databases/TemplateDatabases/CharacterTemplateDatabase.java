package Databases.TemplateDatabases;

import Databases.Bases.DefaultDatabase;
import Entities.Templates.CharacterTemplate;

/**
 * The Template database for all characters.  Part of the CharacterDatabase.
 *
 *  @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public class CharacterTemplateDatabase extends DefaultDatabase<CharacterTemplate> {

    @Override
    public CharacterTemplate create(int id) {
        
        CharacterTemplate template = new CharacterTemplate();
        getContainer().put(id,template);
        getContainer().get(id).setID(id);
        return getContainer().get(id);
    }
}
