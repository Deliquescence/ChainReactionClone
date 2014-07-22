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

import static Deliquescence.Network.PacketTitle.NetworkGameSettingsPacket;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

/**
 *
 * @author Deliquescence <Deliquescence1@gmail.com>
 */
public class Networking {

    static public void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        // kryo.register(endPoint.getClass());

        kryo.register(Deliquescence.Network.PacketTitle.class);
        kryo.register(java.util.HashMap.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(NetworkPacket.class);
        kryo.register(NetworkGameSettings.class);
        kryo.register(Deliquescence.Player.class);
        kryo.register(Deliquescence.Tile.class);
        kryo.register(Deliquescence.Network.NetworkBoard.class);

        //kryo.register(Deliquescence.Network.GameClient.class);
        //kryo.register(Deliquescence.Network.GameServer.class);
        ///????
        kryo.register(javax.swing.plaf.ColorUIResource.class);
        kryo.register(float[].class);
        kryo.register(java.net.Inet4Address.class);
    }

}

class NetworkGameSettings extends NetworkPacket {

    int totalPlayers, rows, cols;
    boolean randomStartingPlayer, RNGEnabled, turnTimerEnabled;
    int timerAction, timerLength;

    public NetworkGameSettings(String title) {
        super(NetworkGameSettingsPacket);
    }

    public NetworkGameSettings() {
        super(NetworkGameSettingsPacket);
    }
}

/*
 class networkSerializer extends Serializer<NetworkPacket> {

 @Override
 public void write(Kryo kryo, Output output, NetworkPacket object) {
 output.wri
 }

 @Override
 public NetworkPacket read(Kryo kryo, Input input, Class<NetworkPacket> type) {
 throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
 }

 }

 */
/*
 class ClientJoinGame extends NetworkPacket {

 }

 class LocalNameChange extends NetworkPacket {

 ArrayList<String> localPlayerNames;

 }

 class MovePreformed extends NetworkPacket {

 int xCoord, yCoord;
 String playerName;
 }



 class MoveRequested extends NetworkPacket {

 int xCoord, yCoord;
 String playerName;
 }

 class gameStart extends NetworkPacket {

 }
 */
