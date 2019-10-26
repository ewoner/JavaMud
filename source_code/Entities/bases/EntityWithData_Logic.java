package Entities.bases;

import Actions.Action;
import Actions.ActionType;
import Actions.TimedAction;
import Databank.DataBank;
import Entities.interfaces.HasData;
import Entities.interfaces.HasLogic;
import LogicCollection.LogicCollection;
import Logics.Logic;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * An Entity with both Data and Logic.  First step in fully functional
 * Entities. (Except Accounts).
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 * Version 1.2.0
 * 1.  Removed the public static int.  A zero will be returned instead.
 * 2.  Changed the type of set to a ConcurrentSkipListSet type.
 * Version 1.1.0
 * 1.  Added a public static int to return if no key is found.
 *  Version 1.0.0
 */
abstract public class EntityWithData_Logic extends Entity implements
        HasLogic, HasData {

    private DataBank<Integer> attributes = new DataBank<Integer>();
    private LogicCollection logics = new LogicCollection();
    private final Set<TimedAction> hooks = new ConcurrentSkipListSet<TimedAction>();
    
    @Override
    public boolean addLogic(String logicName) {
        try {
            logics.add(logicName, this);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public boolean addExistingLogic(Logic logic) {
        try {
            logics.addExisting(logic);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public boolean delLogic(String logic) {
        try {
            // go through all the hooks and unhook them
            clearLogicHooks(logic);

            // delete the logic
            logics.delete(logic);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public Logic getLogic(String logicname) {
        return logics.get(logicname);
    }

    @Override
    public boolean hasLogic(String logicname) {
        try {
            return logics.hasLogic(logicname);
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public int DoAction(Action action) {
        return logics.DoAction(action);
    }

    @Override
    public int DoAction(ActionType actiontype, int arg1, int arg2, int arg3, int arg4, String data) {
        return logics.DoAction(new Action(actiontype, arg1, arg2, arg3, arg4, data));
    }

    @Override
    public int getLogicAttribute(String logicname, String attribute) {
        return logics.getAttribute(logicname, attribute);
    }

    @Override
    public void setLogicAttribute(String logicname, String attribute, int value) {
        logics.setAttribute(logicname, attribute, value);
    }

    @Override
    public int getAttribute(String attribute) {
        if (attributes.get(attribute) == null) {
            return 0;
        } else {
            return attributes.get(attribute);
        }
    }

    @Override
    public void setAttribute(String attribute, int value) {
        attributes.del(attribute);
        attributes.set(attribute, value);
    }

    @Override
    public boolean hasAttribute(String attribute) {
        return attributes.has(attribute);
    }

    @Override
    public void addAttribute(String attribute, int value) {
        attributes.set(attribute, value);
    }

    @Override
    public void delAttribute(String attribute) {
        attributes.del(attribute);
    }

    @Override
    public DataBank<Integer> getAttributes() {
        return attributes;
    }

    @Override
    public void setAttributes(DataBank<Integer> attributes) {
        this.attributes = attributes;
    }

    @Override
    public void copyAttributes(DataBank<Integer> attributes) {
        for (String key : attributes.getAttributeNames()) {
            this.attributes.set(key, attributes.get(key));
        }
    }

    @Override
    public void addHook(TimedAction hook) {
        hooks.add(hook);
    }

    @Override
    public void clearHooks() {
        for (TimedAction action : hooks) {
            action.unhook();
        }
    }

    @Override
    public void clearLogicHooks(String logicname) {
        for (TimedAction action : hooks) {
            if (action.getActionEvent().getType() == ActionType.messagelogic ||
                    action.getActionEvent().getType() == ActionType.dellogic) {
                if (action.getActionEvent().getData().equalsIgnoreCase(logicname)) {
                    // found a match, now unhook it.
                    action.unhook();
                }
            }
        }
    }

    @Override
    public int numHooks() {
        return hooks.size();
    }

    @Override
    public void killHook(ActionType actiontype, String data) {
        synchronized (hooks) {
            for (TimedAction a : hooks) {
                if (a.getActionEvent().getType() == actiontype &&
                        a.getActionEvent().getData().equalsIgnoreCase(data)) {
                    a.unhook();
                }
            }
        }
    }

    @Override
    public void delHook(TimedAction hook) {
        hooks.remove(hook);
    }

    @Override
    public LogicCollection getLogics() {
        return logics;
    }
}
