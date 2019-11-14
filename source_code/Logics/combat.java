package Logics;

import Actions.Action;
import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;
import Entities.Region;
import Entities.Room;
import Mud.D_and_D_Mud.MudData;
import Server.Main;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import utilities.Dice;

/**
 * This logic is needed for any mob to be able to be attacked or to attack.  It
 * currently uses mostly d20 system to hit and damage.  Needs further
 * modifications to be fully d20 system complainent.  Also determines if the
 * player/mob has enough xp to advance in level and currently calls the class
 * logic's level up action.
 *
 * If the player dies, he is now forced transported to the new "swirling light"
 * room.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 * Version 1.0.1
 * 1.  Added support throughout to ensure that "target" is not accessed if it is
 * "null" which happens sometimes if the "target" is removed from the mud and an
 * action is still pending on the "target".
 * 2.  Also changed the respawn hitpoints to "1".
 * 3.  Changed the room references to reflect the new numbering.
 * 4.  Fixed the issue that damage was coming out as a negitive number.  Now 
 * minium number is 1.
 * Version 1.0.0
 */
public class combat extends Logic {

    private MudCharacter me;
    private int targetID = 0;
    private Set<Integer> attackedlist;
    private long attacktime;
    private boolean incombat = false;

    public combat() {
        super("combat");
    }

    @Override
    public void init() {
        attackedlist = new ConcurrentSkipListSet<Integer>();
        targetID = 0;
        attackedlist.clear();
        attacktime = 5000L;//5 seconds
    }

    private void Break() {
        if (targetID == 0) {
            return;
        }
        MudCharacter target = dbg.characterDB.get(targetID);
        me.killHook(ActionType.doaction, "attack");
        if (target != null) {
            Main.game.addActionAbsolute(0, ActionType.vision, me.getRoom(), 0, 0, 0, me.getName() + " stops attacking " + target.getName() + "!!");
            target.DoAction(ActionType.doaction, 0, 0, me.getID(), 0, "brokeattack");
        } else {
            Main.game.addActionAbsolute(0, ActionType.vision, me.getRoom(), 0, 0, 0, me.getName() + " stops attacking!!");
        }
        targetID = 0;
        incombat = false;
    }

    @Override
    public int DoAction(Action action) {
        me = (MudCharacter) me();
        if (action.getType() == ActionType.query && action.getData().equals("canattack")) {
            return TRUE;
        }
        if (action.getType() == ActionType.modifyattribute && action.getData().equals("experience")) {
            me.DoAction(ActionType.announce, 0, 0, 0, 0, "<#00FFFF>You gain " + action.arg4 + " experience!");
            return DEFAULT;
        }
        if (action.getType() == ActionType.modifyattribute && action.getData().equals("maxhitpoints")) {
            if (me.getAttribute("hitpoints") > me.getAttribute("maxhitpoints")) {
                me.setAttribute("hitpoints", me.getAttribute("maxhitpoints"));
            }
            return DEFAULT;
        }
        if (action.getType() == ActionType.doaction && action.getData().equals("attacked")) {
            attackedlist.add(action.arg3);
            if (targetID == 0) {
                me().DoAction(ActionType.doaction, 0, 0, action.arg3, 0, "initattack");
            }
            return DEFAULT;
        }
        if (action.getType() == ActionType.doaction && action.getData().equals("brokeattack")) {

            attackedlist.remove(action.arg3);
            incombat = false;
            return DEFAULT;
        }
        if (action.getType() == ActionType.doaction && action.getData().equals("initattack")) {
            if (action.arg3 == me.getID() || incombat) {
                return DEFAULT;
            } else {
                Main.game.addActionAbsolute(0, ActionType.doaction, 0, me.getID(), 0, 0, "attack");
            }
            targetID = action.arg3;
            MudCharacter target = dbg.characterDB.get(targetID);
            target.DoAction(ActionType.doaction, 0, 0, me.getID(), 0, "attacked");
            Main.game.addActionAbsolute(0, ActionType.vision, me.getRoom(), 0, 0, 0, me.getName() + " begins attacking " + target.getName() + "!!");
            incombat = true;
            return DEFAULT;
        }
        if (action.getType() == ActionType.doaction && action.getData().equals("breakattack")) {
            Break();
            return DEFAULT;
        }
        if (action.getType() == ActionType.leaveroom) {
            if (action.arg1 == targetID || action.arg1 == me.getID()) {
                Break();
            }
            return DEFAULT;
        }
        if (action.getType() == ActionType.doaction && action.getData().equalsIgnoreCase("killed")) {
            Break();
            return DEFAULT;
        }
        if (action.getType() == ActionType.modifyattribute && action.getData().equals("hitpoints")) {
            if (action.arg3 <= 0) {
                me.DoAction(ActionType.doaction, 0, 0, 0, 0, "died");
                incombat = false;
            }
            return DEFAULT;
        }
        if (action.getType() == ActionType.doaction && action.getData().equalsIgnoreCase("died")) {
            Break();
            Main.game.addActionAbsolute(0, ActionType.vision, me.getRoom(), 0, 0, 0, me.getName() + " dies!!!");
            int experience = me.getAttribute("giveexperience");
            if (attackedlist.size() > 0) {
                experience = experience / attackedlist.size();
            }
            for (int k : attackedlist) {
                MudCharacter c = dbg.characterDB.get(k);
                c.DoAction(ActionType.doaction, 0, 0, me.getID(), 0, "killed");
                Main.game.doAction(ActionType.modifyattribute, 0, k, c.getAttribute("experience") + experience, experience, "experience");
                if (c.getAttribute(MudData.EXPERIENCE) >= c.getAttribute(MudData.NEXT_LEVEL_XP)) {
                    c.DoAction(new Action(ActionType.doaction, 0, 0, 0, 0, "levelup"));
                }
            }
            attackedlist.clear();
            for (int item : me.getItems()) {
                Main.game.doAction(ActionType.attemptdropitem, me.getID(), item, dbg.itemDB.get(item).getQuantity(), 0, "");
            }
            if (me.isPlayer() == false) {
                Main.game.addActionAbsolute(0, ActionType.destroycharacter, me.getID(), 0, 0, 0, "");
            } else {
                me.setAttribute("hitpoints", 1);
                Room r = me.Room();
                if (r.DoAction(ActionType.doaction, me.getID(), 0, 0, 0, "deathtransport") == 1) {
                    return DEFAULT;
                }
                Region reg = me.Region();
                if (reg.DoAction(ActionType.doaction, me.getID(), 0, 0, 0, "deathtransport") == 1) {
                    return DEFAULT;
                }
                if (me.DoAction(ActionType.doaction, me.getID(), 0, 0, 0, "deathtransport") == 1) {
                    return DEFAULT;
                }
                Main.game.doAction(ActionType.forcetransport, me.getID(), 1007, 0, 0, "");
            }
            return DEFAULT;
        }
        if (action.getType() == ActionType.doaction && action.getData().equals("attack")) {
            MudCharacter target = dbg.characterDB.get(targetID);
            if (target == null || incombat == false) {
                return DEFAULT;
            }
            Main.game.addActionRelative(attacktime, ActionType.doaction, 0, me.getID(), 0, 0, "attack");
            Item weapon;
            boolean defaultweaponcreated = false;
            if (me().getAttribute("weapon") == 0) {
                weapon = dbg.itemDB.generate(me.getAttribute("defaultweapon"));
                defaultweaponcreated = true;
            } else {
                weapon = dbg.itemDB.get(me.getAttribute("weapon"));
            }
            int armorClass = target.getAttribute("armorclass");
            int hitChance = this.me().getAttribute("basetohit");
            hitChance += target.DoAction(ActionType.query, me.getID(), target.getID(), 0, 0, "getaccuracydelta");
            hitChance += me.DoAction(ActionType.query, me.getID(), target.getID(), 0, 0, "getaccuracydelta");
            hitChance += Dice.Roll_d20();
            if (hitChance <= armorClass) {
                Main.game.addActionAbsolute(0, ActionType.vision, me.getRoom(), 0, 0, 0, me.getName() + " swings at " + target.getName() + " with " + weapon.getName() + ", but misses!");
                return DEFAULT;
            }
            int damage = Dice.Roll_Multi(weapon.getAttribute("damagedie"), weapon.getAttribute("damagedice")) + MudData.getBonus(me().getAttribute(MudData.STR));
            if (damage < 1) {
                damage = 1;
            }
            Main.game.doAction(ActionType.vision, me.getRoom(), 0, 0, 0, "<#FF0000>" + me.getName() + " hits " + target.getName() + " with " + weapon.getName() + " for " + damage + " damage!");
            Main.game.doAction(ActionType.modifyattribute, 0, target.getID(), target.getAttribute("hitpoints") - damage, damage, "hitpoints");
            if (defaultweaponcreated) {
                dbg.itemDB.erase(weapon.getID());
            }
        }
        return DEFAULT;
    }
}
