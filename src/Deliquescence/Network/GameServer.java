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
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

/**
 *
 * @author Deliquescence <Deliquescence1@gmail.com>
 */
public class GameServer extends Server {

    public NetworkGameSettings settings;

    public ArrayList<Player> allPlayers = new ArrayList<>();

    public GameServer() {
        super();
        this.addListener(new Listener.ThreadedListener(new Listener() {
            @Override
            public void connected(Connection c) {
                Log.info("server", "Server Connect");
                Log.debug("server", "Sending server settings");
                sendToTCP(c.getID(), settings);
            }

            @Override
            public void disconnected(Connection c) {
                Log.info("server", "Server Disconnect");
            }

            @Override
            public void received(Connection c, Object object) {
                //Log.debug("Server recieved: " + object);
                try {
                    NetworkPacket np = (NetworkPacket) object;
                    Log.trace("server", "Server recieved network packet with title " + np.packetTitle);

                    switch (np.packetTitle) {
                        case namePacket:
                            ArrayList<Player> thePlayers = (ArrayList<Player>) np.getData("names");
                            for (Player newPlayer : thePlayers) {
                                boolean addable = true;
                                for (Player cPlayer : allPlayers) {
                                    if (newPlayer.equals(cPlayer)) {
                                        addable = false;
                                        //Update the player data (name)
                                        newPlayer.setNumber(cPlayer.getNumber());
                                        allPlayers.remove(cPlayer);
                                        allPlayers.add(newPlayer);
                                        break;
                                    }
                                }
                                if (addable) {
                                    newPlayer.setNumber(allPlayers.size() + 1);
                                    allPlayers.add(newPlayer);
                                }
                            }
                            Log.trace("server", "Server updated internal names");

                            updateNames();

                            break;

                        case turnPacket:
                            Log.debug("server", "Server recieved turn");
                            for (Connection con : GameServer.this.getConnections()) {
                                if (con.getID() != c.getID()) {
                                    sendToTCP(con.getID(), object);
                                }
                            }
                            break;

                        case debugPacket:
                            Log.warn("server", "Debug packet received on server");

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

    /**
     * Get the names from the clients and update the server
     * (Which will then send info back to the clients to stay synchronized)
     */
    public synchronized void getNames() {
        NetworkPacket p = new NetworkPacket(PacketTitle.requestNamesPacket);
        sendToAllTCP(p);
    }

    /**
     * Send the names the server has to the clients for them to update
     */
    public synchronized void updateNames() {
        Log.debug("server", "Server pushing names to clients");
        NetworkPacket p = new NetworkPacket(PacketTitle.namePacket);
        p.setData("names", allPlayers);
        sendToAllTCP(p);
    }

    public Collection<Player> getAllPlayers() {
        return new TreeSet<>(allPlayers);
    }
}
