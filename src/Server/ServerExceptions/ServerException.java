/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.ServerExceptions;

import java.io.IOException;

/**
 *
 * @author Administrator
 */
public class ServerException extends Exception{

    public static final long serialVersionUID = 1l;
    private String tagName;
    private Exception exception;
    private String additionalInfo = null;


    public ServerException(Exception ex, String tagName) {
        super(ex);
        this.tagName = tagName;
        this.exception = ex;
    }

    public ServerException(Exception ex, String tagName, String additionalInfo) {
        this(ex,tagName);
        this.additionalInfo = additionalInfo;
    }

    public Exception getException() {
        return exception;
    }

    public String getTagName() {
        return tagName;
    }
    public String getAdditionalInfo(){
        return additionalInfo;
    }
}
