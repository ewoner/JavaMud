package Mud.D_and_D_Mud;

/**
 * Holder for the various strings used mainly with ATTRIBUTES throughout the game.
 *
 * @author Brion Lang
 *  Date: 08 Dec 2009
 *
 *  Version 1.0.0

 */
public class MudData {

    //Classes
    /**
     * Numer of Barbarian Levels
     */
    public static final String BARBARIAN = "barbarian";
    /**
     * A number representive of the barbarian class
     */
    public static final int BARBARIAN_NUM = 1;
    /**
     * Numer of Bard Levels
     */
    public static final String BARD = "bard";
    /**
     * A number representive of the bard class
     */
    public static final int BARD_NUM = 2;
    /**
     * Numer of Cleric Levels
     */
    public static final String CLERIC = "cleric";
    /**
     * A number representive of the cleric class
     */
    public static final int CLERIC_NUM = 3;
    /**
     * Numer of Druid Levels
     */
    public static final String DRUID = "druid";
    /**
     * A number representive of the druid class
     */
    public static final int DRUID_NUM = 4;
    /**
     * Numer of Fighter Levels
     */
    public static final String FIGHTER = "fighter";
    /**
     * A number representive of the fighter class
     */
    public static final int FIGHTER_NUM = 5;
    /**
     * Numer of Monk Levels
     */
    public static final String MOMK = "monk";
    /**
     * A number representive of the monk class
     */
    public static final int MOMK_NUM = 6;
    /**
     * Numer of Paladin Levels
     */
    public static final String PALADIN = "paladin";
    /**
     * A number representive of the paladin class
     */
    public static final int PALADIN_NUM = 7;
    /**
     * Numer of Ranger Levels
     */
    public static final String RANGER = "ranger";
    /**
     * A number representive of the ranger class
     */
    public static final int RANGER_NUM = 8;
    /**
     * Numer of Rogue Levels
     */
    public static final String ROGUE = "rogue";
    /**
     * A number representive of the rouge class
     */
    public static final int ROGUE_NUM = 9;
    /**
     * Numer of Sorcerer Levels
     */
    public static final String SORCERER = "sorcerer";
    /**
     * A number representive of the sorcerer class
     */
    public static final int SORCERER_NUM = 10;
    /**
     * Numer of Wizard Levels
     */
    public static final String WIZARD = "wizard";
    /**
     * A number representive of the wizard class
     */
    public static final int WIZARD_NUM = 11;
    //others
    /**
     * Current Hit points of the character
     */
    public static final String HITPOINTS = "hitpoints";
    /**
     * Max hit points of the character
     */
    public static final String MAX_HITPOINTS = "maxhitpoints";
    /**
     * Current Experience of the character
     */
    public static final String EXPERIENCE = "experience";
    /**
     * The item template number of the default weapon of the character
     */
    public static final String DEFAULT_WEAPON = "defaultweapon";
    /**
     * The character level (total of all class leveles and creature levels)
     */
    public static final String LEVEL = "level";
    //Encumbrance
    /**
     * Current weight carrired in coins (50 coins = 1 poound)
     */
    public static final String ENCUMBRANCE = "encumbrance";
    /**
     * Max weight the charactger can carry in coins (50 coins = 1 poound)
     */
    public static final String MAX_ENCUMBRANCE = "maxencumbrance";
    //items
    /**
     * The value of the itme in CP
     */
    public static final String VALUE = "value";
    /**
     * The weight of the item in coins (50 coins = 1 poound)
     */
    public static final String WEIGHT = "weight";
    /**
     * 1 = The item is a form of Currency, else not money.
     */
    public static final String CURRENCY = "currency";
    /**
     * 1 = Item can be armed/worn.
     */
    public static final String ARMS = "arms";
    /**
     * 1 = Item is a weapon
     */
    public static final String WEAPON = "weapon";
    /**
     * 1 = Item is a form of armor
     */
    public static final String ARMOR = "armor";
    //armor
    /**
     * 1 = Item is a form of light armor
     */
    public static final String LIGHT_ARMOR = "lightarmor";
    /**
     * 1 = Item is a form of medium armor
     */
    public static final String MEDIUM_ARMOR = "mediumarmor";
    /**
     * 1 = Item is a form of heavy armor
     */
    public static final String HEAVY_ARMOR = "heavyarmor";
    /**
     * 1 = Item is a form of shield
     */
    public static final String SHIELD = "shield";
    /**
     * 1 = Item is a form of gauntlet
     */
    public static final String GAUNTLET = "gauntlet";
    /**
     * The items bonus to armor class
     */
    public static final String ARMOR_BOMUS = "armorbonus";
    /**
     * The armors max bonus posible from DEX while wear this armor.
     */
    public static final String MAX_DEX_BONUS = "maxdexbonus";
    /**
     * The armor's penalty to skill checks (stacks with other armor)
     */
    public static final String ARMOR_CHECK_PENALTY = "armorcheckpenalty";
    /**
     * The armor's chance to cause spell failure (stacks with other aromr)
     */
    public static final String SPELL_FAILURE = "spellfailure";
    //weapons
    /**
     * The weapon is a ammunition for a projectile weapon
     */
    public static final String AMMUNITION = "ammunition";
    /**
     * The weapon is a thrown weapon
     */
    public static final String THROWN_WEAPON = "thrownweapon";
    /**
     * The weapon is a melee weapon
     */
    public static final String MELEE_WEAPON = "meleeweapon";
    /**
     * The weapon is a uses projectiles/ammunition weapon.  The number returned is the ammunition's ID number.
     */
    public static final String PROJECTILE_WEAPON = "projectileweapon";
    /**
     * The weapon is a simple weapon
     */
    public static final String SIMPLE_WEAPON = "simpleweapon";
    /**
     * The weapon is a martial weapon
     */
    public static final String MARTIAL_WEAPON = "martialweapon";
    /**
     * The weapon is an exotic weapon
     */
    public static final String EXOTIC_WEAPON = "exoticweapon";
    /**
     * The weapons die used for damage.
     */
    public static final String DAMAGE_DIE = "damagedie";
    /**
     * The number of die used for damage (normally 1)
     */
    public static final String DAMAGE_DICE = "damagedice";
    /**
     * The lowest number in the weapons critical range (from 20)
     */
    public static final String CRITICAL_RANGE = "criticalrange";
    /**
     * The multipier for damage done if a critical hit is scored
     */
    public static final String CRITICAL_MODIFIER = "criticalmodifier";
    /**
     * The range increment of a weapon in feet
     */
    public static final String RANGE_INCREMENT = "rangeincrement";
    /**
     * 1 = this weapons deals bludgeoning damage
     */
    public static final String BLUDGEONING = "bludgeoning";
    /**
     * 1 = this weapon deals slashing damage
     */
    public static final String SLASHING = "slashing";
    /**
     * 1 = this weapon deals piercing damage
     */
    public static final String PIERCING = "piercing";
    /**
     * 1 or 2 = number of hands to hold this weapon (if creature and weapon of same size)
     */
    public static final String NUMBER_OF_HANDS = "numberofhands";
    /**
     * 1 = this weapon is a "double weapon"
     */
    public static final String DOUBLE_WEAPON = "doubleweapon";
    /**
     * Damage die for the "double weapon"
     */
    public static final String DOUBLE_WEAPON_DAMAGE_DIE = "doubleweapondamagedie";
    /**
     * Number of Dice for the "double weapon"
     */
    public static final String DOUBLE_WEAPON_DAMAGE_DICE = "doubleweapondamagedice";
    /**
     *  1 = this weapon is a reach weapon
     */
    public static final String REACH_WEAPON = "reachweapon";
    /**
     * 1 = this weapon deals only non lethal damage
     */
    public static final String NON_LETHAL_WEAPON = "nonlethalweapon";
    /**
     * this weapon is concidered a light weapon
     */
    public static final String LIGHT_WEAPON = "lightweapon";
    /**
     * This weapon is concidered as an "unarmed" attack (gautlets used to punch)
     */
    public static final String UNARMED_WEAPON = "unarmedweapon";
    //races
    /**
     * 1 = Race is human
     */
    public static final String HUMAN = "human";
    /**
     * 1 = Race is Dwarf
     */
    public static final String DWARF = "dwarf";
    /**
     * 1 = Race is elf
     */
    public static final String ELF = "elf";
    /**
     * 1 = Race is gnome
     */
    public static final String GNOME = "gnome";
    /**
     * 1 = Race is halfling
     */
    public static final String HALFLING = "halfling";
    /**
     * 1 = Race is half-elf
     */
    public static final String HALF_ELF = "halfelf";
    /**
     * 1 = Race is half-orc
     */
    public static final String HALF_ORC = "halforc";
    //sizes
    /**
     * Size of chreateres or items
     */
    public static final String SIZE = "size";
    /**
     * 1 = creature is of a size LONG opposed to the normal TALL
     */
    public static final String SIZE_LONG = "sizelong";
    /**
     * Number of ranks in hide
     */
    public static final String HIDESKILL = "hideranks";
    /**
     * size is fine
     */
    public static final int FINE = 0;
    /**
     * size is diminutive
     */
    public static final int DIMINUTIVE = 1;
    /**
     * size is tiny
     */
    public static final int TINY = 2;
    /**
     * size is small
     */
    public static final int SMALL = 3;
    /**
     * size is medium
     */
    public static final int MEDIUM = 4;
    /**
     * size is large
     */
    public static final int LARGE = 5;
    /**
     * size is huge
     */
    public static final int HUGE = 6;
    /**
     * size is gargantuan
     */
    public static final int GARGANTUAN = 7;
    /**
     * size is colossal
     */
    public static final int COLOSSAL = 8;
    /**
     * Appraise skill ranks
     */
    public static final String APPRAISESKILL = "appraiseranks";
    /**
     * 1 = Item is rare
     */
    public static final String RARE = "rare";
    /**
     * 1 = Item is exotic
     */
    public static final String EXOTIC = "exotic";
    /**
     * 1 = item unique
     */
    public static final String UNIQUE = "unique";
    /**
     * 1 = Mob is trying to hide
     */
    public static final String HIDING = "hiding";
    /**
     * The natural str of the mob
     */
    public static final String STR_NATURAL = "str_natural";
    /**
     * The current str of the mob
     */
    public static final String STR = "str";
    /**
     * The natural dex of the mob
     */
    public static final String DEX_NATURAL = "dex_natural";
    /**
     * The current dex of the mob
     */
    public static final String DEX = "dex";
    /**
     * The natural con of the mob
     */
    public static final String CON_NATURAL = "con_natural";
    /**
     * The current con of the mob
     */
    public static final String CON = "con";
    /**
     * The natural int of the mob
     */
    public static final String INT_NATURAL = "int_natural";
    /**
     * The current int of the mob
     */
    public static final String INT = "int";
    /**
     * The natural wis of the mob
     */
    public static final String WIS_NATURAL = "wis_natural";
    /**
     * The current wis of the mob
     */
    public static final String WIS = "wis";
    /**
     * The natural cha of the mob
     */
    public static final String CHA_NATURAL = "cha_natural";
    /**
     * The current cha of the mob
     */
    public static final String CHA = "cha";
    /**
     *
     */
    public static String SPEED = "speed";
    /**
     *
     */
    public static String FAVOREDCLASS = "favoredclass";
    /**
     * Returns 1 if the MOB is imune to magical sleeps
     */
    public static String IMMUNITYTOSLEEP = "immunitytosleep";
    /**
     * Returns 1 if the MOB has low light vision
     */
    public static String LOWLIGHTVISION = "lowlightvision";
    /**
     * Returns 1 if the MOB has low light vision
     */
    public static String DARKVISION = "darkvision";
    /**
     * Returns the current characters overall level
     */
    public static String CHARARTER_LEVEL = "characterlevel";
    /**
     * Returns the total xp needed for the next character level
     */
    public static String NEXT_LEVEL_XP = "nextlevel";
    /**
     * Returns the mobs current total base to hit
     */
    public static String BASETOHIT = "basetohit";
    /**
     * Returns the mobs current armorclass
     */
    public static String AROMRCLASS = "armorclass";
    public static String DRAGONBORN = "dragonborn";
    public static String LANGUAGE_COMMON = "commonlanguage";
    public static String LANGUAGE_DRACONIC = "draconiclanguage";
    public static String LANGUAGE_DWARVEN = "dwarvenlanguage";
    public static String ELADRIN = "eladrin";
    public static String LANGUAGE_ELVEN = "elvenlanguage";
    public static Object FEYCREATURE = "feycreature";
    public static String HALFELF = "halfelf";
    public static String TIEFLING = "tiefling";

    /**
     * Returns an ability's bonus based on its score.
     *
     * @param stat The score of the attribute
     * @return the bonus of the attribute
     */
    public static int getBonus(int stat) {
        return (int) (Math.floor(stat / 2) - 5);
    }
}
