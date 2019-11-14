package Entities.enums;

/**
 * Enum that holds the different account acces levels.  Not fully functional
 * with mud yet.  Used primarly at creation time right now.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public enum AccessLevel {

    /**
     * Basic player access level
     */
    Peon,
    /**
     * Access level allows account to build on the mud.
     */
    Builder,
    /**
     * Adds building and god-like powers to the account
     */
    God,
    /**
     * Adds only minimal admin rights to the account.  No building access, or god-like powers.
     */
    Admin;
}
