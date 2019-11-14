package Entities.interfaces;

/**
 * Functionality for any Entity that can be from a Template.
 * Item
 * Character
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public interface HasTemplateID {

    //////////////////////////////////////////
    //               BetterMud              //
    //////////////////////////////////////////
    /**
     * Gets the ID of the Template that the Entity is created from.
     *
     * @return Template ID
     */
    public int getTemplateID();

    /**
     * Sets the Entity's Template ID
     * 
     * @param id ID of Template
     */
    public void setTemplateID(int id);
}
