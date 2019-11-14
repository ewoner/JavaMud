package Commands;

import Entities.MudCharacter;

/**
 *
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 * Version 1.0.0
 *
 */
public class disabledevice extends Command {

    public disabledevice(MudCharacter mob) {
        super(mob, "disabledevice", "\"diabledevice <item>\"",
                "Attempts to disable an item.  The item does not need to be in you inventory.");
    }

    @Override
    public void Execute(String args) {
        if (args.isEmpty()) {
            badUsage();
            return;
        }

        

    }
}
