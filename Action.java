package Actions;

/**
 * Everything that happens in the physical part of the world is considered
 * an action and this includes characters moving, attributes changing,
 * items moving around, and people speaking.
 *
 * @author Brion Lang
 * Date: 17 Jan 2009
 * 
 * Version 1.0.0
 */
public class Action {

    /**
     * The Type of action.
     */
    private ActionType action;
    /**
     * All aguements to Action are boiled done to integers.  Depending on
     * the ActionType is what each arguement represetns.  See the Action
     * Type Table Documentation for more information.
     */
    public int arg1;
    /**
     * All aguements to Action are boiled done to integers.  Depending on
     * the ActionType is what each arguement represetns.  See the Action
     * Type Table Documentation for more information.
     */
    public int arg2;
    /**
     * All aguements to Action are boiled done to integers.  Depending on
     * the ActionType is what each arguement represetns.  See the Action
     * Type Table Documentation for more information.
     */
    public int arg3;
    /**
     * All aguements to Action are boiled done to integers.  Depending on
     * the ActionType is what each arguement represetns.  See the Action
     * Type Table Documentation for more information.
     */
    public int arg4;
    /**
     * Some actions require string data.  Accessed by the getData() method.
     */
    private String data;

    /**
     * Creates a basic empty Action without any type.
     * Deleted due to not being used.
     */
//    public Action() {
//        this(null, 0, 0, 0, 0, null);
//    }
    /**
     * Creates a basic Action of the ActionType provided, without any data.
     *
     * @param type The type of action.
     */
    public Action(ActionType type) {
        this(type, 0, 0, 0, 0, null);
    }

    /**
     * Creates a basic Action.
     *
     * @param type The type of action.
     * @param arg1 First arguemnent
     */
    public Action(ActionType type, int arg1) {
        this(type, arg1, 0, 0, 0, null);
    }

    /**
     * Creates a basic Action.
     * 
     * @param type The type of action.
     * @param arg1 First arguemnent
     * @param arg2 Second arguement
     */
    public Action(ActionType type, int arg1, int arg2) {
        this(type, arg1, arg2, 0, 0, null);
    }

    /**
     * Creates a basic Action.
     *
     * @param type The type of action.
     * @param arg1 First arguemnent
     * @param arg2 Second arguement
     * @param arg3 Third aurguemnt
     */
    public Action(ActionType type, int arg1, int arg2, int arg3) {
        this(type, arg1, arg2, arg3, 0, null);
    }

    /**
     * Creates a basic Action.
     *
     * @param type The type of action.
     * @param arg1 First arguemnent
     * @param arg2 Second arguement
     * @param arg3 Third arguement
     * @param arg4 Fourth arguement
     */
    public Action(ActionType type, int arg1, int arg2, int arg3, int arg4) {
        this(type, arg1, arg2, arg3, arg4, null);
    }

    /**
     * Creates a basic Action.
     *
     * Currently not in use.
     *
     * @param type The type of action.
     * @param data String arguement
     *
     */
    public Action(ActionType type, String data) {
        this(type, 0, 0, 0, 0, data);
    }

    /**
     * Creates a basic Action.
     *
     * Currently not in use.
     *
     * @param type The type of action.
     * @param arg1 First arguemnent
     * @param data String arguement
     */
    public Action(ActionType type, int arg1, String data) {
        this(type, arg1, 0, 0, 0, data);
    }

    /**
     * Creates a basic Action.
     *
     * Currently not in use.
     *
     * @param type The type of action.
     * @param arg1 First arguemnent
     * @param arg2 Second arguement
     * @param data String arguement
     */
    public Action(ActionType type, int arg1, int arg2, String data) {
        this(type, arg1, arg2, 0, 0, data);
    }

    /**
     * Creates a basic Action.
     *
     * Currently not in use.
     *
     * @param type The type of action.
     * @param arg1 First arguemnent
     * @param arg2 Second arguement
     * @param arg3 Third arguement
     * @param data String arguement
     */
    public Action(ActionType type, int arg1, int arg2, int arg3, String data) {
        this(type, arg1, arg2, arg3, 0, data);
    }

    /**
     * Creates a basic Action.
     *
     * @param type The type of action.
     * @param arg1 First arguemnent
     * @param arg2 Second arguement
     * @param arg3 Third arguement
     * @param arg4 Fourth arguement
     * @param data String arguement
     */
    public Action(ActionType type, int arg1, int arg2, int arg3, int arg4, String data) {
        this.action = type;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.arg3 = arg3;
        this.arg4 = arg4;
        this.data = data;
    }

    /**
     * Return's the action's type.
     *
     * @return ActionType
     */
    public ActionType getType() {
        return action;
    }

    /**
     * Returns the action's string arguement.
     * 
     * @return String arguement
     */
    public String getData() {
        return data;
    }
}
