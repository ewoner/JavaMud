package Databases;

import Commands.Command;
import Entities.MudCharacter;
import Entities.enums.AccessLevel;
import Scripts.Script;
import Scripts.ScriptManager;
import Scripts.ScriptReloadMode;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Command Database.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public class CommandDatabase {

    private Map<String, Boolean> commands = new HashMap<String, Boolean>();
    private static final String scriptfolder = "Data/Scripts/";
    private static final String classfolder = "Commands";

    /** 
     * Basic Constructor.  Does nothing but calls super()
     */
    public CommandDatabase() {
        super();
    }

    /**
     * Generates a new instance of a command name by its single paramiter
     * 
     * @param name Name of the Command to generate
     * @return A new instance of the command whose name is equal to 'name' above
     * @throws Exception
     */
    public Command generateCommand(String name) throws Exception {
        Command com = null;
        boolean loadable = false;
        String command = null;
        if (commands.containsKey(name.toLowerCase())) {
            command = name.toLowerCase();
            loadable = commands.get(command);
        }
        if (loadable && command != null) {
            try {
                File file = new File(scriptfolder);
                ClassLoader loader = new URLClassLoader(new URL[] {file.toURI().toURL()});
                Class cc = loader.loadClass("Commands." + command);
                @SuppressWarnings(value = "unchecked")
                Constructor cons = cc.getConstructor(MudCharacter.class);
                com = (Command) cons.newInstance((MudCharacter) null);
            } catch (Exception ex) {
                Logger.getLogger(CommandDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (command != null) {
            try {
                ClassLoader loader = Command.class.getClassLoader();
                Class cc = loader.loadClass("Commands." + command);
                @SuppressWarnings(value = "unchecked")
                Constructor cons = cc.getConstructor(MudCharacter.class);
                com = (Command) cons.newInstance((MudCharacter) null);
            } catch (Exception ex) {
                Logger.getLogger(CommandDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (com != null) {
            return com;
        } else {
            throw new Exception("Unknown Command Script");
        }

    /*Command com = generateCommand(name, null);
    return com;*/
    }

    /**
     * Generates a new instance of a command name by its single paramiter
     * assosiating the character with the command.
     *
     * @param name Name of the Command to generate
     * @param mob Mob to assosiate with command
     * @return A new instance of the command whose name is equal to 'name' above
     * @throws Exception
     */
    public Command generateCommand(String name, MudCharacter mob) throws Exception {
        Command com = null;
        boolean loadable = false;
        String command = null;
        if (commands.containsKey(name.toLowerCase())) {
            command = name.toLowerCase();
            loadable = commands.get(command);
        }
        if (loadable && command != null) {
            try {
                File file = new File(scriptfolder);
                ClassLoader loader = new URLClassLoader(new URL[] {file.toURI().toURL()});
                Class cc = loader.loadClass("Commands." + command);
                @SuppressWarnings(value = "unchecked")
                Constructor cons = cc.getConstructor(MudCharacter.class);
                com = (Command) cons.newInstance(mob);
            } catch (Exception ex) {
                Logger.getLogger(CommandDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (command != null) {
            try {
                ClassLoader loader = Command.class.getClassLoader();
                Class cc = loader.loadClass("Commands." + command);
                @SuppressWarnings(value = "unchecked")
                Constructor cons = cc.getConstructor(MudCharacter.class);
                com = (Command) cons.newInstance(mob);
            } catch (Exception ex) {
                Logger.getLogger(CommandDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (com != null) {
            return com;
        } else {
            throw new Exception("Unknown Command Script");
        }
    }

    /** Give that player commands for that access level.
     * 
     *  Not yet implementd.
     *
     * @param player player to give access commands to
     * @param level access level of commands to issue player
     */
    public void giveCommand(MudCharacter player, AccessLevel level) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Loads the initial database from hardcode, NOT from 'scripts'.
     * Calls on loadCommand(String) to due such.  Will load the whole directory,
     * to include the "scriptable" files.
     */
    public void loadDB() {
        File fileDir = new File("Data/Scripts/" + classfolder);
        if (fileDir.isDirectory()) {
            File[] scriptFiles = fileDir.listFiles();
            for (File f : scriptFiles) {
                String name = f.getName().replace(".java", "\n").trim();
                if (name.contains(".class")) {
                    continue;
                }
                boolean workded = ScriptManager.javac(name, "Commands");
                Command c = loadCommand(name);
                addCommand(c);
                if (!workded) {
                    Logger.getLogger(CommandDatabase.class.getName()).log(Level.SEVERE, null, new IOException("Can not compile file: " + f.getPath()));
                }
            }
        } else {
            Logger.getLogger(CommandDatabase.class.getName()).log(Level.SEVERE, null, new IOException("Can not file id not a Directory: " + fileDir.getPath()));
        }
        File commandDir = new File("build/classes/Commands/");
        if (commandDir.isDirectory()) {
            File[] commandFiles = commandDir.listFiles();
            for (File f : commandFiles) {
                String commandName = f.getName().replace(".class", "\n").trim();
                Command c = loadCommand(commandName);
                addCommand(c);
            }
        } else {
            Logger.getLogger(CommandDatabase.class.getName()).log(Level.SEVERE, null, new IOException("Can not file id not a Directory: " + commandDir.getPath()));
        }

    }

    /**
     * This wrapper method is use to ensure no duplicate commands are load into the List.
     *
     * @param command
     */
    public void addCommand(Command command) {
        if ((command != null) && (!commands.containsKey(command.getName().toLowerCase()))) {
            commands.put(command.getName().toLowerCase(), command.isLoadable());
        }
    }

    /**
     * Will reload any script that is loadable.  That defaults only to 'scripted'
     * commands or other user defined ones.  The 12 hard coded commands are
     * non-loadable while the mud is still running.
     *
     * @param name Name of the command to reload
     * @param mode Mode to relaod as (NOT WORKING YET).
     * @return TRUE if reloaded, otherwise FALSE
     */
    public boolean reload(String name, ScriptReloadMode mode) {
        name = name.toLowerCase();
        Command c = null;
        try {
            c = generateCommand(name);
        } catch (Exception ex) {
            Logger.getLogger(CommandDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (c == null || c.isLoadable()) {
            if (commands.containsKey(name)) {
                removeCommand(name);
                if (load(name)) {
                    removeCommand(name);
                    for (MudCharacter mob : Mud.Mud.dbg.characterDB.getInstances().getContainer().values()) {
                        if (mob.hasCommand(name)) {
                            mob.delCommand(name);
                            mob.addCommand(name);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Used to load a command from the "scripts".  It first checks if it is
     * loaded and returns if it is.
     *
     * @param name Name of Command to be load
     * @return TRUE if it loads, else FALSE.
     */
    public boolean load(String name) {
        name = name.toLowerCase();
        Command c = null;
        try {
            c = generateCommand(name);
        } catch (Exception ex) {
            Logger.getLogger(CommandDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (c != null && !c.isLoadable()) {
            return false;
        }
        boolean worked = ScriptManager.javac(name, classfolder);
        if (worked == false) {
            return false;
        }
        try {
            c = loadCommand(name, classfolder);
        } catch (Exception ex) {
            Logger.getLogger(CommandDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        addCommand(c);
        return true;
    }

    /** A test method use to relay the size of the currnt List of Commands
     *
     * @return size of database
     */
    public int size() {
        return commands.size();
    }

    /**
     * Loads a command from binary code.  The directoy it looks in is binDir.
     *
     * @param name Name of the command to load
     * @return Returns a new instance of the command
     */
    private Command loadCommand(String name) {
        if (name.equals("Command")) {
            return null;
        }
        name = name.toLowerCase();
        Command command = null;
        try {
            File classesDir = new File(scriptfolder);
            ClassLoader parentLoader = Command.class.getClassLoader();
            URLClassLoader loader1 = new URLClassLoader(new URL[] {classesDir.toURI().toURL()}, parentLoader);
            Class cls1 = loader1.loadClass(classfolder + "." + name);
            System.out.println("<------Now loading: " + cls1.getName());
            @SuppressWarnings(value = "unchecked")
            Constructor cons = cls1.getConstructor(new Class[] {MudCharacter.class});
            command = (Command) cons.newInstance(new Object[] {null});
            return command;
        } catch (Exception ex) {
            Logger.getLogger(CommandDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return command;
    }

    /** Removes a command from the Mapping.  Checks to ensure the command is
     * 'loadable' first before it removes the command.  Can only remove
     * non-loadable commands.
     */
    private void removeCommand(String name) {
        try {
            if (generateCommand(name).isLoadable()) {
                commands.remove(name);
            }
        } catch (Exception ex) {
            Logger.getLogger(CommandDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Command loadCommand(String name, String className) throws Exception {
        Script script = null;
        name = name.toLowerCase();
        File file = new File(scriptfolder);
        ClassLoader loader = new URLClassLoader(new URL[] {file.toURI().toURL()});
        // load class through new loader
        Class aClass = loader.loadClass(className + "." + name);
        if (className.equals("Commands")) {
            @SuppressWarnings("unchecked")
            Constructor cons = aClass.getConstructor(new Class[] {MudCharacter.class});
            script = (Script) cons.newInstance(new Object[] {null});
        } else {
            script = (Script) aClass.newInstance();
        }
        return (Command)script;
    }
}
