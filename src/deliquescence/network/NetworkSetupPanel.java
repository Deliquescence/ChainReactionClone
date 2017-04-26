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
package Deliquescence.Network;

import Deliquescence.Panel.GameManager;
import com.esotericsoftware.minlog.Log;
import java.awt.Dimension;
import java.io.IOException;
import java.util.concurrent.Callable;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Deliquescence <Deliquescence1@gmail.com>
 */
public class NetworkSetupPanel extends javax.swing.JPanel {

    GameManager gameManager;
    GameManager gameListPanel;

    private final Callable doneButtonAction;

    /**
     * Creates new form NetworkSetupPanel in tabbed form
     *
     * @param gameManager The parent game manager for this when used in tab form
     * @param gameList The game manager to add the new game to
     */
    public NetworkSetupPanel(GameManager gameManager, GameManager gameList) {
        this.gameManager = gameManager;
        this.gameListPanel = gameList;
        initComponents();

        doneButtonAction = new Callable() {

            @Override
            public Boolean call() throws Exception {
                GameManager man = (GameManager) NetworkSetupPanel.this.getParent().getParent();//Sketchy
                man.switchToTabByTitle("Games");

                NetworkGameSettings settings = getCurrentSettings();

                try {
                    gameListPanel.addTab("Hosted Game", new NetworkGameViewer(gameListPanel, settings, LocalPlayersSlider.getValue()), false, true);
                } catch (IOException ex) {
                    Log.error("Start server error", ex);
                    JOptionPane.showMessageDialog(null, "Error trying to start server", "Error on server start", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                return true;
            }
        };
    }

    public NetworkSetupPanel(final JFrame parent, final GameServer server) {
        initComponents();
        this.doneButton.setText("Done");

        doneButtonAction = new Callable() {

            @Override
            public Boolean call() throws Exception {
                server.setSettings(NetworkSetupPanel.this.getCurrentSettings());
                parent.setVisible(false);
                parent.dispose();
                return true;
            }
        };
    }

    public NetworkGameSettings getCurrentSettings() {
        NetworkGameSettings settings = new NetworkGameSettings();

        settings.RNGEnabled = gameSettings1.EnableRNGButton();
        settings.cols = gameSliders1.getColumns();
        settings.totalPlayers = gameSliders1.getPlayers();
        settings.randomStartingPlayer = gameSettings1.RandomStartPlayer();
        settings.rows = gameSliders1.getRows();

        //settings.timerAction = TOdO
        settings.timerLength = gameSettings1.TimerLength();
        settings.turnTimerEnabled = gameSettings1.EnableTurnTimer();

        return settings;
    }

    public static void popupSettingsEditor(GameServer server) {
        JFrame frame = new JFrame();
        NetworkSetupPanel nsp = new NetworkSetupPanel(frame, server);
        frame.add(nsp);
        frame.setMinimumSize(new Dimension(400, 400));
        frame.setPreferredSize(new Dimension(500, 500));
        frame.setVisible(true);
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        LeftPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        LocalPlayersSlider = new javax.swing.JSlider();
        jSeparator1 = new javax.swing.JSeparator();
        gameSliders1 = new Deliquescence.Panel.GameSliders();
        doneButton = new javax.swing.JButton();
        RightPanel = new javax.swing.JPanel();
        gameSettings1 = new Deliquescence.Panel.GameSettings();

        setMinimumSize(new java.awt.Dimension(100, 102));
        setPreferredSize(new java.awt.Dimension(100, 102));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        jSplitPane1.setDividerLocation(550);
        jSplitPane1.setMinimumSize(new java.awt.Dimension(100, 202));
        jSplitPane1.setPreferredSize(new java.awt.Dimension(100, 102));

        LeftPanel.setMinimumSize(new java.awt.Dimension(200, 400));
        LeftPanel.setPreferredSize(new java.awt.Dimension(1000, 600));
        LeftPanel.setLayout(new javax.swing.BoxLayout(LeftPanel, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setLabelFor(LocalPlayersSlider);
        jLabel1.setText("Number of players on this machine:");
        LeftPanel.add(jLabel1);

        LocalPlayersSlider.setMajorTickSpacing(1);
        LocalPlayersSlider.setMaximum(8);
        LocalPlayersSlider.setMinimum(1);
        LocalPlayersSlider.setMinorTickSpacing(1);
        LocalPlayersSlider.setPaintLabels(true);
        LocalPlayersSlider.setPaintTicks(true);
        LocalPlayersSlider.setSnapToTicks(true);
        LocalPlayersSlider.setValue(1);
        LeftPanel.add(LocalPlayersSlider);
        LeftPanel.add(jSeparator1);
        LeftPanel.add(gameSliders1);

        doneButton.setText("Start");
        doneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doneButtonActionPerformed(evt);
            }
        });
        LeftPanel.add(doneButton);

        jSplitPane1.setLeftComponent(LeftPanel);

        RightPanel.setMaximumSize(new java.awt.Dimension(175, 60));
        RightPanel.setMinimumSize(new java.awt.Dimension(175, 60));
        RightPanel.setLayout(new java.awt.GridLayout(1, 0));

        gameSettings1.setMaximumSize(new java.awt.Dimension(175, 60));
        RightPanel.add(gameSettings1);

        jSplitPane1.setRightComponent(RightPanel);

        add(jSplitPane1);
    }// </editor-fold>//GEN-END:initComponents

    private void doneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doneButtonActionPerformed
        try {
            this.doneButtonAction.call();
        } catch (Exception ex) {
            Log.error("NetworkSetupPanel", ex);
        }
    }//GEN-LAST:event_doneButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel LeftPanel;
    private javax.swing.JSlider LocalPlayersSlider;
    private javax.swing.JPanel RightPanel;
    private javax.swing.JButton doneButton;
    private Deliquescence.Panel.GameSettings gameSettings1;
    private Deliquescence.Panel.GameSliders gameSliders1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    // End of variables declaration//GEN-END:variables
}
