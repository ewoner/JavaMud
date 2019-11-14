package Entities.bases;

import Entities.Item;
import Entities.MudCharacter;
import Entities.Portal;
import Entities.PortalEntry.PortalEntry;
import Entities.interfaces.HasCharacters;
import Entities.interfaces.HasItems;
import Entities.interfaces.HasPortals;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Extends 'EntityWithData_Logic'
 *
 * A Third and Final Stop to a fully Functional Entity.
 * Room
 * Region
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
abstract public class EntityWithCharacters_Items_Portals extends EntityWithData_Logic
        implements HasPortals, HasItems, HasCharacters {

    private Set<Integer> characters = Collections.synchronizedSet(new HashSet<Integer>());
    private Set<Integer> items = Collections.synchronizedSet(new HashSet<Integer>());
    private Set<Integer> portals = Collections.synchronizedSet(new HashSet<Integer>());

    @Override
    public void addPortal(int portalid) {
        portals.add(portalid);
    }

    @Override
    public void removePortal(int portalid) {
        portals.remove(portalid);
    }

    @Override
    public int numPortals() {
        return portals.size();
    }

    @Override
    public Set<Integer> getPortals() {
        return portals;
    }

    @Override
    public void addItem(int itemid) {
        items.add(itemid);
    }

    @Override
    public void removeItem(int itemid) {
        items.remove(itemid);
    }

    @Override
    public int numberItems() {
        return items.size();
    }

    @Override
    public Set<Integer> getItems() {
        return items;
    }

    @Override
    public void addCharacter(int characterid) {
        characters.add(characterid);
    }

    @Override
    public void removeCharacter(int characterid) {
        characters.remove(characterid);
    }

    @Override
    public int numCharacters() {
        return characters.size();
    }

    @Override
    public Set<Integer> getCharacters() {
        return characters;
    }


    @Override
    public Portal seekPortal(String portalname) {
        for (Integer i : getPortals()) {
            Portal portal = dbg.portalDB.get(i);
            PortalEntry pe = portal.seekStartRoom(getID());
            if (pe.getDirectionName().equalsIgnoreCase(portalname)) {
                return portal;
            }
        }
        for (Integer i : getPortals()) {
            Portal portal = dbg.portalDB.get(i);
            PortalEntry pe = portal.seekStartRoom(getID());
            if (portal.getName().equalsIgnoreCase(portalname)) {
                return portal;
            }
        }
        for (Integer i : getPortals()) {
            Portal portal = dbg.portalDB.get(i);
            PortalEntry pe = portal.seekStartRoom(getID());
            if (pe.getDirectionName().toLowerCase().contains(portalname.toLowerCase())) {
                return portal;
            }
        }
        for (Integer i : getPortals()) {
            Portal portal = dbg.portalDB.get(i);
            PortalEntry pe = portal.seekStartRoom(getID());
            if (portal.getName().toLowerCase().contains(portalname.toLowerCase())) {
                return portal;
            }
        }
        return null;
    }

    @Override
    public Item seekItem(String name) {
        for (Integer k : getItems()) {
            Item item = dbg.itemDB.get(k);
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        if (name.length() < 3) {
            return null;
        }
        for (Integer k : getItems()) {
            Item item = dbg.itemDB.get(k);
            if (item.getName().toLowerCase().contains(name.toLowerCase())) {
                return item;
            }
        }
        return null;
    }

    @Override
    public MudCharacter seekCharacter(String name) {
        for (Integer k : getCharacters()) {
            MudCharacter mob = dbg.characterDB.get(k);
            if (mob.getName().equalsIgnoreCase(name)) {
                return mob;
            }
        }
        if (name.length() < 3) {
            return null;
        }
        for (Integer k : getCharacters()) {
            MudCharacter mob = dbg.characterDB.get(k);
            if (mob.getName().toLowerCase().contains(name.toLowerCase())) {
                return mob;
            }
        }
        return null;
    }

    @Override
    public Set<Item> Items() {
        Set<Item> itemset = new HashSet<Item>();
        for (Integer k : getItems()) {
            itemset.add(dbg.itemDB.get(k));
        }
        return itemset;
    }

    @Override
    public Set<Portal> Portals() {
        Set<Portal> itemset = new HashSet<Portal>();
        for (Integer k : getPortals()) {
            itemset.add(dbg.portalDB.get(k));
        }
        return itemset;
    }

    @Override
    public Set<MudCharacter> Characters() {
        Set<MudCharacter> itemset = new HashSet<MudCharacter>();
        for (Integer k : getCharacters()) {
            itemset.add(dbg.characterDB.get(k));
        }
        return itemset;
    }
}



