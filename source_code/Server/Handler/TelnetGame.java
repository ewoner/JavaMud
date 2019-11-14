/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Handler;

import Actions.Action;
import Actions.ActionType;
import Entities.Account;
import Entities.MudCharacter;
import Mud.Mud;
import Server.Main;
import Server.NewConnection;
import Server.Protocol.Telnet;
import Server.Reporter.TelnetReporter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class TelnetGame extends NewHandler<Telnet, String> {

    private Account m_account;
    private MudCharacter m_character;
    private Mud g_game;

    public TelnetGame(NewConnection<Telnet> connection, Account m_account, MudCharacter m_char) {
        super(connection);
        this.m_account = m_account;
        this.m_character = m_char;
        g_game = Main.game;
    }

    @Override
    public void handle(String p_data) {
        try {
            Logger.getLogger(TelnetGame.class.getName()).info(m_character.getName()+" is trying to exucute \""+p_data+"\".");
            g_game.doAction(ActionType.command, m_character.getID(), 0, 0, 0, p_data);
        } catch (Exception ex) {
            Logger.getLogger(TelnetGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enter() {
        // hang up the existing character if it's already logged in.
        if (m_character.isLoggedin()) {
            getConnection().getProtocol().sendString(
                    getConnection(),
                    "<#FF0000>Hanging up existing connection...\r\n");
            m_character.DoAction(new Action(ActionType.hangup));
        }

        m_character.addExistingLogic(new TelnetReporter(m_character, getConnection()));
        try {
            // show the news
            g_game.doAction(ActionType.command, m_character.getID(), 0, 0, 0, "/news");
        } catch (Exception ex) {
            Logger.getLogger(TelnetGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            // log in the player
            g_game.doAction(ActionType.enterrealm, m_character.getID(), 0, 0, 0, null);
        } catch (Exception ex) {
            Logger.getLogger(TelnetGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void leave() {
        try {
            g_game.doAction(ActionType.leaverealm, m_character.getID(), 0, 0, 0, null);
        } catch (Exception ex) {
            Logger.getLogger(TelnetGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void hungUp() {
    }

    public void flooded() {
    }
}
