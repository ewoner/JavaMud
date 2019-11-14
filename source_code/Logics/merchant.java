package Logics;

import Actions.Action;
import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;
import Entities.Templates.ItemTemplate;
import Mud.D_and_D_Mud.MudData;
import Server.Main;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This logic module allows the mob to be a merchant.
 *
 * @author Brion Lang
 *  Date: 08 Dec 2009
 *
 *  Version 1.0.0
 */
public class merchant extends Logic {

    protected Set<Integer> inventory = Collections.synchronizedSet(new HashSet<Integer>());
    private Set<Item> items = Collections.synchronizedSet(new HashSet<Item>());
    private MudCharacter me;

    public merchant() {
        super("merchant");
    }

    protected merchant(String name) {
        super(name);
    }

    /**
     * Must always have this method
     *
     * @param action
     * @return 0 = succuess.  other numbers very by logic.
     */
    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.doaction && action.getData().equalsIgnoreCase("list")) {
            MudCharacter character = dbg.characterDB.get(action.arg3);
            me = (MudCharacter) me();
            character.DoAction(ActionType.announce, 0, 0, 0, 0, "<#7F7F7F>--------------------------------------------------------------------------------");
            character.DoAction(ActionType.announce, 0, 0, 0, 0, "<#FFFFFF> Item                                      | Cost");
            character.DoAction(ActionType.announce, 0, 0, 0, 0, "<#7F7F7F>--------------------------------------------------------------------------------");
            for (Item item : Inventory()) {
                character.DoAction(ActionType.announce, 0, 0, 0, 0, "<#7F7F7F> " + item.getName() + "   | " + item.getAttribute("value"));
            }
            character.DoAction(ActionType.announce, 0, 0, 0, 0, "<#7F7F7F>--------------------------------------------------------------------------------");
            cleanUp();
            return 0;
        }
        if (action.getType() == ActionType.doaction && action.getData().toLowerCase().startsWith("buy")) {
            String itemname = action.getData().substring(action.getData().indexOf("buy ") + 4);
            MudCharacter character = dbg.characterDB.get(action.arg3);
            Item item = seekItem(itemname);
            me = (MudCharacter) me();
            if (item == null) {
                character.DoAction(ActionType.announce, 0, 0, 0, 0, "Sorry, you can't buy " + itemname + "here!");
                cleanUp();
                return 0;
            }
            ItemTemplate t = dbg.itemDB.getTemplate(item.getTemplateID());
            if (!hasEnoughCurrency(character, t.getAttribute("value"))) {
                character.DoAction(ActionType.announce, 0, 0, 0, 0, "Sorry, you don't have enough money to buy " + t.getName() + "!");
                cleanUp();
                return 0;
            }
            giveCurrency(character, me, t.getAttribute("value"));
            try {
                Main.game.doAction(ActionType.spawnitem, t.getID(), character.getID(), 1, 0, "");
            } catch (Exception ex) {
                Logger.getLogger(merchant.class.getName()).log(Level.SEVERE, null, ex);
            }
            Main.game.addActionAbsolute(0, ActionType.vision, character.getRoom(), 0, 0, 0, character.getName() + " buys " + t.getName() + ".");
        }
        cleanUp();
        return 0;
    }

    private Set<Item> Inventory() {
        if (items.isEmpty()) {
            for (int k : inventory) {
                Item i = dbg.itemDB.generate(k);
                items.add(i);
            }
        }
        return items;
    }

    private void cleanUp() {
        if (!items.isEmpty()) {
            Iterator<Item> i = items.iterator();
            while (i.hasNext()) {
                Item item = i.next();
                dbg.itemDB.erase(item.getID());
            }
            items.clear();
        }
    }

    private void giveCurrency(MudCharacter c, MudCharacter r, int amount) {
        Item copper = null, silver = null, gold = null, plat = null;
        for (Item i : c.Items()) {
            if (i.getTemplateID() == 1) {
                copper = i;
            }
            if (i.getTemplateID() == 2) {
                silver = i;
            }
            if (i.getTemplateID() == 3) {
                gold = i;
            }
            if (i.getTemplateID() == 4) {
                plat = i;
            }
        }
        if (copper == null) {
            copper = dbg.itemDB.generate(1);
            copper.setQuantity(0);
        }
        if (silver == null) {
            silver = dbg.itemDB.generate(2);
            silver.setQuantity(0);
        }
        if (gold == null) {
            gold = dbg.itemDB.generate(3);
            gold.setQuantity(0);
        }
        if (plat == null) {
            plat = dbg.itemDB.generate(4);
            plat.setQuantity(0);
        }
        int neededAmount;
        int neededCoins;
        if (amount <= copper.getQuantity()) {
            try {
                Main.game.doAction(ActionType.attemptgiveitem, c.getID(), r.getID(), copper.getID(), amount, "");
                return;
            } catch (Exception ex) {
                Logger.getLogger(merchant.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }

        } else {
            neededAmount = amount - copper.getQuantity();
            neededCoins = neededAmount / 10;
            if (neededAmount % 10 > 0) {
                neededCoins++;
            }
            if (silver.getQuantity() <= neededCoins) {
                copper.setQuantity(copper.getQuantity() + silver.getQuantity() * 10);
                c.removeItem(silver.getID());
                c.DoAction(new Action(ActionType.destroyitem, silver.getID()));
                c.addItem(copper.getID());
                copper.setRoom(c.getID());
                c.DoAction(new Action(ActionType.spawnitem, copper.getID()));
            } else {
                silver.setQuantity(silver.getQuantity() - neededCoins);
                copper.setQuantity(copper.getQuantity() + neededCoins * 10);
                c.addItem(copper.getID());
                copper.setRoom(c.getID());
                c.DoAction(new Action(ActionType.spawnitem, copper.getID()));
            }
            if (amount <= copper.getQuantity()) {
                try {
                    Main.game.doAction(ActionType.attemptgiveitem, c.getID(), r.getID(), copper.getID(), amount, "");
                    return;
                } catch (Exception ex) {
                    Logger.getLogger(merchant.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                }
            } else {
                neededAmount = amount - copper.getQuantity();
                neededCoins = neededAmount / 100;
                if (neededAmount % 100 > 0) {
                    neededCoins++;
                }
                if (gold.getQuantity() <= neededCoins) {
                    copper.setQuantity(copper.getQuantity() + gold.getQuantity() * 100);
                    c.removeItem(gold.getID());
                    c.DoAction(new Action(ActionType.destroyitem, gold.getID()));
                    c.addItem(copper.getID());
                    copper.setRoom(c.getID());
                    c.DoAction(new Action(ActionType.spawnitem, copper.getID()));
                } else {
                    gold.setQuantity(gold.getQuantity() - neededCoins);
                    copper.setQuantity(copper.getQuantity() + neededCoins * 100);
                    c.addItem(copper.getID());
                    copper.setRoom(c.getID());
                    c.DoAction(new Action(ActionType.spawnitem, copper.getID()));
                }
                if (amount <= copper.getQuantity()) {
                    try {
                        Main.game.doAction(ActionType.attemptgiveitem, c.getID(), r.getID(), copper.getID(), amount, "");
                        return;
                    } catch (Exception ex) {
                        Logger.getLogger(merchant.class.getName()).log(Level.SEVERE, null, ex);
                        return;
                    }
                } else {
                    neededAmount = amount - copper.getQuantity();
                    neededCoins = neededAmount / 1000;
                    if (neededAmount % 1000 > 0) {
                        neededCoins++;
                    }
                    if (plat.getQuantity() <= neededCoins) {
                        copper.setQuantity(copper.getQuantity() + plat.getQuantity() * 1000);
                        c.removeItem(plat.getID());
                        c.DoAction(new Action(ActionType.destroyitem, plat.getID()));
                        c.addItem(copper.getID());
                        copper.setRoom(c.getID());
                        c.DoAction(new Action(ActionType.spawnitem, copper.getID()));
                    } else {
                        plat.setQuantity(plat.getQuantity() - neededCoins);
                        copper.setQuantity(copper.getQuantity() + neededCoins * 1000);
                        c.addItem(copper.getID());
                        copper.setRoom(c.getID());
                        c.DoAction(new Action(ActionType.spawnitem, copper.getID()));
                    }
                    if (amount <= copper.getQuantity()) {
                        try {
                            Main.game.doAction(ActionType.attemptgiveitem, c.getID(), r.getID(), copper.getID(), amount, "");
                            return;
                        } catch (Exception ex) {
                            Logger.getLogger(merchant.class.getName()).log(Level.SEVERE, null, ex);
                            return;
                        }
                    }
                }
            }
        }
        if (copper.getQuantity() == 0) {
            c.removeItem(plat.getID());
            c.DoAction(new Action(ActionType.destroyitem, copper.getID()));
        }
    }

    private int findName(Set<Item> iteminventory, String newsearch) {
        for (Item i : iteminventory) {
            if (i.getName().equalsIgnoreCase(newsearch)) {
                return i.getID();










            }
        }
        for (Item i : iteminventory) {
            if (newsearch.length() > 2) {
                if (i.getName().toLowerCase().contains(newsearch.toLowerCase())) {
                    return i.getID();










                }
            }
        }
        return 0;










    }

    private boolean hasEnoughCurrency(MudCharacter c, int amount) {
        int total = 0;










        for (Item i : c.Items()) {
            if (i.getTemplateID() == 1 || i.getTemplateID() == 2 || i.getTemplateID() == 3 || i.getTemplateID() == 4) {
                total = i.getQuantity() * i.getAttribute(MudData.VALUE);










                break;










            }
        }
        if (total >= amount) {
            return true;










        }
        return false;










    }

    private Item seekItem(String itemname) {
        for (Item item : Inventory()) {
            if (item.getName().equalsIgnoreCase(itemname)) {
                return item;










            }
        }
        if (itemname.length() < 3) {
            return null;










        }
        for (Item item : Inventory()) {
            if (item.getName().toLowerCase().contains(itemname.toLowerCase())) {
                return item;










            }
        }
        return null;





    }
}
