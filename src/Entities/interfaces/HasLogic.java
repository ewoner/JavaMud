package Entities.interfaces;

import Actions.Action;
import Actions.ActionType;
import Actions.TimedAction;
import LogicCollection.LogicCollection;
import Logics.Logic;

/**
 * Funciontaliy for all Enities that have logic.
 * Character
 * Item
 * Room
 * Region
 * Portal
 * 
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public interface HasLogic {
    /* Know in BetterMud as LogicEntity */
    //////////////////////////////////////////
    //               BetterMud              //
    //////////////////////////////////////////

    /**
     * Adds a new Logic module to the Entity based on logic's name.
     *
     * @param logicname
     * @return TRUE if added
     */
    public boolean addLogic(String logicname);

    /**
     * Adds an existing Logic Module to the Entity.
     *
     * @param logic
     * @return TRUE if added.
     */
    public boolean addExistingLogic(Logic logic);

    /**
     * Addes a hook to the Entity.
     *
     * @param hook Action to Hook
     */
    public void addHook(TimedAction hook);

    /**
     * Clears all hooks from Entity.
     */
    public void clearHooks();

    /**
     * Clears all Logic hooks based on name of hook.
     *
     * @param hookname
     */
    public void clearLogicHooks(String hookname);

    /**
     * Gets a Logic Modules Attribute
     *
     * @param logicName Logic Module's name
     * @param attribute Attribute's name
     * @return Value of Attribute
     */
    public int getLogicAttribute(String logicName, String attribute);

    /**
     * Sets a Logic Modules Attribute's value.
     * 
     * @param logicName Logic Module's name
     * @param attribute Attribute's name
     * @param value Value of Attribute
     */
    public void setLogicAttribute(String logicName, String attribute, int value);
    /**
     * Number of hooks on Entity.
     * @return Number of hooks.
     */
    public int numHooks();

    /**
     * Killes a hook based on ActionType and any data in actiondata.
     * @param action ActionType to kill
     * @param actiondata Data
     */
    public void killHook(ActionType action, String actiondata);

    /**
     * Deletes a hook on this Entity.
     *
     * @param hook Action to delete.
     */
    public void delHook(TimedAction hook);

    /**
     * Deletes Logic Nodule on Entity.
     *
     * @param logicname
     * @return TRUE if removed.
     */
    public boolean delLogic(String logicname);

    /**
     * Returnes a Logic Module based on name.
     *
     * @param logicname
     * @return Logic Module
     */
    public Logic getLogic(String logicname);

    /**
     * Checks to see if the Entity has a Logice Module.
     *
     * @param logicname
     * @return TRUE if present.
     */
    public boolean hasLogic(String logicname);

    /**
     * Passes an Action on to the Logic Collection to be processed.
     *
     * @param action Action to process.
     * @return Return value of Collection.
     */
    public int DoAction(Action action);

    /**
     * Passes an Action on to the Logic Collection to be processed.
     *
     * @param actiontype Type of Action
     * @param arg1 for the Action
     * @param arg2 for the Action
     * @param arg3 for the Action
     * @param arg4 for the Action
     * @param data for the Action
     * @return Return value of Collection.
     */
    public int DoAction(ActionType actiontype, int arg1, int arg2, int arg3, int arg4, String data);

    //////////////////////////////////////////
    //               Origanals              //
    //////////////////////////////////////////
    /**
     * Gets the whole Logice Collection.  Mainly used in saving and loading.
     *
     * @return Logic Collection
     */
    public LogicCollection getLogics();
}
