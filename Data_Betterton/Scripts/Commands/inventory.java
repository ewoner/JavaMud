package Commands;

import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;
import Mud.D_and_D_Mud.MudData;

/**
 * This command shows what the player is currently carrying.  It also currently
 * shows the combat data for the player.  A future command ("score" maybe) will
 * be used to handle that data at a later time.
 *
 * @author Brion Lang
 * Date: 17 Jan 2009
 *
 * Version 1.1.1
 * 1. Modified the last line of the print out to "reset" the font.
 * 2. Fixed a formatting error on the class, xp, and hitpoint variables.
 * Version 1.1.0
 * 1. Added combat data to the commands
 * Version 1.0.0
 */
public class inventory extends Command {

    public inventory(MudCharacter mob) {
        super(mob, "inventory", "\"inventory\"",
                "Lists items in your inventory");
    }

    @Override
    public void Execute(String parameters) {
        String string = "<#FFFFFF>--------------------------------------------------------------------------------\r\n";
        string += "<#00FF00> Your Inventory\r\n";
        string += "<#FFFFFF>--------------------------------------------------------------------------------\r\n";
        string += "<$reset> ";
        if (Mob().getItems().size() == 0) {
            string += "NOTHING!!!\r\n";
        } else {
            for (Item item : Mob().Items()) {
                string += item.getName();
                string += "\r\n ";
            }
        }
        string += "\r\n<#FFFFFF>--------------------------------------------------------------------------------\r\n";
        string += "<#FFFFFF> Weapon:       <$reset>";
        Item item;
        boolean defaultweaponcreated = false;
        if (Mob().getAttribute("weapon") == 0) {
            item = Mud.Mud.dbg.itemDB.generate(Mob().getAttribute("defaultweapon"));
            defaultweaponcreated = true;
        } else {
            item = Mud.Mud.dbg.itemDB.get(Mob().getAttribute("weapon"));
        }
        int dice = item.getAttribute("damagedice");
        int die = item.getAttribute("damagedie");
        int bonus = MudData.getBonus(Mob().getAttribute(MudData.STR));
        String damagestring = "("+ (1*dice+bonus)+"-"+(die*dice+bonus)+")" ;
        string += item.getName() + " " + damagestring + "\r\n";
        string += "<#FFFFFF> Armor:        <$reset>";
        Item armor;
        armor = Mud.Mud.dbg.itemDB.get(Mob().getAttribute("armor"));
        if (armor != null) {
            string += armor.getName() + "\r\n";
        } else {
            string += "None" + "\r\n";
        }
        string += "<#FFFFFF> Total Weight: <$reset>" + Mob().getAttribute("encumbrance") + "\r\n";
        string += "<#FFFFFF> Max Weight:   <$reset>" + Mob().getAttribute("maxencumbrance") + "\r\n";
        string += "<#FFFFFF>--------------------------------------------------------------------------------\r\n";
        string += "<#FFFFFF> STR: <$reset>" + Mob().getAttribute(MudData.STR) + "<#FFFFFF>      INT: <$reset>" + Mob().getAttribute(MudData.INT) + "\r\n";
        string += "<#FFFFFF> DEX: <$reset>" + Mob().getAttribute(MudData.DEX) + "<#FFFFFF>      WIS: <$reset>" + Mob().getAttribute(MudData.WIS) + "\r\n";
        string += "<#FFFFFF> CON: <$reset>" + Mob().getAttribute(MudData.CON) + "<#FFFFFF>      CHA: <$reset>" + Mob().getAttribute(MudData.CHA) + "\r\n";
        string += "<#FFFFFF> To Hit Bonus:     <$reset>" + Mob().getAttribute(MudData.BASETOHIT) + "<#FFFFFF> Armor Class:      <$reset>" + Mob().getAttribute(MudData.AROMRCLASS) + "\r\n";
        string += "<#FFFFFF>--------------------------------------------------------------------------------\r\n";
        string += "<#FFFFFF> Fighter:           <$reset>" + Mob().getAttribute(MudData.FIGHTER) + "\r\n";
        string += "<#FFFFFF> Experience:        <$reset>" + Mob().getAttribute(MudData.EXPERIENCE) + "<#FFFFFF> Till Next Level:  <$reset>" + Mob().getAttribute(MudData.NEXT_LEVEL_XP) + "\r\n";
        string += "<#FFFFFF> Hit Points:        <$reset>" + Mob().getAttribute("hitpoints") + "<#FFFFFF> Max Hit Points:   <$reset>" + Mob().getAttribute("maxhitpoints") + "\r\n";

        string += "<#FFFFFF>--------------------------------------------------------------------------------<$reset>\r\n";
        Mob().DoAction(ActionType.announce, 0, 0, 0, 0, string);
        if (defaultweaponcreated) {
            Mud.Mud.dbg.itemDB.erase(item.getID());
        }




    }
}
