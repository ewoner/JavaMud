package Commands;

import Actions.Action;
import Actions.ActionType;
import Entities.MudCharacter;
import Scripts.ScriptReloadMode;
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
public class reloadscript extends Command {

    public reloadscript(MudCharacter mob) {
        super(mob,
                "ReloadScript",
                "\"reloadscript <type> <file> <keepall|keepdata>\"",
                "Reloads a script.  It will not load a new script.");
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
        if (!file.contains(" ")) {
            badUsage();
            return;
        }
        String keep = file.substring(file.indexOf(' ') + 1);
        file = file.substring(0, file.indexOf(' '));
        ScriptReloadMode flagtype = null;
        if (keep.toLowerCase().equals("keepall")) {
            flagtype = ScriptReloadMode.LEAVEEXISTING;
        } else if (keep.toLowerCase().equals("keepdata")) {
            flagtype = ScriptReloadMode.RELOADFUNCTIONS;
        } else {
            badUsage();
            return;
        }
        if (type.toLowerCase().equals("command")) {
            boolean worked = false;
            try {
                worked = Mud.Mud.dbg.commandDB.reload(file, flagtype);
            } catch (Exception ex) {
                Logger.getLogger(reloadscript.class.getName()).log(Level.SEVERE, null, ex);
            }
//            boolean worked = true;
//            Mud.Mud.dbg.commandDB.Load(file);
            if (worked) {
                try {
                    Main.game.addActionAbsolute(0, ActionType.announce, 0, 0, 0, 0, Mob().getName() + " has reloaded the script: " + file);
                } catch (Exception ex) {
                    Logger.getLogger(reloadscript.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                Mob().DoAction(new Action(ActionType.error, 0, 0, 0, 0, "Character Script " + file + " not loaded.!"));
            }
            return;
        } else if (type.toLowerCase().equals("logic")) {
            boolean worked = false;
            try {
                worked = Mud.Mud.dbg.logicDB.reload(file, flagtype);
            } catch (Exception ex) {
                Logger.getLogger(reloadscript.class.getName()).log(Level.SEVERE, null, ex);
            }
//            boolean worked = true;
//            Mud.Mud.dbg.commandDB.Load(file);
            if (worked) {
                try {
                    Main.game.addActionAbsolute(0, ActionType.announce, 0, 0, 0, 0, Mob().getName() + " has reloaded the script: " + file);
                } catch (Exception ex) {
                    Logger.getLogger(reloadscript.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                Mob().DoAction(new Action(ActionType.error, 0, 0, 0, 0, "Character Script " + file + " not loaded.!"));
            }
            return;
        }
        Mob().DoAction(new Action(ActionType.error, 0, 0, 0, 0, "Invalid Script Type"));

    }
}
