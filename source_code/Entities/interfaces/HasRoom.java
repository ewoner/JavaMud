package Entities.interfaces;

import Entities.Room;

/**
 * Functionality for any Entity that has a Room.
 * Item
 * Character
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */public interface HasRoom {

    //////////////////////////////////////////
    //               Origanals              //
    //////////////////////////////////////////
     /**
      * Gets the accual room of the Entity based on its ID.
      *
      * @return Room of Entity
      */
     public Room Room();

    //////////////////////////////////////////
    //               BetterMud              //
    //////////////////////////////////////////
     /**
      * Sets the Entiy's Room ID
      *
      * @param roomid
      */
     public void setRoom(int roomid);

     /**
      * Gets the Entity's Room ID.
      * 
      * @return Entity's Room's ID
      */
     public int getRoom();
}
