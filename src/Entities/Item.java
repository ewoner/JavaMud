package Entities;

import Entities.Templates.ItemTemplate;
import Entities.bases.EntityWithRegion_Room;
import Entities.interfaces.Template;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The basic Item Entity of the game.
 *
 * NOITE - If the Region's ID = 0, then the Item belongs to a character.
 * 
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public class Item extends EntityWithRegion_Room {

    private boolean isquantity = false;             // if so, what is the quantity?
    private int quantity = 1;// is this a quantity object?

    /**
     * Creates a bare bones new Item Entity.
     *
     * NOTE- DOES NOT ADD IT TO THE DATABASE.
     */
    public Item() {
    }

    private Item(int id) {
        this();
        setID(id);
    }

    /**
     * Overrides the Entity getName() method to put the quantity in the name.
     * Everywhere that <#> is found in the name it replaces it with the number
     * from quantity.
     *
     * DO NOT USE THIS METHOD TO SAVE ANY INFORMATION.
     * Instaed call 'super.getName()'.
     *
     * @return a String of the Item with its quanity.
     */
    @Override
    public String getName() {
        String name = super.getName();
        if (isquantity) {
            return name.replaceAll("<#>", "" + quantity);
        } else {
            return super.getName();
        }
    }

    @Override
    public void loadTemplate(Template template) {
        ItemTemplate itemtemplate = (ItemTemplate) template;
        setTemplateID(itemtemplate.getID());
        setName(itemtemplate.getName());
        setDescription(itemtemplate.getDescription());
        isquantity = itemtemplate.isQuantity();
        quantity = itemtemplate.getQuantity();
        copyAttributes(itemtemplate.getAttributes());
        for (String logicname : itemtemplate.getLogics()) {
            addLogic(logicname);
        }
    }

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
     * Gets the current quantity of the item.  Defaults to 1.
     *
     * @return the Item;s quantity.
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

    @Override
    public void load(BufferedReader reader) throws IOException {
        remove();
        reader.readLine();
        setName(reader.readLine());
        reader.readLine();
        setDescription(reader.readLine());
        reader.readLine();
        setRoom(Integer.parseInt(reader.readLine()));
        reader.readLine();
        setRegion(Integer.parseInt(reader.readLine()));
        reader.readLine();
        isquantity = Boolean.parseBoolean(reader.readLine());
        reader.readLine();
        quantity = Integer.parseInt(reader.readLine());
        reader.readLine();
        setTemplateID(Integer.parseInt(reader.readLine()));

        // save my attributes to disk
        getAttributes().load(reader);

        // save my logic
        getLogics().load(reader, this);
        add();
    }

    @Override
    public void save(PrintWriter writer) {
        writer.println("[NAME]");
        writer.println(super.getName());
        writer.println("[DESCRIPTION]");
        writer.println(getDescription());
        writer.println("[ROOM]");
        writer.println(getRoom());
        writer.println("[REGION]");
        writer.println(getRegion());
        writer.println("[ISQUANTITY]");
        writer.println(isquantity);
        writer.println("[QUANTITY]");
        writer.println(quantity);
        writer.println("[TEMPLATEID]");
        writer.println(getTemplateID());
        getAttributes().save(writer);
        getLogics().save(writer);
    }

    /**
     * Addes the Item to the Region, Room or Character.
     */
    public void add() {
        // when regions are 0, that means the item is on a character
        if (getRegion() == 0) {
            MudCharacter c = dbg.characterDB.get(getRoom());
            c.addItem(getID());
        } else {
            Region reg = dbg.regionDB.get(getRegion());
            reg.addItem(getID());

            Room r = dbg.roomDB.get(getRoom());
            r.addItem(getID());
        }
    }

    /**
     * Renoves the Item from the Region, Room or Character.
     */
    public void remove() {
        // just return if the room is 0, that means the item hasn't been loaded yet
        if (getRoom() == 0) {
            return;
        }
        // when regions are 0, that means the item is on a character
        if (getRegion() == 0) {
            MudCharacter c = dbg.characterDB.get(getRoom());
            c.removeItem(getID());
        } else {
            Region reg = dbg.regionDB.get(getRegion());
            reg.removeItem(getID());

            Room r = dbg.roomDB.get(getRoom());
            r.removeItem(getID());
        }
    }

    @Override
    public Room Room() {
        if (getRegion() == 0) {
            return null;
        } else {
            return super.Room();
        }
    }
}
