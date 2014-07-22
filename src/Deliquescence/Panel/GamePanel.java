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
package Deliquescence.Panel;

import Deliquescence.Board;
import Deliquescence.Player;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * This panel has the game board, as well as the other display features and buttons for playing the game.
 *
 * @author Josh
 */
public class GamePanel extends javax.swing.JPanel {

    GameManager gameManager;
    public Board gameBoard;
    Timer timer;

    int totalTime;
    int currentTime;
    final int timerAction;

    /**
     * Creates a new GamePanel with all the specified settings.
     *
     * @param gameManager The parent {@link GameManager} of this GamePanel.
     * @param players The number of players in the game.
     * @param rows The number of rows in the game.
     * @param columns The number of columns in the game.
     * @param playerNames Array containing the names of the players.
     * @param RNGEnabled True if the RNG button will be enabled.
     * @param RandomizePlayer True to choose a random player to start.
     * @param timerLength The length of the timer (0 if disabled)
     * @param timeAction 0 for skip turn, 1 for RNG turn
     */
    public GamePanel(GameManager gameManager, int players, int rows, int columns, String[] playerNames, boolean RNGEnabled, boolean RandomizePlayer, int timerLength, int timeAction) {
        initComponents();
        this.gameManager = gameManager;

        RNGButton.setEnabled(RNGEnabled);

        this.totalTime = timerLength;
        this.currentTime = totalTime;
        this.timerAction = timeAction;

        timer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                TimerCounter.setText(Integer.toString(currentTime));
                if (currentTime > 0) {
                    currentTime--;
                } else {
                    currentTime = totalTime;
                    if (timerAction == 0) {//Skip
                        gameBoard.SkipTurn();
                    } else if (timerAction == 1) {//RNG
                        gameBoard.RNGTurn();
                    }
                }
            }
        }
        );

        makeBoard(players, rows, columns, playerNames, RandomizePlayer);

        add(gameBoard);
        if (totalTime != 0) {
            TimerLabel1.setText("Timer:");
            //startTimer();
            //TODO stop when lose focus
            //todo fix wins with early skipped turns
            //maybe dont start until after first turns
        }
    }

    protected void makeBoard(int players, int rows, int columns, String[] playerNames, boolean RandomizePlayer) {
        gameBoard = new Board(this, players, rows, columns, playerNames, RandomizePlayer);
    }

    /**
     * Reset the turn timer to its starting value.
     */
    public void resetTimer() {//TODO glitch when turn done near end of timer
        currentTime = totalTime;
    }

    /**
     * End the turn timer.
     */
    public void stopTimer() {
        if (totalTime != 0) {
            timer.stop();
        }
    }

    /**
     * Start the turn timer.
     */
    public void startTimer() {
        if (totalTime != 0) {
            timer.start();
        }
    }

    /**
     * Set the color and text of the large player turn label.
     *
     * @param color The color of the label .
     * @param text The text of the label, usually "Player _'s turn"
     */
    public void setPlayerStatus(Color color, String text) {
        PlayerStatusLabel.setForeground(color);
        PlayerStatusLabel.setText(text);
    }

    /**
     * Update the list of players on the side so it displays if a player has died.
     *
     * @param players Array of all the players that are/were part of this game.
     */
    @SuppressWarnings("unchecked") //attributes.put()
    public void refreshPlayerList(Player[] players) {
        PlayerListPanel.removeAll();
        JLabel title = new JLabel("Players:");
        title.setFont(new Font("Gulium", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setHorizontalTextPosition(SwingConstants.CENTER);
        PlayerListPanel.add(title);
        for (Player p : players) {
            String labelText = p.getDisplayName();
            Font baseFont = new Font("Gulium", Font.BOLD, 16);
            Font labelFont;
            if (!p.isAlive()) {
                java.util.Map attributes = baseFont.getAttributes();
                attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
                attributes.put(TextAttribute.FONT, new Font("Gulium", Font.PLAIN, 16));

                labelFont = new Font(attributes);
            } else {
                labelFont = baseFont;
            }

            JLabel label = new JLabel(labelText);
            label.setForeground(p.getColor());
            label.setFont(labelFont);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setHorizontalTextPosition(SwingConstants.CENTER);
            PlayerListPanel.add(label);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        RightSidePanel = new javax.swing.JPanel();
        UndoButton = new javax.swing.JButton();
        CloseButton = new javax.swing.JButton();
        RightCenterPanel = new javax.swing.JPanel();
        RNGPanel = new javax.swing.JPanel();
        RNGButton = new javax.swing.JButton();
        TimerPanel = new javax.swing.JPanel();
        TimerLabel1 = new javax.swing.JLabel();
        TimerCounter = new javax.swing.JLabel();
        PlayerListPanel = new javax.swing.JPanel();
        PlayerStatusLabel = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        RightSidePanel.setLayout(new java.awt.BorderLayout());

        UndoButton.setText("Undo");
        UndoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UndoButtonActionPerformed(evt);
            }
        });
        RightSidePanel.add(UndoButton, java.awt.BorderLayout.PAGE_START);

        CloseButton.setText("End Game");
        CloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseButtonActionPerformed(evt);
            }
        });
        RightSidePanel.add(CloseButton, java.awt.BorderLayout.PAGE_END);

        RightCenterPanel.setLayout(new javax.swing.BoxLayout(RightCenterPanel, javax.swing.BoxLayout.Y_AXIS));

        RNGButton.setText("RNG");
        RNGButton.setEnabled(false);
        RNGButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RNGButtonActionPerformed(evt);
            }
        });
        RNGPanel.add(RNGButton);

        RightCenterPanel.add(RNGPanel);

        TimerPanel.setLayout(new java.awt.GridLayout(4, 1));

        TimerLabel1.setFont(new java.awt.Font("Gulim", 0, 18)); // NOI18N
        TimerLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TimerLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        TimerPanel.add(TimerLabel1);

        TimerCounter.setFont(new java.awt.Font("Gulim", 1, 24)); // NOI18N
        TimerCounter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TimerPanel.add(TimerCounter);

        RightCenterPanel.add(TimerPanel);

        PlayerListPanel.setLayout(new javax.swing.BoxLayout(PlayerListPanel, javax.swing.BoxLayout.Y_AXIS));
        RightCenterPanel.add(PlayerListPanel);

        RightSidePanel.add(RightCenterPanel, java.awt.BorderLayout.CENTER);

        add(RightSidePanel, java.awt.BorderLayout.LINE_END);

        PlayerStatusLabel.setFont(new java.awt.Font("Gulim", 1, 36)); // NOI18N
        PlayerStatusLabel.setText("Player's turn");
        PlayerStatusLabel.setPreferredSize(new java.awt.Dimension(60, 50));
        add(PlayerStatusLabel, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void CloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseButtonActionPerformed
        this.gameManager.removeTab(this);
    }//GEN-LAST:event_CloseButtonActionPerformed

    private void UndoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UndoButtonActionPerformed
        gameBoard.undo();
    }//GEN-LAST:event_UndoButtonActionPerformed

    private void RNGButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RNGButtonActionPerformed
        gameBoard.RNGTurn();
    }//GEN-LAST:event_RNGButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CloseButton;
    private javax.swing.JPanel PlayerListPanel;
    private javax.swing.JLabel PlayerStatusLabel;
    private javax.swing.JButton RNGButton;
    private javax.swing.JPanel RNGPanel;
    private javax.swing.JPanel RightCenterPanel;
    private javax.swing.JPanel RightSidePanel;
    private javax.swing.JLabel TimerCounter;
    private javax.swing.JLabel TimerLabel1;
    private javax.swing.JPanel TimerPanel;
    private javax.swing.JButton UndoButton;
    // End of variables declaration//GEN-END:variables
}
