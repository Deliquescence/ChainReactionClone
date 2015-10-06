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

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import java.util.ArrayList;

/**
 *
 * @author Deliquescence <Deliquescence1@gmail.com>
 */
public class GameClient extends Client {

    public NetworkGameSettings settings;

    public ArrayList<NetworkPlayer> localPlayers = new ArrayList<>();
    public ArrayList<NetworkPlayer> allPlayers = new ArrayList<>();

    public boolean gameStarted = false;

    public WaitingRoomPanel wrp;
    public NetworkGame game;

    public GameClient() {
        super();
        this.addListener(new Listener.ThreadedListener(new Listener() {
            @Override
            public void connected(Connection c) {
                //Log.set(Log.LEVEL_TRACE);

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

                            gameStarted = true;
                            Log.debug("client", "Client started game");
                            break;

                        case requestNamesPacket:
                            Log.trace("client", "Client sending names");
                            NetworkPacket namep = new NetworkPacket(PacketTitle.namePacket);
                            namep.setData("names", localPlayers);

                            sendTCP(namep);
                            Log.trace("client", "Client sent names");
                            break;

                        case namePacket:
                            Log.trace("client", "Adding names to client");

                            ArrayList<NetworkPlayer> thePlayers = (ArrayList<NetworkPlayer>) np.getData("names");

                            for (NetworkPlayer newPlayer : thePlayers) {
                                boolean addable = true;
                                for (NetworkPlayer cPlayer : allPlayers) {
                                    if (newPlayer.uuid.compareTo(cPlayer.uuid) == 0) {
                                        addable = false;
                                        cPlayer = newPlayer;
                                        break;
                                    }
                                }
                                if (addable) {
                                    allPlayers.add(newPlayer);
                                }
                            }

                            break;

                        case turnPacket:
                            GameClient.this.game = wrp.networkGamePanel.netGame;
                            game.doTurn(
                                    game.board.getTile((int) np.getData("x"), (int) np.getData("y"))
                            );
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

    public void sendDebugPacket() {
        Log.warn("client", "Client sending debug packet");
        NetworkPacket p = new NetworkPacket(PacketTitle.debugPacket);
        //ArrayList<String> data = new ArrayList<>();
        //data.add("test");

        NetworkPlayer data = new NetworkPlayer(1);
        p.setData("data", data);
        sendTCP(p);
    }
}
