package Entities.interfaces;

import Databank.DataBank;

/**
 * Basic funcality for any entity that requires data in the form of a
 * DataBank.
 *
 * For any Entity that will have attributes:
 * Portal
 * Character
 * Item
 * Room
 * Region
 * 
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public interface HasData {
    /* Known in BetterMud as DataEntity */
    //////////////////////////////////////////
    //               BetterMud              //
    //////////////////////////////////////////

    /**
     * Gets an attribute's value
     *
     * @param name Attribute
     * @return Value
     */
    public int getAttribute(String name);

    /**
     * Sets an Attribute.  
     * 
     * Currrently does the same as add due to underlying
     * DataBank implentation.
     *
     * @param name Attribute
     * @param value Value
     */
    public void setAttribute(String name, int value);

    /**
     * Checks to see if the Entity has the Attribute.
     *
     * @param name Attribute
     * @return TRUE if present, else FALSE
     */
    public boolean hasAttribute(String name);

    /**
     * Addes a new atrribute to the DataBank.
     *
     * Currently the same functionality as the setAttribute() method
     * due to DataBank's implentation.
     *
     * @param name Attribute
     * @param value Value
     */
    public void addAttribute(String name, int value);

    /**
     * Removes an Atrribute from the Entity.
     *
     * @param name Attriute to remove.
     */
    public void delAttribute(String name);

    /**
     * Retrunes the entire DataBank.  Used mainly to load and save the
     * DataBank.
     *
     * @return Entitys DataBank.
     */
    public DataBank<Integer> getAttributes();
    //////////////////////////////////////////
    //               BetterMud              //
    //////////////////////////////////////////

    /**
     * Sets the Entity's DataBank to the incoming DataBank.  NOTE-it does not
     * copy values, but sets the reference to the DataBank.
     *
     * @param attributes DataBank
     */
    public void setAttributes(DataBank<Integer> attributes);

    /**
     * Copies the attrbutes and values from one DataBank to another.
     * 
     * @param attributes DataBank to copy.
     */
    public void copyAttributes(DataBank<Integer> attributes);
}
