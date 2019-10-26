package Databases;

import Commands.Command;
import Entities.Item;
import Entities.MudCharacter;
import Entities.Portal;
import Entities.Region;
import Entities.Room;
import Entities.bases.Entity;
import Entities.enums.EntityType;
import Logics.Logic;
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
 * The Logic Database.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 *  Version 1.0.0
 */
public class LogicDatabase {

    private Map<String, String> modules = new HashMap<String, String>();
    private static final String scriptsfolder = "Data/Scripts/";
    private static final String classfolder = "Logics";

    /** 
     * Basic do not thing constructor.  Calls super() only.
     */
    public LogicDatabase() {
        super();
    }

    /**
     * Returns number of modules in memory.
     *
     * @return Number of modules loaded.
     */
    public int number() {
        return modules.size();
    }

    /**
     * Generates a new instance of a Logic Module
     *
     * @param name Name of module to create
     * @param entity Entity to generated logic is for
     * @return Returns a new instance of a module.
     */
    public Logic generate(String name, Entity entity) {
        Logic logic = null;
        int id = entity.getID();
        if (modules.containsKey(name.toLowerCase())) {
            try {
                File file = new File(scriptsfolder);
                ClassLoader parant = Logics.Logic.class.getClassLoader();
                ClassLoader loader = new URLClassLoader(new URL[] {file.toURI().toURL()}, parant);
                Class cls1 = loader.loadClass("Logics." + name);
//                Constructor con = cls1.getConstructor(new Class[] {String.class, int.class});
//                logic = (Logic) con.newInstance(name, id);
                logic = (Logic) cls1.newInstance();
                logic.setEntityID(id);
                if (entity instanceof MudCharacter) {
                    logic.setEntityType(EntityType.Character);
                } else if (entity instanceof Item) {
                    logic.setEntityType(EntityType.Item);
                } else if (entity instanceof Region) {
                    logic.setEntityType(EntityType.Region);
                } else if (entity instanceof Room) {
                    logic.setEntityType(EntityType.Room);
                } else if (entity instanceof Portal) {
                    logic.setEntityType(EntityType.Portal);
                }
                logic.init();
            } catch (Exception ex) {
                Logger.getLogger(LogicDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return logic;
    }

    /**
     * Used to load a command from the "scripts".  It first checks if it is
     * loadable and then will check if it is loaded and the will call
     * reload(name) it if it is.
     *
     * @param module Name of Command to be load
     * @param path
     * @return if the logic is loaded
     */
    public boolean load(String module, String path) {
        module = module.toLowerCase();
        String modpath;
        if (path.length() > 13) {
            modpath = path.substring(path.indexOf("Data/Scripts/") + 13);
            modpath = modpath.replace("\\", "/").replace(module, "\n").replace(".java", "\n").substring(1).trim();
        } else {
            modpath = "Logics";
        }
        modpath = modpath.toLowerCase();
        boolean workded = ScriptManager.compile(module, modpath);
        if (workded == false) {
            return false;
        }
        try {
            Logic l = loadLogicModule(module);
        } catch (Exception ex) {
            Logger.getLogger(CommandDatabase.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        modules.put(module, path);
        return true;

    }

    /** Loads the initial database from hardcode, NOT from 'scripts'.
     *  Calls on loadCommand(String) to due such.  Will load the whole directory,
     * to include the "scriptable" files.
     */
    public void loadDB() {
        File logicDir = new File("Data/Scripts/Logics");
        if (logicDir.isDirectory()) {
            File[] logicFiles = logicDir.listFiles();
            for (File f : logicFiles) {
                if (f.isDirectory()) {
                    File[] moreFiles = f.listFiles();
                    for (File f2 : moreFiles) {
                        String logicName = f2.getName().replace(".java", "\n").trim();
                        if (logicName.contains(".class")) {
                            continue;
                        }
                        load(logicName, f2.getPath());
                    }
                } else {
                    String logicName = f.getName().replace(".java", "\n").trim();
                    if (logicName.contains(".class")) {
                        continue;
                    }
                    load(logicName, f.getPath());
                }
            }
        } else {
            Logger.getLogger(CommandDatabase.class.getName()).log(Level.SEVERE, null, new IOException("Can not file Directory: " + logicDir.getPath()));
        }
    }

    /*
     * Removed for now.
     * 
     * @param logic Logic module to add.
     */
    /*private void addModule(String logic) {
    }*/

    /**
     * Will reload any script that is loadable.  That defaults only to 'scripted'
     * logics or other user defined ones.  The hard coded logics are
     * non-loadable while the mud is still running.
     *
     * @param name
     * @param mode
     * @return if the logic is reloaded
     */
    public boolean reload(String name, ScriptReloadMode mode) {

        Logic l = this.generate(name, null);
        if (l == null || l.isLoadable()) {
            if (modules.containsKey(name)) {
                String path = modules.get(name);
                removeLogic(name);
                if (load(name, path)) {
                    for (MudCharacter mob : Mud.Mud.dbg.characterDB.getInstances().getContainer().values()) {
                        if (mob.hasLogic(name)) {
                            mob.delLogic(name);
                            mob.addLogic(name);
                        }
                    }
                    for (Item mob : Mud.Mud.dbg.itemDB.getInstances().getContainer().values()) {
                        if (mob.hasLogic(name)) {
                            mob.delLogic(name);
                            mob.addLogic(name);
                        }
                    }
                    for (Room mob : Mud.Mud.dbg.roomDB.getContainer().values()) {
                        if (mob.hasLogic(name)) {
                            mob.delLogic(name);
                            mob.addLogic(name);
                        }
                    }
                    for (Region mob : Mud.Mud.dbg.regionDB.getContainer().values()) {
                        if (mob.hasLogic(name)) {
                            mob.delLogic(name);
                            mob.addLogic(name);
                        }
                    }
                    for (Portal mob : Mud.Mud.dbg.portalDB.getContainer().values()) {
                        if (mob.hasLogic(name)) {
                            mob.delLogic(name);
                            mob.addLogic(name);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Loads a logic module from binary code.  The directoy it looks in is binDir.
     *
     * @param module Name of the command to load
     * @return Returns a new instance of the logic
     */
    public static Logic loadLogic(String module) {
        Logic logic = null;
        if (module.equals("Logic")) {
            return null;
        }
        try {
            File classesDir = new File(scriptsfolder);
            System.out.println();
            ClassLoader parentLoader = Command.class.getClassLoader();
            URLClassLoader loader1 = new URLClassLoader(new URL[] {classesDir.toURI().toURL()}, parentLoader);
            Class cls1 = loader1.loadClass(classfolder + "." + module);

            System.out.println("Now loading: " + cls1.getName());
            @SuppressWarnings(value = "unchecked")
            Constructor cons = cls1.getConstructor(new Class[] {MudCharacter.class});
            logic = (Logic) cons.newInstance(new Object[] {null});
            return logic;
        } catch (Exception ex) {
            Logger.getLogger(CommandDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return logic;
    }

    private Logic loadLogicModule(String name) {
        if (name.equals("Logics")) {
            return null;
        }
        name = name.toLowerCase();
        Logic logic = null;
        try {
            File classesDir = new File(scriptsfolder);
            ClassLoader parentLoader = Command.class.getClassLoader();
            URLClassLoader loader1 = new URLClassLoader(new URL[] {classesDir.toURI().toURL()}, parentLoader);
            Class cls1 = loader1.loadClass(classfolder + "." + name);
            System.out.println("<------Now loading: " + cls1.getName());
            logic = (Logic) cls1.newInstance();
            return logic;
        } catch (Exception ex) {
            Logger.getLogger(CommandDatabase.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return logic;
    }

    /** Removes a logic from the Mapping.  Checks to ensure the command is
     * 'loadable' first before it removes the command.  Can only remove
     * non-loadable commands.
     */
    private void removeLogic(String module) {
        if (generate(module, null).isLoadable()) {
            modules.remove(module);
        }
    }
}
