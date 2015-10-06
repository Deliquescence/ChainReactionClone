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
import com.esotericsoftware.minlog.Log;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.InetAddress;
import java.util.ArrayList;
import javax.swing.JTextField;

/**
 *
 * @author Josh
 */
public class WaitingRoomPanel extends javax.swing.JPanel {

    InetAddress serverAddress;
    GameManager gameList;
    NetworkGameViewer ngv;
    int localPlayers;
    GameClient client;
    GameServer server;
    boolean isServer;
    private Thread nameUpdateThread;

    public NetworkGamePanel networkGamePanel;

    /**
     * Creates new form WaitingRoomPanel
     */
    public WaitingRoomPanel(GameManager listPanel, GameClient client, int localPlayers, NetworkGameViewer ngv) {
        this.gameList = listPanel;
        this.localPlayers = localPlayers;
        this.client = client;
        this.ngv = ngv;
        isServer = false;
        client.wrp = this;

        initComponents();
        names n = new names();
        NamesPanel.add(n);
        //this.client.localPlayers.addAll(n.getPlayerNames());//todo add changed names
        this.client.localPlayers.add(new NetworkPlayer(0, "client"));

        NamesPanel.setPreferredSize(new Dimension(800, 20 * (localPlayers + 1)));
    }

    public WaitingRoomPanel(GameManager listPanel, GameServer server, int localPlayers, NetworkGameViewer ngv) {
        this.gameList = listPanel;
        this.localPlayers = localPlayers;
        this.server = server;
        this.ngv = ngv;
        isServer = true;

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

        initComponents();
        names n = new names();
        NamesPanel.add(n);
        this.client.localPlayers.add(new NetworkPlayer(0, "server"));
        NamesPanel.setPreferredSize(new Dimension(800, 20 * (localPlayers + 1)));

        this.nameUpdateThread = createNameUpdater();
        this.nameUpdateThread.start();
    }

    private Thread createNameUpdater() {
        final int delayMills = 1000;

        Runnable nameUpdater = new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Log.trace("NameUpdater", "Name updater tick");
                        WaitingRoomPanel.this.server.getNames();

                        Thread.sleep(delayMills);
                    }
                } catch (InterruptedException ex) {
                    Log.info(WaitingRoomPanel.class.getName(), "Name updater was interrupetd, probably on purpose though", ex);
                }
            }
        };

        Thread nameUpdaterThread = new Thread(nameUpdater, "NameUpdater");
        return nameUpdaterThread;
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
        RequestNamesButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        NamesPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        LeaveButton = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        StartButton.setText("Start");
        StartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartButtonActionPerformedWaitingRoom(evt);
            }
        });
        add(StartButton);

        RequestNamesButton.setText("names");
        RequestNamesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RequestNamesButtonActionPerformed(evt);
            }
        });
        add(RequestNamesButton);

        jButton1.setText("Edit Settings (Host Only) (TODO)");
        add(jButton1);

        jLabel1.setText("Server Address:");
        jLabel1.setText("Server Address: " + serverAddress);
        add(jLabel1);

        NamesPanel.setMaximumSize(new java.awt.Dimension(999999, 2000));
        NamesPanel.setMinimumSize(new java.awt.Dimension(200, 25));
        NamesPanel.setPreferredSize(new java.awt.Dimension(40, 20));
        NamesPanel.setLayout(new javax.swing.BoxLayout(NamesPanel, javax.swing.BoxLayout.Y_AXIS));
        add(NamesPanel);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jLabel3.setText("Players:");
        jPanel1.add(jLabel3);

        jList1.setMaximumSize(new java.awt.Dimension(0, 500));
        jScrollPane1.setViewportView(jList1);

        jPanel1.add(jScrollPane1);

        add(jPanel1);

        LeaveButton.setText("Exit");
        LeaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LeaveButtonActionPerformed(evt);
            }
        });
        add(LeaveButton);
    }// </editor-fold>//GEN-END:initComponents

    private void LeaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LeaveButtonActionPerformed
        this.gameList.removeTab(this);
    }//GEN-LAST:event_LeaveButtonActionPerformed

    private void StartButtonActionPerformedWaitingRoom(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StartButtonActionPerformedWaitingRoom
        if (isServer) {
            startGame(server);
        } else {
            Log.warn("Only server can start");
        }
    }//GEN-LAST:event_StartButtonActionPerformedWaitingRoom

    private void RequestNamesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RequestNamesButtonActionPerformed
        if (isServer) {
            server.getNames();
        } else {
            client.sendDebugPacket();
        }
    }//GEN-LAST:event_RequestNamesButtonActionPerformed

    public void startGame(GameClient client) {
        Log.trace("wrp.startgame");
        //NetworkGameViewer ngv = (NetworkGameViewer) this.getParent();

        String[] myPlayerNames = new String[client.settings.totalPlayers + 1];
        Log.trace("myPlayerNames: " + myPlayerNames);

        for (int i = 1; i <= client.allPlayers.size(); i++) {
            myPlayerNames[i] = client.allPlayers.get(i - 1).getName();
            Log.trace("myPlayerNames[" + i + "]: " + myPlayerNames[i]);
        }
        this.networkGamePanel = new NetworkGamePanel(gameList, client.settings.totalPlayers, client.settings.rows, client.settings.cols, myPlayerNames, false, false, 0, 0, server, client);
        this.ngv.displayGame(this.networkGamePanel);
    }

    private void startGame(GameServer server) {
        this.nameUpdateThread.interrupt();
        server.getNames();

        NetworkPacket p = new NetworkPacket(GameStartPacket);

        server.sendToAllTCP(p);
        NetworkGameViewer ngv = (NetworkGameViewer) this.getParent();

        String[] myPlayerNames = new String[server.settings.totalPlayers + 1];

        for (int i = 1; i <= server.allPlayers.size(); i++) {
            myPlayerNames[i] = server.allPlayers.get(i - 1).getName();
        }

        this.networkGamePanel = new NetworkGamePanel(gameList, server.settings.totalPlayers, server.settings.rows, server.settings.cols, myPlayerNames, false, false, 0, 0, server, client);
        ngv.displayGame(networkGamePanel);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton LeaveButton;
    private javax.swing.JPanel NamesPanel;
    private javax.swing.JButton RequestNamesButton;
    private javax.swing.JButton StartButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    //This doesnt comply with DRY, but extending panel.playerNames wasnt working
    class names extends javax.swing.JPanel {

        private JTextField[] playerTextFields;
        private int numPlayers;

        /**
         * Creates new form PlayerNames
         */
        public names() {
            initComponents();
            numPlayers = localPlayers;
            playerTextFields = new JTextField[numPlayers + 1];
            makeTextFields(null);

        }

        /**
         * Gets the configured name of a player from its text field.
         *
         * @param ID The id of the localPlayers name to be found
         *
         * @return The name of the player
         */
        public ArrayList<String> getPlayerNames() {
            ArrayList<String> out = new ArrayList<String>();
            for (JTextField tf : playerTextFields) {
                out.add(tf.getText());
            }
            //return playerTextFields[ID].getText();
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
