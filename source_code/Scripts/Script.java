package Scripts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The basic Script in the game.  Both Command and Logic and all their
 * sub class start with being of type Script.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
abstract public class Script {

    /**
     * the name of the script as on file.  Each subclass must make its own
     * setScriptName() method and this is left protected in order to acess it.
     *
     * Currently out of use.
     */
    protected String scriptName;

    /**
     * Loades a script from file.  by default, this one chews up two tags, 
     * {DATA][/DATA] only.
     * 
     * @param reader The reader to read from.
     * @throws java.io.IOException
     */
    public void Load(BufferedReader reader) throws IOException {
        // default to empty data loading, by chewing up the [DATA] and
        // [/DATA] tags.
        reader.readLine();
        reader.readLine();
    }

    /**
     * Saves a script from file.  by default, this one saves only  two tags, 
     * {DATA][/DATA].
     * 
     * @param writer The reader to read from.
     */
    public void Save(PrintWriter writer) {
        // default to empty data saving
        writer.println("[DATA]");
        writer.println("[/DATA]]");
    }

    /**
     * Gets the script's game name.
     *
     * Currently out of use.
     *
     * @return Scripts game name.
     */
    public String getScriptName() {
        return scriptName;
    }

    /**
     * Must be overrode in subclasses.  Used to set the Scripts name.  For
     * example.  The method for Command looks like the following:
     *
     * public void setScriptName() {
     *      String className = this.getClass().getName();
     *      className = className.substring(className.indexOf('.') + 1);
     *      scriptName = className;
     *}
     * 
     * Currently out of use.
     *
     */
    abstract public void setScriptName();
}
