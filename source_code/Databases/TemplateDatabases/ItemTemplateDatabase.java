package Databases.TemplateDatabases;

import Databases.Bases.DefaultDatabase;
import Entities.Templates.ItemTemplate;

/**
 * The Template database for all items.  Part of the ItemDatabase.
 *
 *  @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public class ItemTemplateDatabase extends DefaultDatabase<ItemTemplate> {

    @Override
    public ItemTemplate create(int id) {
        
        ItemTemplate template = new ItemTemplate();
        getContainer().put(id, template);
        getContainer().get(id).setID(id);
        return getContainer().get(id);
    }
}
