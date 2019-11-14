package Scripts;

import java.io.PrintWriter;
import javax.tools.JavaCompiler;
/**
 * A Static class that hold some universial methods for Logic and Command
 * databases.  Namely compiler.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public class ScriptManager {

    /**
     * Compiles a Script of name tand type.  All script names are in lowercase
     * to help advoid confustion.  The only alowed  Class Types are "Command"
     * "Logic".  No other options are allowed with this Mud.
     *
     * The compiler defaults to outputing any errors to the System.out stream.
     *
     * @param scriptName name of the script (lowercae only)
     * @param scriptClassType "Command" or "Logic" only.
     * @return TRUE if it compiled, else FALSE if it failed.
     */
    public static boolean javac(String scriptName, String scriptClassType) {
        String sourceFile = "Data/Scripts/" + scriptClassType + "/" + scriptName + ".java";
        String dircrotyOprtion = "-d";
        String directory = "Data/Scripts/";
        String helpOption = "-help";
        PrintWriter writer = new PrintWriter(System.out);
        int compileReturnCode = com.sun.tools.javac.Main.compile(new String[] {dircrotyOprtion, directory, sourceFile}, writer);
        if (compileReturnCode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
