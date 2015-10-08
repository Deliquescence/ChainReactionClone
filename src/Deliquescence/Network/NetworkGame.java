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

import Deliquescence.Game;
import Deliquescence.Panel.GamePanel;
import Deliquescence.Player;
import Deliquescence.Tile;

/**
 *
 * @author Deliquescence <Deliquescence1@gmail.com>
 */
public class NetworkGame extends Game {

    GameServer server;
    GameClient client;

    /**
     * A game instance over network. Most game logic is still handled underneath by {@link  Game} except how turns are handled over network.
     *
     * @param parent The {@link GamePanel} that contains this board. It will probably be {@link NetworkGamePanel} though.
     * @param NumberOfPlayers The number of players in this game.
     * @param Rows The number of rows in this game.
     * @param Columns The number of columns in this game.
     * @param players An array containing the players.
     * @param server The {@link GameServer}
     * @param client The {@link GameClient}
     */
    public NetworkGame(GamePanel parent, int NumberOfPlayers, int Rows, int Columns, Player[] players, GameServer server, GameClient client) {
        super(parent, NumberOfPlayers, Rows, Columns, players, false);
        this.server = server;
        this.client = client;
    }

    @Override
    public void clickFunction(Tile t) {
        doNetworkTurn(t.getX(), t.getY());
    }

    /**
     * Attempt to perform a network turn. Will fail if it's no one on the local clients turn or standard cases from non-network turn.
     *
     * @param x The x coordinate of the board to do the turn on.
     * @param y The y coordinate of the board to do the turn on.
     */
    public void doNetworkTurn(int x, int y) {

        if (!this.client.hasLocalPlayer(this.currentPlayer)) {
            return; //Don't take other peoples turns
        }

        Tile t = this.board.getTile(x, y);

        if (doTurn(t)) {
            NetworkPacket p = new NetworkPacket(PacketTitle.turnPacket);

            p.setData("x", t.getX());
            p.setData("y", t.getY());
            p.setData("player", this.currentPlayer);

            client.sendTCP(p);
        }
    }
}
