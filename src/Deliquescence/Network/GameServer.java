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

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import java.util.ArrayList;

/**
 *
 * @author Deliquescence <Deliquescence1@gmail.com>
 */
public class GameServer extends Server {

    public NetworkGameSettings settings;

    //public ArrayList<String> localPlayers = new ArrayList<String>();
    public ArrayList<NetworkPlayer> allPlayers = new ArrayList<>();

    public GameServer() {
        super();
        this.addListener(new Listener.ThreadedListener(new Listener() {
            @Override
            public void connected(Connection c) {

                //Log.set(Log.LEVEL_TRACE);
                Log.info("Server Connect");
                Log.debug("Sending server settings");
                sendToTCP(c.getID(), settings);
            }

            @Override
            public void disconnected(Connection c) {
                Log.info("Server Disconnect");
            }

            @Override
            public void received(Connection c, Object object) {
                //Log.debug("Server recieved: " + object);
                try {
                    NetworkPacket np = (NetworkPacket) object;
                    Log.debug("Server recieved network packet with title " + np.packetTitle);

                    switch (np.packetTitle) {
                        /*
                         case turnPacket:
                         System.out.println("attempt turn on server");
                         boolean valid = false;
                         Tile onTile = (Tile) np.getData("onTile");
                         if (onTile.getOwnerID() == 0) { //Unowned, can claim
                         valid = true;
                         } else { //Is owned
                         if (onTile.getOwner() == (Player) np.getData("player")) { //Make sure it is their tile
                         valid = true;

                         } else { //Cannot play on others tiles
                         valid = false;
                         }
                         }

                         NetworkPacket resp = new NetworkPacket(PacketTitle.turnPacket);
                         resp.setData("valid", valid);
                         c.sendTCP(resp);

                         break;*/
                        case namePacket:
                            Log.debug("Adding names to server");

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
                                    newPlayer.setNumber(allPlayers.size() + 1);
                                    allPlayers.add(newPlayer);

                                }
                            }

                            Log.debug("sending names to client");

                            NetworkPacket p = new NetworkPacket(PacketTitle.namePacket);
                            p.setData("names", allPlayers);
                            //p.setData("names", (ArrayList<NetworkPlayer>) np.getData("names"));
                            c.sendTCP(p);

                            break;

                        case turnPacket:
                            Log.debug("Server recieved turn");
                            for (Connection con : GameServer.this.getConnections()) {
                                if (con.getID() != c.getID()) {
                                    sendToTCP(con.getID(), object);
                                }
                            }
                            break;

                        case debugPacket:
                            //TODO WHEN LEFT OFF, DISCONNECT IS CAUSED BY SENDING NetworkPlayer
                            //TODO Look into custom seralizer or download

                            Log.warn("Debug packet received on server");
                            NetworkPlayer data = (NetworkPlayer) np.getData("data");
                            //for (String d : data) {
                            Log.debug(data.getDisplayName());
                            //}

                            NetworkPacket p1 = new NetworkPacket(PacketTitle.debugPacket);
                            // sendTCP(p);
                            GameServer.this.sendToAllTCP(p1);

                            break;
                    }
                } catch (ClassCastException ignore) {
                }

            }
        }));

    }

    public void getNames() {
        NetworkPacket p = new NetworkPacket(PacketTitle.requestNamesPacket);
        sendToAllTCP(p);
    }

}
