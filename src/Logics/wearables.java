/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logics;

import Actions.Action;
import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;
import Mud.D_and_D_Mud.MudData;
import Server.Main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This logic is used on all items that the mobs may wear.  It handles all the
 * queries if an item may be worn, as well as handling the accual putting on and
 * then removing the item from the character.
 *
 * Future support will be added for different body parts.
 *
 * @author Brion Lang
 *  Date: 08 Dec 2009
 *
 *  Version 1.0.0
 */
public class wearables extends Logic {

    private int offHand = 0;
    private int primaryHand = 0;
    private int head = 0;
    private int legs = 0;
    private int chest = 0;
    private int arms = 0;
    private int hands = 0;
    private int rightRing = 0;
    private int leftRing = 0;

    public wearables() {
        super("wearables");
    }

    /**
     * Must always have this method
     *
     * @param action
     * @return resaults of trying to do Action
     */
    @Override
    public int DoAction(Action action) {
        if (action.getType() == ActionType.query && action.getData().equals("canwear")) {
            Item item = Mud.Mud.dbg.itemDB.get(action.arg1);
            if (item.getAttribute("wearable") == 1 || item.getAttribute("arms") == 1) {
                return TRUE;
            }
            return FALSE;
        }
        if (action.getType() == ActionType.doaction && action.getData().equals("wear")) {
            Item item = Mud.Mud.dbg.itemDB.get(action.arg3);
            if (item.getAttribute("shield") == 1) {
                disrobe(2);
                wear(item);
            } else if (item.getAttribute("armor") == 1) {
                disrobe(1);
                wear(item);
            } else if (item.getAttribute("weapon") == 1) {
                wear(item);
            }
        }
        if (action.getType() == ActionType.doaction && action.getData().equals("unwear")) {
            disrobe(action.arg3);
        }
        if (action.getType() == ActionType.dropitem && action.arg1 == me().getID()) {
            lose(action.arg2);
        }
        if (action.getType() == ActionType.giveitem && action.arg1 == me().getID()) {
            lose(action.arg3);
        }
        if (action.getType() == ActionType.destroyitem) {
            lose(action.arg1);
        }
        return DEFAULT;
    }

    void disrobe(int itemtype) {
        MudCharacter me = (MudCharacter) me();
        if (itemtype == 1) {
            if (me.getAttribute("armor") != 0) {
                Item armor = Mud.Mud.dbg.itemDB.get(me.getAttribute("armor"));
                me.setAttribute("armor", 0);
                chest = 0;
                me.setAttribute(MudData.AROMRCLASS, me.getAttribute(MudData.AROMRCLASS) - armor.getAttribute("armorbonus"));
                if (me.DoAction(ActionType.query, 0, 0, 0, 0, "cantbeseen") == Logic.FALSE) {
                    Main.game.addActionAbsolute(0, ActionType.vision, me.getRoom(), 0, 0, 0, me.getName() + " removes " + armor.getName() + ".");
                } else {
                    me.DoAction(ActionType.announce, me.getRoom(), 0, 0, 0, me.getName() + " removes " + armor.getName() + "!");
                }
            }
        } else if (itemtype == 2) {
            if (me.getAttribute("shield") != 0) {
                Item shield = Mud.Mud.dbg.itemDB.get(me.getAttribute("shield"));
                me.setAttribute("shield", 0);
                offHand = 0;
                me.setAttribute(MudData.AROMRCLASS, me.getAttribute(MudData.AROMRCLASS) - shield.getAttribute("armorbonus"));
                if (me.DoAction(ActionType.query, 0, 0, 0, 0, "cantbeseen") == Logic.FALSE) {
                    Main.game.addActionAbsolute(0, ActionType.vision, me.getRoom(), 0, 0, 0, me.getName() + " removes " + shield.getName() + ".");
                } else {
                    me.DoAction(ActionType.announce, me.getRoom(), 0, 0, 0, me.getName() + " removes " + shield.getName() + "!");
                }
            }
        } else if (itemtype == 3) {
            if (me.getAttribute("weapon") != 0) {
                Item weapon = Mud.Mud.dbg.itemDB.get(me.getAttribute("weapon"));
                if (weapon.getAttribute("numberofhands") == 2) {
                    offHand = 0;
                    primaryHand = 0;
                } else {
                    primaryHand = 0;
                }
            }
        }
    }

    private void wear(Item item) {
        MudCharacter me = (MudCharacter) me();
        if ((item.getAttribute("wearable") == 1) || (item.getAttribute("arms") == 1)) {
            if (item.getAttribute("shield") == 1) {
                offHand = item.getID();
                me.setAttribute("shield", item.getID());
                me.setAttribute(MudData.AROMRCLASS, me.getAttribute(MudData.AROMRCLASS) + item.getAttribute("armorbonus"));
                if (me.DoAction(ActionType.query, 0, 0, 0, 0, "cantbeseen") == Logic.FALSE) {
                    Main.game.addActionAbsolute(0, ActionType.vision, me.getRoom(), 0, 0, 0, me.getName() + " puts on " + item.getName() + "!");
                } else {
                    me.DoAction(ActionType.announce, me.getRoom(), 0, 0, 0, me.getName() + " puts on " + item.getName() + "!");
                }

            } else if (item.getAttribute("armor") == 1) {
                chest = item.getID();
                me.setAttribute("armor", item.getID());
                me.setAttribute(MudData.AROMRCLASS, me.getAttribute(MudData.AROMRCLASS) + item.getAttribute("armorbonus"));
                if (me.DoAction(ActionType.query, 0, 0, 0, 0, "cantbeseen") == Logic.FALSE) {
                    Main.game.addActionAbsolute(0, ActionType.vision, me.getRoom(), 0, 0, 0, me.getName() + " puts on " + item.getName() + "!");
                } else {
                    me.DoAction(ActionType.announce, me.getRoom(), 0, 0, 0, me.getName() + " puts on " + item.getName() + "!");
                }

            } else if (item.getAttribute("weapon") == 1) {
                if (item.getAttribute(MudData.NUMBER_OF_HANDS) == 2) {
                    if (offHand != 0) {
                        disrobe(2);
                        me().DoAction(ActionType.doaction, 0, 0, item.getID(), 0, "arm");
                        offHand = item.getID();
                        primaryHand = offHand;
                    }
                } else {
                    me().DoAction(ActionType.doaction, 0, 0, item.getID(), 0, "arm");
                    primaryHand = item.getID();
                }
            }
        }
    }

    private void lose(int itemid) {
        if (me().getAttribute("armor") == itemid) {
            disrobe(1);
        }
        if (me().getAttribute("shield") == itemid) {
            disrobe(2);
        }
        if (me().getAttribute("weapon") == itemid) {
            disrobe(3);
            me().DoAction(ActionType.doaction, 0, 0, 1, 0, "disarm");
        }

    }

    @Override
    public void Load(BufferedReader reader) throws IOException {
        // default to empty data loading, by chewing up the [DATA] and
        // [/DATA] tags.
        reader.readLine();
        offHand = Integer.parseInt(reader.readLine());
        primaryHand = Integer.parseInt(reader.readLine());
        head = Integer.parseInt(reader.readLine());
        legs = Integer.parseInt(reader.readLine());
        chest = Integer.parseInt(reader.readLine());
        arms = Integer.parseInt(reader.readLine());
        hands = Integer.parseInt(reader.readLine());
        rightRing = Integer.parseInt(reader.readLine());
        leftRing = Integer.parseInt(reader.readLine());
        reader.readLine();
    }

    @Override
    public void Save(PrintWriter writer) {
        // default to empty data saving
        writer.println("[DATA]");
        writer.println(offHand);
        writer.println(primaryHand);
        writer.println(head);
        writer.println(legs);
        writer.println(chest);
        writer.println(arms);
        writer.println(hands);
        writer.println(rightRing);
        writer.println(leftRing);
        writer.println("[/DATA]]");
    }

    public String Head() {
        if (head == 0) {
            return "Nothng";
        } else {
            return dbg.itemDB.get(head).getName();
        }

    }

    public String Chest() {
        if (chest == 0) {
            return "Nothng";
        } else {
            return dbg.itemDB.get(chest).getName();
        }

    }

    public String Arms() {
        if (arms == 0) {
            return "Nothng";
        } else {
            return dbg.itemDB.get(arms).getName();
        }

    }

    public String Hands() {
        if (hands == 0) {
            return "Nothng";
        } else {
            return dbg.itemDB.get(hands).getName();
        }

    }

    public String PrimaryHand() {
        if (primaryHand == 0) {
            return "Nothng";
        } else {
            return dbg.itemDB.get(primaryHand).getName();
        }

    }

    public String OffHand() {
        if (offHand == 0) {
            return "Nothng";
        } else {
            return dbg.itemDB.get(offHand).getName();
        }

    }

    public String Legs() {
        if (legs == 0) {
            return "Nothng";
        } else {
            return dbg.itemDB.get(legs).getName();
        }

    }

    public String RightRing() {
        if (rightRing == 0) {
            return "Nothng";
        } else {
            return dbg.itemDB.get(rightRing).getName();
        }

    }

    public String LeftRing() {
        if (leftRing == 0) {
            return "Nothng";
        } else {
            return dbg.itemDB.get(leftRing).getName();
        }
    }
}
