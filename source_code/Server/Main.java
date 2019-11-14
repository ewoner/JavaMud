package Server;

import Mud.Exceptions.MudException;
import Mud.Mud;
import Server.ServerExceptions.ServerException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The main enterence into the mud.  Runs both the mud class and the server.
 *
 * @author Brion Lang
 *  Date: 17 Jam 2009
 *
 * Version 1.0.1
 * 1.  Added a "catch" block to handle normal errors to prevent the mud from 
 * crashing
 * Version 1.0.0
 */
public class Main extends Thread {

    private ListenManager telnetlistener;
    private NewConnectionManager telnetconnectionmanager;
    /**
     * The game itself on the "server".
     */
    public static Mud game;

    /**
     * Creeats a new server.  Initializes the ListenManager, Connection Manager
     * and the mud.  Loads all databases.  At the end of this call the mud is
     * running.
     */
    public Main() {
        try {
            telnetlistener = new ListenManager();
            telnetconnectionmanager = new NewConnectionManager();
            telnetlistener.setConnectionManager(telnetconnectionmanager);
            telnetlistener.addPort(9909);
            game = new Mud();
            game.loadall();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }

    /**
     * The enteracne into the main class from the outside world.
     * Starts a new main class and sets everything in motion before begining
     * the main game loop.
     *
     * Will close down everything after the mud shuts down.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Main main = new Main();
        main.telnetlistener.setRunning(true);
        main.telnetlistener.start();
        while (Main.game.isRunning()) {
            try {
                main.telnetconnectionmanager.manage();
                Main.game.executeLoop();
                Main.yield();
            } catch (ServerException ex) {
                Logger.getLogger(ex.getTagName()).log(Level.SEVERE, null, ex);
                Logger.getLogger(ex.getTagName()).log(Level.SEVERE, null, "AKA = " + ex.getException());
            } catch (Exception ex) {
                Logger.getLogger(main.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            main.telnetconnectionmanager.shutdown();
        } catch (ServerException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        main.telnetlistener.setRunning(false);
        //Main.game.saveall();
        Mud.dbg.purge();
        System.exit(0);
    }
}
