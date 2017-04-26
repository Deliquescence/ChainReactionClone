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
package deliquescence.panel;

import deliquescence.Config;
import deliquescence.Utils;

/**
 * A panel for setting up a local game.
 * Subpanels {@link GameSliders} and {@link GameSettings}
 */
public class LocalGameSetup extends javax.swing.JPanel {

	GameManager gameManager;

	/**
	 * Creates new form LocalGameSetup with the specified GameManager parent.
	 *
	 * @param gameManager The parent {@link GameManager} of this setup panel.
	 */
	public LocalGameSetup(GameManager gameManager) {
		initComponents();

		this.gameManager = gameManager;
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        SplitPane = new javax.swing.JSplitPane();
        NumbersPanel = new javax.swing.JPanel();
        gameSlidersPanel = new deliquescence.panel.GameSliders();
        StartButton = new javax.swing.JButton();
        PlayerNamesPanel = new javax.swing.JPanel();
        gameSettingsPanel = new deliquescence.panel.GameSettings();
        playerNames1 = new deliquescence.panel.PlayerNames();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        SplitPane.setDividerLocation(550);
        SplitPane.setMinimumSize(new java.awt.Dimension(100, 202));
        SplitPane.setPreferredSize(new java.awt.Dimension(100, 102));

        NumbersPanel.setMinimumSize(new java.awt.Dimension(200, 400));
        NumbersPanel.setPreferredSize(new java.awt.Dimension(1000, 600));
        NumbersPanel.setLayout(new javax.swing.BoxLayout(NumbersPanel, javax.swing.BoxLayout.Y_AXIS));

        gameSlidersPanel.setMinimumSize(new java.awt.Dimension(50, 150));
        NumbersPanel.add(gameSlidersPanel);

        StartButton.setText("Start");
        StartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartButtonActionPerformed(evt);
            }
        });
        NumbersPanel.add(StartButton);

        SplitPane.setLeftComponent(NumbersPanel);

        PlayerNamesPanel.setMaximumSize(new java.awt.Dimension(175, 60));
        PlayerNamesPanel.setMinimumSize(new java.awt.Dimension(175, 60));
        PlayerNamesPanel.setLayout(new javax.swing.BoxLayout(PlayerNamesPanel, javax.swing.BoxLayout.Y_AXIS));
        PlayerNamesPanel.add(gameSettingsPanel);
        PlayerNamesPanel.add(playerNames1);

        SplitPane.setRightComponent(PlayerNamesPanel);

        add(SplitPane);
    }// </editor-fold>//GEN-END:initComponents

    private void StartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StartButtonActionPerformed
		String[] names = new String[Config.getInt("MAX_PLAYERS")];
		for (int i = 1; i <= Config.getInt("MAX_PLAYERS"); i++) {
			names[i - 1] = playerNames1.getPlayerName(i);
		}
		int timerLength;
		if (gameSettingsPanel.EnableTurnTimer()) {
			timerLength = gameSettingsPanel.TimerLength();
		} else {
			timerLength = 0;
		}
		int timerAction = 0;//0 skip, 1 rng
		if (gameSettingsPanel.timerExpireRNG()) {
			timerAction = 1;
		}

		GamePanel gamePanel = new GamePanel(this.gameManager, gameSlidersPanel.getPlayers(), gameSlidersPanel.getRows(), gameSlidersPanel.getColumns(), Utils.namesToPlayers(names), gameSettingsPanel.EnableRNGButton(), gameSettingsPanel.RandomStartPlayer(), timerLength, timerAction);
		gameManager.addGameTab(gamePanel);
    }//GEN-LAST:event_StartButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel NumbersPanel;
    private javax.swing.JPanel PlayerNamesPanel;
    private javax.swing.JSplitPane SplitPane;
    private javax.swing.JButton StartButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private deliquescence.panel.GameSettings gameSettingsPanel;
    private deliquescence.panel.GameSliders gameSlidersPanel;
    private deliquescence.panel.PlayerNames playerNames1;
    // End of variables declaration//GEN-END:variables
}