package Commands;

import Actions.ActionType;
import Entities.Item;
import Entities.MudCharacter;
import Logics.Logic;
import Logics.wearables;

/**
 * This command is used to show what equipment the character currently has
 * equipted.
 *
 * @author Brion Lang
 *  Date: 08 Dec 2009
 *
 * Version 1.0.0
 *
 */
public class outfit extends Command {

    public outfit(MudCharacter mob) {
        super(mob, "outfit", "\"outfit\"",
                "Displays the players current item worn and their location.");
    }

    @Override
    public void Execute(String args) {
        Logics.wearables wearables = (wearables) Mob().getLogic("wearables");
        String string = "<#FFFFFF>--------------------------------------------------------------------------------\r\n";
        string += "<#00FF00> What you are wearing:\r\n";
        string += "<#FFFFFF>--------------------------------------------------------------------------------\r\n";
        string += "<#FFFFFF>Head:         <$reset>" + wearables.Head()+"\r\n";
        string += "<#FFFFFF>Chest:        <$reset>" + wearables.Chest()+"\r\n";
        string += "<#FFFFFF>Arms:         <$reset>" + wearables.Arms()+"\r\n";
        string += "<#FFFFFF>Hands:        <$reset>" + wearables.Hands()+"\r\n";
        string += "<#FFFFFF>Primary Hand: <$reset>" + wearables.PrimaryHand()+"\r\n";
        string += "<#FFFFFF>Off Hand:     <$reset>" + wearables.OffHand()+"\r\n";
        string += "<#FFFFFF>Legs:         <$reset>" + wearables.Legs()+"\r\n";
        string += "<#FFFFFF>Right Ring:   <$reset>" + wearables.RightRing()+"\r\n";
        string += "<#FFFFFF>Left Ring:    <$reset>" + wearables.LeftRing()+"\r\n";
        string += "<#FFFFFF>--------------------------------------------------------------------------------<$reset>\r\n";
        Mob().DoAction(ActionType.announce, 0, 0, 0, 0, string);
    }
}
