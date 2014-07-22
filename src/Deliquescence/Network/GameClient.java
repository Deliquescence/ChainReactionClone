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
import java.util.ArrayList;

/**
 *
 * @author Deliquescence <Deliquescence1@gmail.com>
 */
public class GameClient extends Client {

    public NetworkGameSettings settings;

    public ArrayList<String> localPlayers = new ArrayList<>();
    public ArrayList<String> allPlayers = new ArrayList<>();

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
        this.addListener(new ClientListener() {
            @Override
            public void connected(Connection c) {

            }

            @Override
            public void disconnected(Connection c) {

            }

            @Override
            public void received(Connection c, Object object) {

                try {
                    NetworkPacket np = (NetworkPacket) object;
                    /*if (waiting && (np.packetTitle == waitingFor)) {
                     this.notify();
                     waiting = false;
                     response = object;
                     } else {*/
                    switch (np.packetTitle) {
                        case NetworkGameSettingsPacket:
                            settings = (NetworkGameSettings) np;
                            break;

                        case GameStartPacket:
                            NetworkPacket p = new NetworkPacket(PacketTitle.namesPacket);
                            p.setData("names", localPlayers);
                            sendTCP(p);

                            wrp.startGame(GameClient.this);
                            gameStarted = true;
                            break;
                    }

                } catch (ClassCastException ignore) {
                }
            }
        });
    }
}
