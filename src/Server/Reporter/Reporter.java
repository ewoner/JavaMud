/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Reporter;

import Actions.Action;
import Entities.Item;
import Entities.MudCharacter;
import Entities.Portal;
import Entities.Room;

/**
 *
 * @author Administrator
 */
public interface Reporter {

    public String name();

    public boolean canSave();

    public int DoAction(Action action);

    void SeeRoom(Room p_id);

    void SeeRoomName(Room p_id);

    void SeeRoomDesc(Room p_id);

    void SeeRoomExits(Room p_id);

    void SeeRoomCharacters(Room p_id);

    void SeeRoomItems(Room p_id);

    void EnterRoom(MudCharacter p_character, Portal p_portal);

    void LeaveRoom(MudCharacter p_character, Portal p_portal);

    void Died(MudCharacter p_character);

    void GetItem(MudCharacter p_character, Item p_item);

    void DropItem(MudCharacter p_character, Item p_item);

    void GaveItem(MudCharacter p_giver, MudCharacter p_receiver, Item p_item);

    // ------------------------------------------------------------------------
    //  Helper, sends a string to a connection.
    // ------------------------------------------------------------------------
    void SendString(String string);
}
