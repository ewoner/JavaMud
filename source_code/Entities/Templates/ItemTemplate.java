package Entities.Templates;

import Databank.DataBank;
import Entities.bases.Entity;
import Entities.interfaces.HasData;
import Entities.interfaces.Template;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * The ItemTemplate represents a template to use to quickly make
 * new instances of a similar item.  This Template implements "HasData" but
 * many of the functionality is not support at this time.  This is due mainly
 * to the fact it appears no use for full functionallity.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public class ItemTemplate extends Entity implements
        HasData, Template {

    private DataBank<Integer> attributes = new DataBank<Integer>();
    private boolean isquantity;
    private int quantity = 1;// if so, what is the quantity?
    private Set<String> logics = new HashSet<String>();// if so, what is the quantity?

    
    /**
     * Returns if the item is a quantity item or not.  A quantity item is one
     * one instance can have more than one item. (Coins for example.)
     *
     * @return TRUE if can be more than one
     */
    public boolean isQuantity() {
        return isquantity;
    }

    /**
     * Sets if the item is a quanity item or not.
     *
     * @param isquantity
     */
    public void setIsQuantity(boolean isquantity) {
        this.isquantity = isquantity;
    }

    /**
     * Gets the current quantity of the item.  Defaults to 1.
     *
     * @return the templates default quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the current quantity of the item.
     *
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    
    /**
     * Gets a listing of all Logic Names normally with this item.
     *
     * @return Set of Item Names.
     */
    public Set<String> getLogics() {
        return logics;
    }

    @Override
    public void load(BufferedReader p_stream) throws IOException {
        p_stream.readLine();
        setName(p_stream.readLine());
        p_stream.readLine();
        setDescription(p_stream.readLine());
        p_stream.readLine();
        isquantity = Boolean.parseBoolean(p_stream.readLine());
        p_stream.readLine();
        quantity = Integer.parseInt(p_stream.readLine());

        // save my attributes to disk
        attributes.load(p_stream);

        // save my logic
        String temp = p_stream.readLine();
        temp = p_stream.readLine();
        while (!temp.contains("[/LOGICS]")) {
            logics.add(temp);
            temp = p_stream.readLine();
        }
    }

    @Override
    public void save(PrintWriter p_stream) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getAttribute(String p_name) {
        return attributes.get(p_name);
    }

    @Override
    public void setAttribute(String p_name, int p_val) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasAttribute(String p_name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addAttribute(String p_name, int p_initialval) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delAttribute(String p_name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DataBank getAttributes() {
        return attributes;
    }

    @Override
    public void setAttributes(DataBank<Integer> attributes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void copyAttributes(DataBank<Integer> attributes) {
        for (String key : attributes.getAttributeNames()) {
            this.attributes.set(key, attributes.get(key));
        }
    }
}
