package Commands;

import Actions.ActionType;
import Entities.MudCharacter;
import Entities.Templates.ItemTemplate;
import Mud.Mud;
import Server.Main;

/**
 * This command is used to create a new item using a template from the database.
 * This command does not create a totally new item.
 *
 *
 * @author Brion Lang
 * Date: 17 Jan 2009
 * 
 * Version 1.0.1
 * 1.  Added a "try/catch" block to catch errors when the usage is incorrect
 * and preventing the error of inputing the quantity from displaying the 
 * "badUsage" method.
 * Version 1.0.0
 */
public class spawnitem extends Command {

    public spawnitem(MudCharacter mob) {
        super(mob, "spawnitem", "\"spawnitem <item template id/item template name> <|quantity>\"",
                "Spawns a new item in your inventory");
    }

    @Override
    public void Execute(String args) {
        if (args.isEmpty()) {
            badUsage();
            return;
        }
        int quantity = 0;
        String[] parms = Mud.splitString(args);
        if (parms.length > 1) {
            String temp = parms[0];
            try {quantity = Integer.parseInt(parms[1]);}
            catch(Exception ex){
                badUsage();
                return;
            }
            args = temp;
        } else {
            args = parms[0];
        }

        ItemTemplate t;
        try {
            t = Mud.dbg.itemDB.getTemplate(Integer.parseInt(args));
        } catch (Exception ex) {
            t = Mud.dbg.itemDB.getTemplate(args);
        }
        if (t == null) {
            Mob().DoAction(ActionType.error, 0, 0, 0, 0, "Item: \"" + args + "\" can not be found in the database.");
            return;
        }
        Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Spawning Item...");
        Main.game.doAction(ActionType.spawnitem, t.getID(), Mob().getID(), 1, quantity, "");
        Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Success.");


    }
}
