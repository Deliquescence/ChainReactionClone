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

import Deliquescence.Player;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;

/**
 *
 * @author Deliquescence <Deliquescence1@gmail.com>
 */
public class GameClient extends Client {

    public NetworkGameSettings settings;

    /**
     * The local players on this clients machine
     */
    public Player[] localPlayers;

    /**
     * All the players in the game, no zeroth player
     */
    public ArrayList<Player> allPlayers = new ArrayList<>();

    public boolean gameStarted = false;

    public WaitingRoomPanel wrp;
    public NetworkGame game;

    public InetAddress serverAddress;

    public GameClient() {
        super();

        this.addListener(new Listener.ThreadedListener(new Listener() {
            @Override
            public void connected(Connection c) {
                //Log.set(Log.LEVEL_TRACE);

                try {
                    serverAddress = InetAddress.getByName(GameClient.this.getRemoteAddressTCP().getHostString());
                } catch (UnknownHostException ex) {
                }

                Log.info("client", "Client Connect");
            }

            @Override
            public void disconnected(Connection c) {
                Log.info("client", "Client Disconnect");
            }

            @Override
            public void received(Connection c, Object object) {
                //Log.info("Client Recieve: " + object);
                try {

                    NetworkPacket np = (NetworkPacket) object;
                    Log.trace("client", "Client recieved network packet with title " + np.packetTitle);

                    switch (np.packetTitle) {
                        case NetworkGameSettingsPacket:
                            Log.debug("client", "Setting client settings");
                            settings = (NetworkGameSettings) np;
                            break;

                        case GameStartPacket:

                            Log.debug("client", "Client starting game");
                            wrp.startGame(GameClient.this);
                            GameClient.this.game = wrp.networkGamePanel.netGame;

                            //check if the players are randomized
                            boolean randomStartingPlayer = (boolean) np.getData("randomStart");
                            if (randomStartingPlayer) {
                                Player startPlayer = (Player) np.getData("startPlayer");
                                GameClient.this.game.setCurrentPlayer(startPlayer);//Set the start player to was the server randomly picked
                            }

                            gameStarted = true;
                            Log.debug("client", "Client started game");
                            break;

                        case requestNamesPacket:
                            GameClient.this.wrp.updateLocalNames();//Refresh localPlayers with current values

                            NetworkPacket namep = new NetworkPacket(PacketTitle.namePacket);
                            namep.setData("names", new ArrayList<>(Arrays.asList(localPlayers)));

                            sendTCP(namep);
                            Log.trace("client", "Client sent names to server");
                            break;

                        case namePacket:
                            ArrayList<Player> thePlayers = (ArrayList<Player>) np.getData("names");

                            updateNames(thePlayers);

                            break;

                        case turnPacket:
                            game.doTurn(
                                    game.board.getTile((int) np.getData("x"), (int) np.getData("y"))
                            );
                            break;

                        case readyStatusPacket:
                            int remaining = (int) np.getData("numWaitingFor");
                            int seconds = (int) np.getData("seconds");
                            GameClient.this.wrp.setReadyInfo(remaining, seconds);

                            break;

                        case debugPacket:

                            Log.warn("client", "Debug packet received on client");
                            break;
                    }

                } catch (ClassCastException ignore) {
                }
            }
        }));
    }

    public synchronized void updateNames(Collection<Player> players) {
        for (Player newPlayer : players) {
            boolean addable = true;
            for (Player cPlayer : allPlayers) {
                if (newPlayer.equals(cPlayer)) {
                    addable = false;
                    //Update the player data (name)
                    allPlayers.remove(cPlayer);
                    allPlayers.add(newPlayer);
                    break;
                }
            }
            if (addable) {
                allPlayers.add(newPlayer);
            }
        }
        Log.trace("client", "Client updated names");
    }

    /**
     * Check if a player is one of the local players on this client.
     *
     * @param testPlayer The player to check
     *
     * @return if true if the player is local
     */
    public boolean hasLocalPlayer(Player testPlayer) {
        boolean hasPlayer = false;
        for (Player p : localPlayers) {
            if (p.equals(testPlayer)) {
                hasPlayer = true;
                break;
            }
        }
        return hasPlayer;
    }

    public void sendDebugPacket() {
        Log.warn("client", "Client sending debug packet");
        NetworkPacket p = new NetworkPacket(PacketTitle.debugPacket);
        ArrayList<String> data = new ArrayList<>();
        data.add("test");

        p.setData("data", data);
        sendTCP(p);
    }

    public Collection<Player> getAllPlayers() {
        return new TreeSet<>(allPlayers);
    }
}
