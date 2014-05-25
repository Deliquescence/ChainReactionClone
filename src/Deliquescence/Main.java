/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Deliquescence;

import Deliquescence.Network.BrowserPanel;
import Deliquescence.Network.LANSetupPanel;
import Deliquescence.Network.ServerSetupPanel;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Josh
 */
public class Main extends JFrame implements Refreshable {

    public Main() {
        Config.init();
        Config.refreshables.add(this);
        refreshConfig();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Config.getInt("FRAME_WIDTH"), Config.getInt("FRAME_HEIGHT"));

        setTitle(Config.getString("TITLE"));

        JTabbedPane MainTabbedPane = new JTabbedPane();

        GameManagerPanel localGamesPanel = new GameManagerPanel();
        GameManagerPanel networkGamesPanel = new GameManagerPanel();//Todo networking
        GameManagerPanel networkGamesListPanel = new GameManagerPanel();
        ConfigPanel configPanel = new ConfigPanel();
        ColorConfigPanel colorConfigPanel = new ColorConfigPanel();

        localGamesPanel.addTab("Create a game", new LocalGameSetupPanel(localGamesPanel));

        networkGamesPanel.addTab("Games", networkGamesListPanel);
        networkGamesPanel.addTab("Create a LAN game", new LANSetupPanel());
        networkGamesPanel.addTab("Create a server game", new ServerSetupPanel());
        networkGamesPanel.addTab("Join a game", new BrowserPanel(networkGamesPanel, networkGamesListPanel));

        MainTabbedPane.addTab("Local Games", localGamesPanel);
        MainTabbedPane.addTab("Network Games", networkGamesPanel);

        MainTabbedPane.addTab("Configuration", configPanel);
        MainTabbedPane.addTab("Color Config", colorConfigPanel);

        add(MainTabbedPane);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame ex = new Main();
                ex.setVisible(true);
            }
        });
    }

    @Override
    public void refreshConfig() {
        setTitle(Config.getString("TITLE"));
    }
}
