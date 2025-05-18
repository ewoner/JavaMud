package Logics;

import Actions.Action;
import Databank.DataBank;
import Databases.Group.DatabaseGroup;
import Entities.bases.EntityWithData_Logic;
import Entities.enums.EntityType;
import Mud.Mud;
import Scripts.Script;

/**
 * The basic Logic Module of the game.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 * Version 1.1.0
 * 1. Removed the "Logic()" constructor.
 * Version 1.0.0
 */
abstract public class Logic extends Script {

    /**
     * Used to answer queries.
     */
    static final public int TRUE = 1;
    /**
     * Used to answer queries.
     */
    static final public int FALSE = 0;
    /**
     * Used to answer other actions/logics (cantreceive)
     */
    static final public int NO = 1;
    /**
     * Used to answer other actions/logics (cantreceive)
     */
    static final public int YES = 0;
    /**
     * The default return from a logic module.
     */
    static final public int DEFAULT = 0;
    private String name;
    private boolean loadable = true;
    private int entityID;
    private EntityType entitytype;
    private DataBank<Integer> bank = new DataBank<Integer>();
    /**
     * Wrapper for the Mud's Database Group field.
     */
    protected DatabaseGroup dbg = Mud.dbg;

    /**
     * Used for out of game Logic moudels, namely Handlers.
     *
     * DO NOT USE FOR IN GAME LOGICE MODULES.  It will cause unprediciable
     * resaults.
     */
    public Logic() {
        this(null);
    }// </editor-fold>

    /**
     * Creates a new Logic Module with a name.
     * @param name
     */
    public Logic(String name) {
        this.name = name;
        setScriptName();
    }

    /**
     * By default, the Handlers are Logic Modules.  This method checks to make
     * sure a Module can be saved prior to saving the module.  By default this
     * is set to true.  Only Classes that can not be save have to override this
     * method.
     *
     * @return if the logic can be saved (TRUE)
     */
    public boolean canSave() {
        return true;
    }

    /**
     * Gets the value of an atrribute from the Module.
     *
     * @param attribute Atrribute
     * @return Value of Atrribte.
     */
    public int getAttribute(String attribute) {
        return bank.get(attribute);
    }

    /**
     * Sets the vaule of an atrribute of the Module.
     *
     * @param attribute
     * @param value
     */
    public void setAttribute(String attribute, int value) {
        bank.set(attribute, value);
    }

    /**
     * Process an action to the Logic Module.  This method need to be overwrote
     * in sub0classes of logic.  
     * 
     * @param action Action to process.
     * @return  Loigc.DEFAULT=0
     */
    public int DoAction(Action action) {
        return DEFAULT;
    }

    /**
     * Sets the Logics Entity ID.
     * @param id ID of the Entity this module belongs to
     */
    public void setEntityID(int id) {
        entityID = id;
    }

    /**
     * Sets the Type of Entity that this logic Module belongs to
     *
     * @param type Entity's type.
     */
    public void setEntityType(EntityType type) {
        this.entitytype = type;
    }

    @Override
    public void setScriptName() {
        String className = this.getClass().getName();
        className = className.substring(className.indexOf('.') + 1);
        scriptName = className;
    }

    /**
     * Gets the Logic Module's name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the Logic modules Name
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sees if the Logic Module is loadable as a script.  All but handlers are
     * loadable by default.
     * 
     * @return the loadable
     */
    public boolean isLoadable() {
        return loadable;
    }

    /**
     * Called after creation to inilialize the script.  A call is made in
     * the Logic Database as part of the 'generate()' method there.
     *
     * This method need to be overriden to initalize any items as needed.  This
     * is the place to put any coding that deals with an entity as the
     * constructor is called prior to the entity id and type being set.
     *
     * This verision of the method does nothing.
     */
    public void init() {
        //does nothing;
    }

    /**
     *  Returns the Entity's ID
     *
     * @return ID of Entity this Module belongs too
     */
    public int getEntityID() {
        return entityID;
    }

    /**
     * This method calls on the database group to get an entity based on which
     * type of entity this Module belongs.  It will then return the most basic
     * common class that has Logic Modules "EntityWithData_Logic" and return
     * Entity.
     *
     * This method is primarly used in the DoAction method of any sub-class of
     * this class as a convience mehtod.  A lot of the basic funtionality is
     * there as the type returned, but if more specific functionality is needed
     * that is only in one class, then the returned Entity can be easily casted
     * as appropriately.
     *
     * @return Entity of proper class casted backward in to EntityWIthData_Logic
     */
    public EntityWithData_Logic me() {
        if (entitytype == EntityType.Character) {
            return dbg.characterDB.get(entityID);
        } else if (entitytype == EntityType.Item) {
            return dbg.itemDB.get(entityID);
        } else if (entitytype == EntityType.Room) {
            return dbg.roomDB.get(entityID);
        } else if (entitytype == EntityType.Portal) {
            return dbg.portalDB.get(entityID);
        } else {
            return dbg.regionDB.get(entityID);
        }
    }
}
