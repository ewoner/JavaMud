package Commands;

import Actions.ActionType;
import Entities.MudCharacter;
import Entities.Region;
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
public class savedatabase extends Command {

    public savedatabase(MudCharacter mob) {
        super(mob,
                "savedatabase",
                "\"savedatabase <all|region|players> <|regionid>\"",
                "Performs a manual database save");
    }

    @Override
    public void Execute(String args) {

        if (args.isEmpty()) {
            badUsage();
            return;
        }
        String[] parms = args.split(" ");

        if (parms[0].equals("all")) {
            Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Beginning Complete Database Save");
            Main.game.addActionAbsolute(0, ActionType.savedatabases, 0, 0, 0, 0, "");
            return;
        }
        if (parms[0].equals("region")) {
            if (parms.length < 2) {
                badUsage();
                return;
            }
            Region region = Mud.Mud.dbg.regionDB.findname(parms[1]);
            if (region == null) {
                Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Could not find region " + parms[1] + ".  Pleace check spelling.");
                return;
            }
            Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Beginning Region Database Save: " + region.getName());
            Main.game.addActionAbsolute(0, ActionType.saveregion, region.getID(), 0, 0, 0, "");
            return;
        }
        if (parms[0].equals("players")) {
            Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Beginning Player Database Save");
            Main.game.addActionAbsolute(0, ActionType.saveplayers, 0, 0, 0, 0, "");
            return;
        }

        badUsage();

    }
}
