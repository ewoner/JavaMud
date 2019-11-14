/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Mud.Exceptions;

/**
 * The Basic Exception in the Mud game itself.
 *
 * @author Brion Lang
 *  Date: 26 Nov 2009
 *
 * Version 1.0.0
 */
public class MudException extends Exception {

    /**
     * The serial version of the class.
     */
    public static final long serialVersionUID = 1l;
    private String className;
    private Exception exception;

    /**
     * Basic constructor.  Takes any "Exception" and a string for the class's name where the error was thrown.
     * 
     * @param ex The exception thrown
     * @param className The name of the class were the exception was thrown.
     */
    public MudException(Exception ex, String className){
        super(ex);
        this.className = className;
        this.exception = ex;
    }

    /**
     * Getter method for the exception thrown.
     *
     * @return The exception
     */
    public Exception getException(){
        return exception;
    }

    /**
     * Gets the name of the class where the exception was thrown.
     *
     * @return The class's name where the exception was thrown.
     */
    public String getClassName(){
        return className;
    }

}
