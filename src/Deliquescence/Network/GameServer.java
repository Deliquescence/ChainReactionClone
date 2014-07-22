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
import Deliquescence.Tile;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import java.util.ArrayList;

/**
 *
 * @author Deliquescence <Deliquescence1@gmail.com>
 */
public class GameServer extends Server {

    public NetworkGameSettings settings;

    //public ArrayList<String> localPlayers = new ArrayList<String>();
    public ArrayList<String> allPlayers = new ArrayList<String>();

    public GameServer() {
        super();
        this.addListener(new ServerListener() {
            @Override
            public void connected(Connection c) {
                sendToTCP(c.getID(), settings);
            }

            @Override
            public void disconnected(Connection c) {

            }

            @Override
            public void received(Connection c, Object object) {

                if (object instanceof NetworkPacket) {
                    NetworkPacket np = (NetworkPacket) object;
                    switch (np.packetTitle) {
                        case attemptTurnPacket:
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

                            NetworkPacket resp = new NetworkPacket(PacketTitle.attemptTurnPacket);
                            resp.setData("valid", valid);
                            c.sendTCP(resp);

                            break;

                        case namesPacket:
                            //
                            allPlayers.addAll((ArrayList<String>) np.getData("names"));
                            break;
                    }
                }

            }
        });

    }

}
