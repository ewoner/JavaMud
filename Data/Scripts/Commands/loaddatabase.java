package Commands;

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
 * Version 1.0.1
 * 1. Changed the "usage" line to bring it on line with the rest of the commands.
 * Version 1.0.0
 *
 */
public class loaddatabase extends Command {

    public loaddatabase(MudCharacter mob) {
        super(mob,
                "loaddatabase",
                "\"loaddatabase <ITEMS/CHARACTERS/REGIONS/COMMANDS/LOGICS> <file name> <|keepdata|keepall>\"",
                "Performs a manual database load");
    }

    @Override
    public void Execute(String args) {
        if (args.isEmpty()) {
            badUsage();
            return;
        }
        String[] parms = args.split(" ");
        int mode = 0;
        if (parms.length < 2) {
            badUsage();
            return;
        }
        try {
            if (parms[0].equals("items")) {
                Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Beginning Item Template Database Load: " + parms[1]);
                Main.game.doAction(ActionType.reloaditems, 0, 0, 0, 0, parms[1]);
                return;
            }
            if (parms[0].equals("characters")) {
                Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Beginning Character Template Database Load: " + parms[1]);
                Main.game.doAction(ActionType.reloadcharacters, 0, 0, 0, 0, parms[1]);
                return;
            }
            if (parms[0].equals("regions")) {
                Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Beginning Region Database Load: " + parms[1]);
                Main.game.doAction(ActionType.reloadregion, 0, 0, 0, 0, parms[1]);
                return;
            }
            if (parms[0].equals("commands")) {
                if (parms.length < 3) {
                    badUsage();
                    return;
                }
                if (parms[2].equals("keepdata")) {
                    mode = 1;
                } else if (parms[2].equals("keepall")) {
                    mode = 0;
                } else {
                    badUsage();
                    return;
                }
                Mob().DoAction(ActionType.announce, mode, 0, 0, 0, "Beginning Command Database Load: " + parms[1]);
                Main.game.doAction(ActionType.reloadcommandscript, mode, 0, 0, 0, parms[1]);
                return;
            }
            if (parms[0].equals("logics")) {
                if (parms.length < 3) {
                    badUsage();
                    return;
                }
                if (parms[2].equals("keepdata")) {
                    mode = 1;
                } else if (parms[2].equals("keepall")) {
                    mode = 0;
                } else {
                    badUsage();
                    return;
                }
                Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Beginning Logic Database Load: " + parms[1]);
                Main.game.doAction(ActionType.reloadlogicscript, mode, 0, 0, 0, parms[1]);
                return;
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }


        badUsage();

    }
}
