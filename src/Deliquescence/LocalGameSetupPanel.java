/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Deliquescence;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

/**
 *
 * @author Josh
 */
public class LocalGameSetupPanel extends javax.swing.JPanel {

    GameManagerPanel gameManager;

    /**
     * This panel has settings for creating new singleplayer games.
     */
    public LocalGameSetupPanel(GameManagerPanel gameManager) {
        initComponents();

        this.gameManager = gameManager;
        //This.parent == A tab in GameManagerPanel
        //(that^).Parent == GameManagerPanel

//        Config.refreshables.add(this);
//        refreshConfig();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        SplitPane = new javax.swing.JSplitPane();
        NumbersPanel = new javax.swing.JPanel();
        gameSlidersPanel = new Deliquescence.GameSlidersPanel();
        StartButton = new javax.swing.JButton();
        PlayerNamesPanel = new javax.swing.JPanel();
        gameSettingsPanel = new Deliquescence.GameSettingsPanel();
        PlayerNamesLabel = new javax.swing.JLabel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        SplitPane.setDividerLocation(550);
        SplitPane.setMinimumSize(new java.awt.Dimension(100, 202));
        SplitPane.setPreferredSize(new java.awt.Dimension(100, 102));

        NumbersPanel.setMinimumSize(new java.awt.Dimension(600, 400));
        NumbersPanel.setPreferredSize(new java.awt.Dimension(1000, 600));
        NumbersPanel.setLayout(new javax.swing.BoxLayout(NumbersPanel, javax.swing.BoxLayout.Y_AXIS));
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

        PlayerNamesLabel.setText("Player Names:");
        PlayerNamesLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        PlayerNamesPanel.add(PlayerNamesLabel);

        //Add player text boxes
        playerTextFields = new JTextField[Deliquescence.Config.getInt("MAX_PLAYERS")+1];
        for (int i = 1; i <= Deliquescence.Config.getInt("MAX_PLAYERS"); i++) {
            final JTextField textField = new JTextField();
            playerTextFields[i] = textField;
            textField.setName(Integer.toString(i));
            textField.setText(Deliquescence.Config.getDefaultPlayerName(i));
            textField.setMaximumSize(new Dimension(400, 20));

            textField.addFocusListener(new FocusListener(){
                @Override
                public void focusGained(FocusEvent  e){
                    if (textField.getText().contains("Player ")) {
                        textField.setText("");
                    }
                }
                @Override
                public void focusLost(FocusEvent  e) {
                    if (textField.getText().equals("")) {
                        textField.setText("Player " + textField.getName());
                    }
                }
            });

            PlayerNamesPanel.add(playerTextFields[i]);
        }

        SplitPane.setRightComponent(PlayerNamesPanel);

        add(SplitPane);
    }// </editor-fold>//GEN-END:initComponents

    private void StartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StartButtonActionPerformed
        String[] names = new String[Config.getInt("MAX_PLAYERS") + 1];
        for (int i = 1; i <= Config.getInt("MAX_PLAYERS"); i++) {
            names[i] = playerTextFields[i].getText();//Todo if MAX_PLAYERS is changed while running and game not restarted, suspect AOB here
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

        GamePanel gamePanel = new GamePanel(this.gameManager, gameSlidersPanel.getPlayers(), gameSlidersPanel.getRows(), gameSlidersPanel.getColumns(), names, gameSettingsPanel.EnableRNGButton(), gameSettingsPanel.RandomStartPlayer(), timerLength, timerAction);
        gameManager.addGameTab(gamePanel);
    }//GEN-LAST:event_StartButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel NumbersPanel;
    private javax.swing.JLabel PlayerNamesLabel;
    private JTextField[] playerTextFields;
    private javax.swing.JPanel PlayerNamesPanel;
    private javax.swing.JSplitPane SplitPane;
    private javax.swing.JButton StartButton;
    private javax.swing.ButtonGroup buttonGroup1;
    private Deliquescence.GameSettingsPanel gameSettingsPanel;
    private Deliquescence.GameSlidersPanel gameSlidersPanel;
    // End of variables declaration//GEN-END:variables
}
