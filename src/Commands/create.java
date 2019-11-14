package Commands;

import Actions.ActionType;
import Databases.Group.DatabaseGroup;
import Entities.MudCharacter;
import Server.Handler.CreationHandler;
import Server.Reporter.TelnetReporter;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 * Version 1.0.0
 *
 */
public class create extends Command {

    final static DatabaseGroup dbg = Mud.Mud.dbg;

    public create(MudCharacter mob) {
        super(mob, "create", "\"create\"",
                "Opens the creatation dialogs to create a new MOB, item, portal, room or region.  Creating a new room makes a new portal.  Creating a new region makes a new room and new portal.");
    }

    @Override
    public void Execute(String args) {
        if (args.isEmpty()) {
            badUsage();
            return;
        }
        TelnetReporter reporter = (TelnetReporter) Mob().getLogic("telnetreporter");
        Mob().DoAction(ActionType.announce, 0, 0, 0, 0, "Entering into creation mode.");
        reporter.getConnection().addHandler(new CreationHandler(reporter.getConnection()));

    }
}
