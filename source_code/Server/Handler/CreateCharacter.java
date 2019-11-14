/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Handler;

import Entities.Account;
import Entities.MudCharacter;
import Entities.enums.AccessLevel;
import Logics.Logic;
import Mud.Mud;
import Databases.Group.DatabaseGroup;
import Mud.D_and_D_Mud.MudData;

/**
 *  This is the basic workhorse for creating a new character.  It first asks you
 * for a race, then sets up a first level fighter for you.
 *
 * Future verions will allow you to choose a class as well and set up skills,
 * feats, etc.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 *
 */
public class CreateCharacter extends Logic {

    static DatabaseGroup dbg = Mud.dbg;
    static public String listRases =
            "<#FFFFFF>--------------------------------------------------------------------------------\r\n"
            + "<#00FF00> Please Choose a Race For Your Character:\r\n"
            + "<#FFFFFF>--------------------------------------------------------------------------------\r\n"
            + "<$reset> 0 - Go Back\r\n"
            + "<$reset> 1 - Dragonborn\r\n"
            + "<$reset> 2 - Dwarf\r\n"
            + "<$reset> 3 - Eladrin\r\n"
            + "<$reset> 4 - Elf\r\n"
            + "<$reset> 5 - Half-Elf\r\n"
            + "<$reset> 6 - Halfling\r\n"
            + "<$reset> 7 - Human\r\n"
            + "<$reset> 8 - Tiefling\r\n"
           /* + "<$reset> 9 - Deva\r\n"
            + "<$reset> 10 - Gnome\r\n"
            + "<$reset> 11 - Goliath\r\n"
            + "<$reset> 12 - Half-Orc\r\n"
            + "<$reset> 13 - Shifter\r\n"
            + "<$reset> 14 - Githzerai\r\n"
            + "<$reset> 15 - Minotaur\r\n"
            + "<$reset> 16 - Shardmind\r\n"
            + "<$reset> 17 - Wilden\r\n"*/
            + "<#FFFFFF>--------------------------------------------------------------------------------\r\n"
            + "<#FFFFFF> Enter Choice: <$reset>";
    static public String listClasses =
            "<#FFFFFF>--------------------------------------------------------------------------------\r\n"
            + "<#00FF00> Please Choose a Race For Your Character:\r\n"
            + "<#FFFFFF>--------------------------------------------------------------------------------\r\n"
            + "<$reset> 0 - Go Back\r\n"
            + "<$reset> 1 - Fighter\r\n"
            + "<$reset> 2 - Cleric\r\n"
            + "<$reset> 3 - Wizard\r\n"
            + "<$reset> 4 - Thief\r\n"
            + "<#FFFFFF>--------------------------------------------------------------------------------\r\n"
            + "<#FFFFFF> Enter Choice: <$reset>";

    static public void setup(int id, int raceNum, int classNum) {
        MudCharacter player = dbg.characterDB.get(id);//BetterMUD.character(id);
        Account a = dbg.accountDB.get(player.getAccount());
        AccessLevel l = a.getAccesslevel();
        player.getAttributes().purge();
        player.addAttribute("str", 15);
        player.addAttribute("dex", 13);
        player.addAttribute("con", 14);
        player.addAttribute("int", 12);
        player.addAttribute("wis", 10);
        player.addAttribute("cha", 8);
        player.addAttribute(MudData.NEXT_LEVEL_XP, 1000);
        player.addAttribute(MudData.ENCUMBRANCE, 0);
        if (raceNum == 1) {
            player.addLogic(MudData.HUMAN);
        } else if (raceNum == 2) {
            player.addLogic(MudData.DWARF);
        } else if (raceNum == 3) {
            player.addLogic(MudData.ELF);
        }
        // defaults all characters to a fighter level 1
        player.addAttribute(MudData.AROMRCLASS, 10 + MudData.getBonus(player.getAttribute(MudData.DEX)));
        player.addAttribute(MudData.MAX_ENCUMBRANCE, calculateMaxEncumbrance(player.getAttribute(MudData.STR)));
        player.addAttribute(MudData.BASETOHIT, MudData.getBonus(player.getAttribute(MudData.STR)));
        player.addLogic(MudData.FIGHTER);
        if (l.ordinal() >= 0) {
            player.addCommand("action");
            player.addCommand("appraise");
            player.addCommand("arm");
            player.addCommand("attack");
            player.addCommand("breakattack");
            player.addCommand("buy");
            player.addCommand("cast");
            player.addCommand("chat");
            player.addCommand("commands");
            player.addCommand("disarm");
            player.addCommand("down");
            player.addCommand("drop");
            player.addCommand("east");
            player.addCommand("get");
            player.addCommand("give");
            player.addCommand("go");
            player.addCommand("inventory");
            player.addCommand("list");
            player.addCommand("look");
            player.addCommand("ne");
            player.addCommand("north");
            player.addCommand("northeast");
            player.addCommand("northwest");
            player.addCommand("nw");
            player.addCommand("pies");
            player.addCommand("quiet");
            player.addCommand("quit");
            player.addCommand("read");
            player.addCommand("receive");
            player.addCommand("say");
            player.addCommand("se");
            player.addCommand("south");
            player.addCommand("southeast");
            player.addCommand("southwest");
            player.addCommand("sw");
            player.addCommand("up");
            player.addCommand("use");
            player.addCommand("west");
            player.addCommand("wear");
            player.addCommand("remove");
            player.addCommand("bug");
            player.addCommand("outfit");
            //c.addCommand("cast)  //not ready yet.
        }
        if (l.ordinal() >= 2) {
            player.addCommand("announce");
            player.addCommand("bootoff");
        }
        if (l.ordinal() >= 3) {
            player.addCommand("addattribute");
            player.addCommand("addcommand");
            player.addCommand("addplayerlogic");
            player.addCommand("cleanup");
            player.addCommand("controlmob");
            player.addCommand("create");
            player.addCommand("delattribute");
            player.addCommand("delcommand");
            player.addCommand("delplayerlogic");
            player.addCommand("destroycharacter");
            player.addCommand("destroyitem");
            player.addCommand("disabledevice");
            player.addCommand("emulate");
            player.addCommand("loaddatabase");
            player.addCommand("loadscript");
            player.addCommand("modify");
            player.addCommand("reloadscript");
            player.addCommand("savedatabase");
            player.addCommand("shutdown");
            player.addCommand("spawnitem");
            player.addCommand("spawnmob");
            player.addCommand("spawnroom");
            player.addCommand("teleport");
            player.addCommand("visual");
        }
        player.setRoom(1007);
        player.setRegion(1000);
        player.setQuiet(true);
    }

    private static int calculateMaxEncumbrance(int attribute) {
        switch (attribute) {
            case 1:
                return 50 * 3;
            case 2:
                return 50 * 6;
            case 3:
                return 50 * 10;
            case 4:
                return 50 * 13;
            case 5:
                return 50 * 16;
            case 6:
                return 50 * 20;
            case 7:
                return 50 * 23;
            case 8:
                return 50 * 26;
            case 9:
                return 50 * 30;
            case 10:
                return 50 * 33;
            case 11:
                return 50 * 38;
            case 12:
                return 50 * 43;
            case 13:
                return 50 * 50;
            case 14:
                return 50 * 58;
            case 15:
                return 50 * 66;
            case 16:
                return 50 * 76;
            case 17:
                return 50 * 86;
            case 18:
                return 50 * 100;
            case 19:
                return 50 * 116;
            case 20:
                return 50 * 133;
            case 21:
                return 50 * 153;
            case 22:
                return 50 * 173;
            case 23:
                return 50 * 200;
            case 24:
                return 50 * 233;
            case 25:
                return 50 * 266;
            case 26:
                return 50 * 306;
            case 27:
                return 50 * 346;
            case 28:
                return 50 * 400;
            case 29:
                return 50 * 466;
            default:
                return 0;
        }
    }
}
