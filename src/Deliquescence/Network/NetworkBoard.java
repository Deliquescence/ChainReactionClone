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

import Deliquescence.Board;
import Deliquescence.Panel.GamePanel;
import Deliquescence.Player;
import Deliquescence.Tile;

/**
 *
 * @author Deliquescence <Deliquescence1@gmail.com>
 */
public class NetworkBoard extends Board {

    GameServer server;
    GameClient client;

    public NetworkBoard(GamePanel parent, int NumberOfPlayers, int Rows, int Columns, String[] playerNames, boolean RandomizePlayerStart, GameServer server, GameClient client) {
        super(parent, NumberOfPlayers, Rows, Columns, playerNames, RandomizePlayerStart);
        this.server = server;
        this.client = client;

    }

    public synchronized boolean tryTurn(Tile t) {
        System.out.println("tryTurn!!");

        if (!inGame) {
            return false;
        }
        NetworkPacket p = new NetworkPacket(PacketTitle.attemptTurnPacket);
        p.setData("onTile", t);
        p.setData("player", this.currentPlayer);
        /*
         client.addListener(new ClientListener(){
         @Override
         public void received(Connection c, Object object) {
         this.notify();
         }
         });
         */
        //client.sendTCP(p);

        ResponseWaiter responseWaiter = new ResponseWaiter(client, PacketTitle.attemptTurnPacket);

        NetworkPacket resp = responseWaiter.sendAndGetResponse(p);
        //NetworkPacket resp = (NetworkPacket) client.waitForResponse(PacketTitle.attemptTurnPacket);

        if ((boolean) resp.getData("valid") == true) {
            doTurn(t, currentPlayer);
            return true;
        }
        return false;
    }

    public boolean doTurn(Tile onTile, Player player) {
        //boolean dirty = false;
        Tile[][] fieldPreviousTemp = new Tile[numCols][numRows];//may not need to be temp
        for (int fieldx = 0; fieldx < numCols; fieldx++) {
            for (int fieldy = 0; fieldy < numRows; fieldy++) {
                fieldPreviousTemp[fieldx][fieldy] = field[fieldx][fieldy].cloneTile();
            }
        }

        onTile.setNumberOfParticles(onTile.getNumberOfParticles() + 1);//Increase particle count

        fieldPrevious = fieldPreviousTemp;

        inReaction = true;
        try {
            doReaction();
        } catch (StackOverflowError er) {

        }
        inReaction = false;

        if (!gameWon()) {
            incrementPlayer(currentPlayerID);//Only increment if not won
        }

        repaint();
        return true;
    }

    @Override
    public void clickFunction(Tile t) {
        tryTurn(t);
    }

}
