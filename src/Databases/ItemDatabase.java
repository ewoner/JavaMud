/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Databases;

import Databases.InstancesDatabses.ItemInstanceDatabase;
import Databases.TemplateDatabases.ItemTemplateDatabase;
import Databases.TemplateDatabases.TemplateInstanceDatabase;
import Entities.Item;
import Entities.Templates.ItemTemplate;

/**
 * The Item Database.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 * Version 1.0.1
 * 1.  Changed references fix errors when underlying database structure changes.  No fundimental changes.
 *  Version 1.0.0
 */
public class ItemDatabase extends TemplateInstanceDatabase<Item, ItemTemplate> {

    private final String templatefolder = "data/templates/items/";

    /**
     *
     */
    public ItemDatabase() {
        createDatabases(new ItemTemplateDatabase(),new ItemInstanceDatabase());
        //templates = new ItemTemplateDatabase();
        //instances = new ItemInstanceDatabase();
    }

    /**
     * Wrapper method for templates loadDiretory
     */
    public void loadTemplates() {
        getTemplates().loadDirectory(templatefolder);
    }

    /**
     * Wrapper method for template loadFile
     *
     * @param filename
     */
    public void loadTemplate(String filename) {
        getTemplates().loadFile(templatefolder + filename);
    }
}