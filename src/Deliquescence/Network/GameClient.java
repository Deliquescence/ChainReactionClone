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
    /*
     private Object response;
     private boolean waiting;
     private PacketTitle waitingFor;


     public synchronized Object waitForResponse(PacketTitle packet, Thread with) {
     waitingFor = packet;
     waiting = true;
     try {
     this.wait();
     } catch (Exception e) {
     }
     return response;
     }*/

    public GameClient() {
        super();
        this.addListener(new Listener.ThreadedListener(new Listener() {
            @Override
            public void connected(Connection c) {
                Log.set(Log.LEVEL_TRACE);

                Log.info("Client Connect");
            }

            @Override
            public void disconnected(Connection c) {
                Log.info("Client Disconnect");
            }

            @Override
            public void received(Connection c, Object object) {
                Log.info("Client Recieve: " + object);
                try {
                    NetworkPacket np = (NetworkPacket) object;
                    /*if (waiting && (np.packetTitle == waitingFor)) {
                     this.notify();
                     waiting = false;
                     response = object;
                     } else {*/
                    switch (np.packetTitle) {
                        case NetworkGameSettingsPacket:
                            Log.debug("Setting client settings");
                            settings = (NetworkGameSettings) np;
                            break;

                        case GameStartPacket:
                            Log.debug("Client sending names");
                            NetworkPacket p = new NetworkPacket(PacketTitle.namesPacket);
                            p.setData("names", localPlayers);
                            sendTCP(p);

                            Log.debug("Client starting game");
                            wrp.startGame(GameClient.this);
                            gameStarted = true;
                            Log.debug("Client started game");
                            break;

                        case namesPacket:
                            Log.debug("Adding names to client");

                            GameClient.this.allPlayers.addAll((ArrayList<NetworkPlayer>) np.getData("players"));
                            break;

                        case turnPacket:
                            GameClient.this.wrp.networkGamePanel.netGameBoard.doTurn(
                                    (Deliquescence.Tile) np.getData("onTile"),
                                    (Deliquescence.Network.NetworkPlayer) np.getData("player")
                            );
                            break;
                    }

                } catch (ClassCastException ignore) {
                }
            }
        }));
    }
}
