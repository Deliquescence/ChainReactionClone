/* 
 * Copyright (c) 2014, Deliquescence <Deliquescence1@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * - Neither the name of the copyright holder nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package Deliquescence;

import Deliquescence.Network.BrowserPanel;
import Deliquescence.Network.LANSetupPanel;
import Deliquescence.Network.ServerSetupPanel;
import Deliquescence.Panel.ColorConfigPanel;
import Deliquescence.Panel.ConfigPanel;
import Deliquescence.Panel.GameManager;
import Deliquescence.Panel.LocalGameSetup;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 * The starting point for ChainReactionClone
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

        GameManager localGamesPanel = new GameManager();
        GameManager networkGamesPanel = new GameManager();//Todo networking
        GameManager networkGamesListPanel = new GameManager();
        ConfigPanel configPanel = new ConfigPanel();
        ColorConfigPanel colorConfigPanel = new ColorConfigPanel();

        localGamesPanel.addTab("Create a game", new LocalGameSetup(localGamesPanel));

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
