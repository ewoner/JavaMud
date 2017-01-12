package Commands;

import Actions.Action;
import Actions.ActionType;
import Entities.MudCharacter;
import Server.Main;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jan 2009
 *
 * Version 1.0.0
 *
 */
public class loadscript extends Command {

    public loadscript(MudCharacter mob) {
        super(mob,
                "LoadScript",
                "\"loadscript <type> <file> <|path> <|RELOAD> \"",
                "Loads a script.  It will not reload a previous loaded script.");
    }

    @Override
    public void Execute(String parameters) {
        if (parameters == null || parameters.equals("")) {
            badUsage();
            return;
        } else if (!parameters.contains(" ")) {
            badUsage();
            return;
        }
        String type = parameters.substring(0, parameters.indexOf(" "));
        String file = parameters.substring(parameters.indexOf(' ') + 1);
        if (type.toLowerCase().equals("command")) {
            boolean worked = false;
            try {
                worked = Mud.Mud.dbg.commandDB.load(file);
            } catch (Exception ex) {
                Logger.getLogger(reloadscript.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (worked) {
                Main.game.addActionAbsolute(0, new Action(ActionType.announce, 0, 0, 0, 0, Mob().getName() + " has load the script " + file + "!"));
            } else {
                Mob().DoAction(new Action(ActionType.error, 0, 0, 0, 0, "Character Script " + file + " not loaded.!"));
            }
            return;
        } else if (type.equalsIgnoreCase("logic")) {
            boolean worked = false;
            String path = null;
            if (!file.contains(" ")) {
                path = "Logics";
            } else {
                path = file.substring(file.indexOf(' ')).trim();
                file = file.substring(0, file.indexOf(' ')).trim();
            }
            try {
                worked = Mud.Mud.dbg.logicDB.load(file, path);
            } catch (Exception ex) {
                Logger.getLogger(reloadscript.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (worked) {
                Main.game.addActionAbsolute(0, new Action(ActionType.announce, 0, 0, 0, 0, Mob().getName() + " has load the script " + file + "!"));
            } else {
                Mob().DoAction(new Action(ActionType.error, 0, 0, 0, 0, "Character Script " + file + " not loaded.!"));
            }
            return;
        }
        Mob().DoAction(new Action(ActionType.error, 0, 0, 0, 0, "Invalid Script Type"));
    }
}
