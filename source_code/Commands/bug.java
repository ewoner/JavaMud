package Commands;

import Actions.Action;
import Actions.ActionType;
import Entities.MudCharacter;
import Entities.Room;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * This command is used to report a bug.  Each bug is saved in the "bug" folder.
 * The file name is the time in miliseconds of when the bug was reported.  It
 * saves which acount reported the bug, a brief naritive of what the bug is,
 * the Mob that used the command, and a copy of the room and all its contents.
 *
 * @author Brion Lang
 * Date: 08 Dec 2009
 * Version 1.1.0
 * 1.  Added a visionual announcement when a bug is submitted.
 * Version 1.0.0
 */
public class bug extends Command {

    public bug(MudCharacter mob) {
        super(mob, "bug", "\"bug <bug naritive>\"",
                "This command is used to report bugs/errors with the mud.");
    }

    @Override
    public void Execute(String args) {
        if (args.isEmpty()) {
            badUsage();
            return;
        }
        Room room = Mob().Room();
        String timestamp = Mud.MyTimer.DateStamp() + " " + Mud.MyTimer.TimeStamp();
        long time = Mud.MyTimer.GetTimeMS();
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter("bug/" + time + ".bug"));
            out.println(timestamp + "\nbug reported by " + Mud.Mud.dbg.accountDB.get(Mob().getAccount()).getName() + " as follows:\n");
            out.println(args + "\n\n\n* * * * *\n\nMob is as follows:\n");
            Mud.Mud.dbg.characterDB.SaveEntity(out, Mob());
            out.println("\n\n\n* * * * *\n\nRoom is as follows:\n");
            Mud.Mud.dbg.roomDB.saveEntity(out, room);
            for (MudCharacter c : room.Characters()) {
                Mud.Mud.dbg.characterDB.SaveEntity(out, c);
            }
           Mob().DoAction(new Action(ActionType.announce,"Thank your for submitting the bug."));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
