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

import Deliquescence.Panel.GameManager;
import Deliquescence.Panel.GamePanel;
import Deliquescence.Player;

/**
 *
 * @author Deliquescence <Deliquescence1@gmail.com>
 */
public class NetworkGamePanel extends GamePanel {

    public final GameServer server;
    public final GameClient client;

    public NetworkGame netGame;

    public NetworkGamePanel(GameManager gameManager, int numPlayers, int rows, int columns, Player[] players, boolean RNGEnabled, boolean RandomizePlayer, int timerLength, int timeAction, GameServer server, GameClient client) {
        super(gameManager, numPlayers, rows, columns, players, RNGEnabled, RandomizePlayer, timerLength, timeAction);
        this.server = server;
        this.client = client;

        this.netGame.server = server;
        this.netGame.client = client;
    }

    @Override
    protected void makeGame(int numPlayers, int rows, int columns, Player[] players, boolean RandomizePlayer) {
        game = new NetworkGame(this, numPlayers, rows, columns, players, RandomizePlayer, server, client);
        netGame = (NetworkGame) game;
    }
}
