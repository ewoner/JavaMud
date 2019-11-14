package Entities.interfaces;

import Entities.MudCharacter;
import java.util.Set;

/**
 * Basic methods need to handle multiple charactes.  Characters are to be
 * stored as integers.
 *
 * For all Entity that have multiple characters:
 * Account
 * Room
 * Region
 * 
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */public interface HasCharacters {

    //////////////////////////////////////////
    //               BetterMud              //
    //////////////////////////////////////////
     /**
      * Adds a character to the Entity
      *
      * @param character The ID of the character to be added.
      */
     public void addCharacter(int character);

     /**
      * Removes character from the Entity.
      *
      * @param character The ID of the character to remove
      */
     public void removeCharacter(int character);

     /**
      * Number of characters the Entity currently has
      *
      * @return Number of characters present.
      */
     public int numCharacters();

    //////////////////////////////////////////
    //               Origanals              //
    //////////////////////////////////////////
     /**
      * Returns a Set of all character IDs of the Entity.
      *
      * @return Set of character IDs.
      */
     public Set<Integer> getCharacters();

     /**
      * Conviences call to get all MudCharacters the Entity has
      * based on its list of IDs.
      *
      * @return Set of MudCharacters based on character ID's present.
      */
     public Set<MudCharacter>  Characters();
     /**
      * Seeks a Character in the Entity. Searchs first a full match then
      * partial.
      *
      * @param name to search for
      * @return Character or NULL if not found.
      */
     public MudCharacter seekCharacter(String name);
}
