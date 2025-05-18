package Commands;

import Actions.Action;
import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;
import Logics.Logic;
import Server.Main;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.0.0
 *
 */
public class use extends Command {

    public use(MudCharacter mob) {
        super(mob, "use", "\"use <item>\"",
                "Tries to use an item");
    }

    @Override
    public void Execute(String parameters) {
        if (parameters.isEmpty()) {
            this.badUsage();
            return;
        }
        Item item;
        try {
            item = Mob().seekItem(parameters);
        } catch (Exception e) {
            item = Mob().seekItem(parameters);
        }
        if (item == null) {
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "Cannot find the item: " + parameters + "!");
            return;
        }
        if (item.hasAttribute("weapon")) {
            Main.game.doAction(new Action(ActionType.command, Mob().getID(), 0, 0, 0, "/arm "+parameters));
            return;
        }
        if (item.hasAttribute("armor") || item.hasAttribute("shield")) {
            Main.game.doAction(new Action(ActionType.command, Mob().getID(), 0, 0, 0, "/wear "+parameters));
            return;
        }
        if (item.DoAction(ActionType.query, 0, 0, 0, 0, "canuse") == Logic.FALSE) {
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "Cannot use " + item.getName() + "!");
            return;
        }
        item.DoAction(ActionType.doaction, 0, 0, Mob().getID(), 0, "use");
    }
}
