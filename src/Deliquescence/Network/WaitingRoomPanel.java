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

import Deliquescence.Config;
import static Deliquescence.Network.PacketTitle.GameStartPacket;
import Deliquescence.Panel.GameManager;
import Deliquescence.Player;
import com.esotericsoftware.minlog.Log;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.Callable;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Josh
 */
public class WaitingRoomPanel extends javax.swing.JPanel {

    InetAddress serverAddress;
    GameManager gameList;
    NetworkGameViewer ngv;
    int numLocalPlayers;
    GameClient client;
    GameServer server;
    boolean isServer;
    private Thread nameUpdateThread;
    NamesEditor names;

    public NetworkGamePanel networkGamePanel;

    /**
     * Creates new form WaitingRoomPanel
     */
    public WaitingRoomPanel(GameManager listPanel, GameClient client, int numLocalPlayers, NetworkGameViewer ngv) {
        initComponents();

        this.gameList = listPanel;
        this.numLocalPlayers = numLocalPlayers;
        this.client = client;
        this.serverAddress = client.serverAddress;
        serverAddressLabel.setText("Server Address: " + serverAddress.getHostName());
        this.ngv = ngv;
        isServer = false;
        client.wrp = this;

        names = new NamesEditor();
        NamesPanel.add(names);

        updateLocalNames();

        NamesPanel.setPreferredSize(new Dimension(800, 20 * (numLocalPlayers + 1)));
    }

    public WaitingRoomPanel(GameManager listPanel, GameServer server, int numLocalPlayers, NetworkGameViewer ngv) {
        initComponents();

        this.gameList = listPanel;
        this.numLocalPlayers = numLocalPlayers;
        this.server = server;
        this.ngv = ngv;
        isServer = true;
        try {
            this.serverAddress = InetAddress.getLocalHost();//Since is server
            serverAddressLabel.setText("Server Address: " + this.serverAddress.getHostName());
        } catch (UnknownHostException ex) {
        }

        try {
            this.client = new GameClient();

            Networking.register(client);

            client.start();

            NetworkGameSettings settings = new NetworkGameSettings();
            client.settings = settings;

            client.connect(5000, "localhost", Config.getInt("NETWORK_PORT"));

            client.wrp = this;
        } catch (Exception e) {
            Log.error(WaitingRoomPanel.class.getName(), "", e);
        }

        names = new NamesEditor();
        NamesPanel.add(names);
        updateLocalNames();
        NamesPanel.setPreferredSize(new Dimension(800, 20 * (numLocalPlayers + 1)));

        this.nameUpdateThread = Updater.createUpdater("names", new Callable() {

            @Override
            public Object call() throws Exception {
                WaitingRoomPanel.this.server.getNames();
                return 0;
            }
        });
        this.nameUpdateThread.start();
    }

    public void updateLocalNames() {
        String[] theNames = names.getPlayerNames();

        if (client.localPlayers == null) { //Need to init
            client.localPlayers = new Player[numLocalPlayers];
            for (int i = 0; i < theNames.length; i++) {
                client.localPlayers[i] = new Player(i, theNames[i]);
            }
        }

        for (int i = 0; i < theNames.length; i++) {
            client.localPlayers[i].setName(theNames[i]);
        }

        //Players display
        int i = 0;
        String[] displayNames = new String[this.client.allPlayers.size()];
        for (Player p : this.client.getAllPlayers()) {
            displayNames[i] = p.getDisplayName();
            i++;
        }
        playersjList.setListData(displayNames);
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

        StartButton = new javax.swing.JButton();
        editSettingsButton = new javax.swing.JButton();
        serverAddressLabel = new javax.swing.JLabel();
        NamesPanel = new javax.swing.JPanel();
        playersListPanel = new javax.swing.JPanel();
        playersListLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        playersjList = new javax.swing.JList();
        LeaveButton = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        StartButton.setText("Start");
        StartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartButtonActionPerformedWaitingRoom(evt);
            }
        });
        add(StartButton);

        editSettingsButton.setText("Edit Settings (Host Only) (TODO)");
        editSettingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSettingsButtonActionPerformed(evt);
            }
        });
        add(editSettingsButton);

        serverAddressLabel.setText("Server Address:");
        serverAddressLabel.setText("Server Address: " + serverAddress);
        add(serverAddressLabel);

        NamesPanel.setMaximumSize(new java.awt.Dimension(999999, 2000));
        NamesPanel.setMinimumSize(new java.awt.Dimension(200, 25));
        NamesPanel.setPreferredSize(new java.awt.Dimension(40, 20));
        NamesPanel.setLayout(new javax.swing.BoxLayout(NamesPanel, javax.swing.BoxLayout.Y_AXIS));
        add(NamesPanel);

        playersListPanel.setLayout(new javax.swing.BoxLayout(playersListPanel, javax.swing.BoxLayout.Y_AXIS));

        playersListLabel.setText("Players:");
        playersListPanel.add(playersListLabel);

        playersjList.setModel(new DefaultListModel());
        playersjList.setMaximumSize(new java.awt.Dimension(0, 500));
        jScrollPane1.setViewportView(playersjList);

        playersListPanel.add(jScrollPane1);

        add(playersListPanel);

        LeaveButton.setText("Exit");
        LeaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LeaveButtonActionPerformed(evt);
            }
        });
        add(LeaveButton);
    }// </editor-fold>//GEN-END:initComponents

    private void LeaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LeaveButtonActionPerformed
        this.gameList.removeTab(this);//todo cleaner exit of server and client
    }//GEN-LAST:event_LeaveButtonActionPerformed

    private void StartButtonActionPerformedWaitingRoom(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StartButtonActionPerformedWaitingRoom
        if (isServer) {
            startGame(server);
        } else {
            Log.info("Only server can start");
            JOptionPane.showMessageDialog(null, "Only server can start", "Silly", JOptionPane.INFORMATION_MESSAGE);//Todo ready up
        }
    }//GEN-LAST:event_StartButtonActionPerformedWaitingRoom

    private void editSettingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSettingsButtonActionPerformed
        if (isServer) {
            NetworkSetupPanel.popupSettingsEditor(this.server);
        }
    }//GEN-LAST:event_editSettingsButtonActionPerformed

    public void startGame(GameClient client) {
        Log.trace("wrp.startgame");

        this.networkGamePanel = new NetworkGamePanel(
                gameList,
                client.settings.totalPlayers,
                client.settings.rows,
                client.settings.cols,
                client.getAllPlayers().toArray(new Player[0]),
                client.settings.RNGEnabled, 0, 0,
                server,
                client
        );
        this.ngv.displayGame(this.networkGamePanel);
    }

    private void startGame(GameServer server) {
        this.nameUpdateThread.interrupt();
        server.getNames();

        try {
            Thread.sleep(1111); //Really need names to update properly
        } catch (InterruptedException ex) {
        }

        NetworkPacket p = new NetworkPacket(GameStartPacket);

        //Handle random starting player
        Random rand = new Random();
        p.setData("randomStart", server.getSettings().randomStartingPlayer);
        if (server.getSettings().randomStartingPlayer) {
            Player[] plays = server.getAllPlayers().toArray(new Player[0]);

            //Shuffle order of players
            for (int i = plays.length - 1; i > 0; i--) {
                int index = rand.nextInt(i + 1);
                int a = plays[index].getNumber();
                plays[index].setNumber(plays[i].getNumber());
                plays[i].setNumber(a);
            }

            try {
                server.updateNames();
                Thread.sleep(1111); //Really need names to update properly
            } catch (InterruptedException ex) {
            }

            //Random start
            Player randomPlayer = plays[rand.nextInt(server.allPlayers.size())];
            p.setData("startPlayer", randomPlayer);
            try {
                Thread.sleep(1111); //Really need names to update properly
            } catch (InterruptedException ex) {
            }
        }

        server.sendToAllTCP(p);

        this.networkGamePanel = new NetworkGamePanel(gameList, server.getSettings().totalPlayers, server.getSettings().rows, server.getSettings().cols, server.getAllPlayers().toArray(new Player[0]), server.getSettings().RNGEnabled, 0, 0, server, client);
        ngv.displayGame(networkGamePanel);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton LeaveButton;
    private javax.swing.JPanel NamesPanel;
    private javax.swing.JButton StartButton;
    private javax.swing.JButton editSettingsButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel playersListLabel;
    private javax.swing.JPanel playersListPanel;
    private javax.swing.JList playersjList;
    private javax.swing.JLabel serverAddressLabel;
    // End of variables declaration//GEN-END:variables

    //This doesnt comply with DRY, but extending panel.playerNames wasnt working
    class NamesEditor extends javax.swing.JPanel {

        private JTextField[] playerTextFields;
        private int numPlayers;

        /**
         * Creates new form PlayerNames
         */
        public NamesEditor() {
            initComponents();
            numPlayers = numLocalPlayers;
            playerTextFields = new JTextField[numPlayers + 1];
            makeTextFields(null);

        }

        /**
         * Gets the configured name of a player from its text field.
         *
         * @param ID The id of the numLocalPlayers name to be found
         *
         * @return The name of the player
         */
        public String[] getPlayerNames() {
            String[] out = new String[numPlayers];
            for (int i = 0; i < out.length; i++) {
                out[i] = playerTextFields[i + 1].getText();//playerTextFields is 1 based index
            }
            return out;
        }

        private void makeTextFields(String[] preConfiguredNames) {
            fieldsPanel.removeAll();

            //Add player text boxes
            for (int i = 1; i <= numPlayers; i++) {

                //If the config is changed, do not overwrite the old player name
                JTextField temp = null;
                try {
                    temp = new JTextField(preConfiguredNames[i]);
                } catch (Exception e) {
                }
                if (temp == null || "".equals(temp.getText())) { //temp.getText() == ""
                    temp = new JTextField(Deliquescence.Config.getDefaultPlayerName(i));
                }
                final JTextField textField = temp;
                playerTextFields[i] = textField;

                textField.setName(Integer.toString(i));

                textField.setMaximumSize(new Dimension(800, 20));
                textField.setMinimumSize(new Dimension(200, 20));
                textField.setPreferredSize(new Dimension(400, 20));

                textField.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        if (textField.getText().contains("Player ")) {
                            textField.setText("");
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        if (textField.getText().equals("")) {
                            textField.setText("Player " + textField.getName());
                        }
                    }
                });
                playerTextFields[i].setVisible(true);
                fieldsPanel.add(playerTextFields[i]);
            }
        }

        /**
         * This method is called from within the constructor to
         * initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is
         * always regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">
        private void initComponents() {

            fieldsPanel = new javax.swing.JPanel();
            jLabel1 = new javax.swing.JLabel();

            setLayout(new java.awt.BorderLayout());

            fieldsPanel.setLayout(new javax.swing.BoxLayout(fieldsPanel, javax.swing.BoxLayout.Y_AXIS));
            add(fieldsPanel, java.awt.BorderLayout.CENTER);

            jLabel1.setText("Local Player Names:");
            jLabel1.setToolTipText("");
            add(jLabel1, java.awt.BorderLayout.PAGE_START);
        }// </editor-fold>

        // Variables declaration - do not modify
        private javax.swing.JPanel fieldsPanel;
        private javax.swing.JLabel jLabel1;
        // End of variables declaration
    }
}
