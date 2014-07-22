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
import Deliquescence.Panel.GameManager;
import Deliquescence.Refreshable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.DefaultListModel;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;

/**
 *
 * @author Josh
 */
public class BrowserPanel extends javax.swing.JPanel implements Refreshable {

    DefaultListModel listModel;
    GameManager gameManager;
    GameManager gameListPanel;

    String serverName;

    /**
     * Creates new form LANBrowserPanel
     *
     * @param gameManager //TODO
     * @param gameList The panel to send new games to
     */
    public BrowserPanel(GameManager gameManager, GameManager gameList) {
        this.gameManager = gameManager;
        this.gameListPanel = gameList;
        initComponents();

        listModel.add(0, "TODO");
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

        PlayerCountPanel = new javax.swing.JPanel();
        PlayersSliderLabel = new javax.swing.JLabel();
        PlayersSlider = new javax.swing.JSlider();
        ServerAddressPanel = new javax.swing.JPanel();
        ServerAddressFieldLabel = new javax.swing.JLabel();
        ServerAddressField = new javax.swing.JTextField();
        JoinServerButton = new javax.swing.JButton();
        ButtonPanel1 = new javax.swing.JPanel();
        RefreshButton = new javax.swing.JButton();
        JoinLANButton = new javax.swing.JButton();
        LanGamesListLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listModel = new DefaultListModel();
        GameList = GameList = new javax.swing.JList(listModel);

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        PlayerCountPanel.setMaximumSize(new java.awt.Dimension(32767, 75));
        PlayerCountPanel.setMinimumSize(new java.awt.Dimension(400, 50));
        PlayerCountPanel.setPreferredSize(new java.awt.Dimension(300, 50));
        PlayerCountPanel.setLayout(new javax.swing.BoxLayout(PlayerCountPanel, javax.swing.BoxLayout.LINE_AXIS));

        PlayersSliderLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        PlayersSliderLabel.setLabelFor(PlayersSlider);
        PlayersSliderLabel.setText("Number of players on this client: ");
        PlayersSliderLabel.setMaximumSize(new java.awt.Dimension(350, 15));
        PlayersSliderLabel.setMinimumSize(new java.awt.Dimension(150, 15));
        PlayersSliderLabel.setPreferredSize(new java.awt.Dimension(200, 15));
        PlayerCountPanel.add(PlayersSliderLabel);

        PlayersSlider.setMajorTickSpacing(1);
        PlayersSlider.setMaximum(8);
        PlayersSlider.setMinimum(1);
        PlayersSlider.setMinorTickSpacing(1);
        PlayersSlider.setPaintLabels(true);
        PlayersSlider.setPaintTicks(true);
        PlayersSlider.setSnapToTicks(true);
        PlayersSlider.setValue(1);
        PlayerCountPanel.add(PlayersSlider);

        add(PlayerCountPanel);

        ServerAddressPanel.setMaximumSize(new java.awt.Dimension(32767, 100));
        ServerAddressPanel.setMinimumSize(new java.awt.Dimension(203, 30));
        ServerAddressPanel.setPreferredSize(new java.awt.Dimension(400, 40));
        ServerAddressPanel.setLayout(new java.awt.BorderLayout());

        ServerAddressFieldLabel.setText("Server Address");
        ServerAddressFieldLabel.setMaximumSize(new java.awt.Dimension(203, 10));
        ServerAddressFieldLabel.setMinimumSize(new java.awt.Dimension(203, 10));
        ServerAddressFieldLabel.setPreferredSize(new java.awt.Dimension(203, 10));
        ServerAddressPanel.add(ServerAddressFieldLabel, java.awt.BorderLayout.PAGE_START);

        ServerAddressField.setText("localhost");
        ServerAddressField.setMaximumSize(new java.awt.Dimension(2147483647, 20));
        ServerAddressField.setPreferredSize(new java.awt.Dimension(40, 10));
        ServerAddressPanel.add(ServerAddressField, java.awt.BorderLayout.CENTER);

        JoinServerButton.setText("Join");
        JoinServerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JoinServerButtonActionPerformed(evt);
            }
        });
        ServerAddressPanel.add(JoinServerButton, java.awt.BorderLayout.LINE_END);

        add(ServerAddressPanel);

        ButtonPanel1.setMaximumSize(new java.awt.Dimension(32767, 100));
        ButtonPanel1.setPreferredSize(new java.awt.Dimension(400, 20));
        ButtonPanel1.setLayout(new java.awt.GridLayout(1, 0));

        RefreshButton.setText("Refresh");
        RefreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefreshButtonActionPerformed(evt);
            }
        });
        ButtonPanel1.add(RefreshButton);

        JoinLANButton.setText("Join Selected Game");
        JoinLANButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JoinLANButtonActionPerformed(evt);
            }
        });
        ButtonPanel1.add(JoinLANButton);

        add(ButtonPanel1);

        LanGamesListLabel.setText("LAN Games");
        LanGamesListLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        add(LanGamesListLabel);

        GameList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(GameList);

        add(jScrollPane1);
    }// </editor-fold>//GEN-END:initComponents

    private void joinGame(String server, int localPlayers) {
        try {
            InetAddress addr = InetAddress.getByName(server);

            gameListPanel.addTab(addr.getHostName(), new NetworkGameViewer(gameListPanel, addr, localPlayers), false, true);
            gameManager.switchToTabByTitle("Games");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void RefreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefreshButtonActionPerformed

        GameList.removeAll();
        String local;
        Integer[] LocalIP = new Integer[4];

        try {
            local = InetAddress.getLocalHost().getHostAddress();
            String[] IPstr = local.split("\\.");
            for (int i = 0; i < 4; i++) {
                LocalIP[i] = Integer.valueOf(IPstr[i]);
            }
        } catch (UnknownHostException ex) {
            local = "Network Error";
            return;
        }
        SubnetUtils utils = new SubnetUtils(local, "255.255.255.0");
        SubnetInfo info = utils.getInfo();

        try {
            for (String ip : info.getAllAddresses()) {
                InetAddress addr = InetAddress.getByName(ip);
                if (addr.isReachable(10)) {
                    Socket socket = new Socket(addr, Config.getInt("NETWORK_PORT"));
                }

//                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//                oos.writeObject("ping");
//
//                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//                String response = (String) ois.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_RefreshButtonActionPerformed

    private void JoinLANButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JoinLANButtonActionPerformed
        serverName = (String) GameList.getSelectedValue();
        joinGame(serverName, PlayersSlider.getValue());
    }//GEN-LAST:event_JoinLANButtonActionPerformed

    private void JoinServerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JoinServerButtonActionPerformed
        joinGame(ServerAddressField.getText(), PlayersSlider.getValue());
    }//GEN-LAST:event_JoinServerButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ButtonPanel1;
    private javax.swing.JList GameList;
    private javax.swing.JButton JoinLANButton;
    private javax.swing.JButton JoinServerButton;
    private javax.swing.JLabel LanGamesListLabel;
    private javax.swing.JPanel PlayerCountPanel;
    private javax.swing.JSlider PlayersSlider;
    private javax.swing.JLabel PlayersSliderLabel;
    private javax.swing.JButton RefreshButton;
    private javax.swing.JTextField ServerAddressField;
    private javax.swing.JLabel ServerAddressFieldLabel;
    private javax.swing.JPanel ServerAddressPanel;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void refreshConfig() {
        try {
            PlayersSlider.setMaximum(Config.getInt("MAX_PLAYERS"));
        } catch (NullPointerException e) {//Config not loaded
            PlayersSlider.setMaximum(8);
        }
    }
}
