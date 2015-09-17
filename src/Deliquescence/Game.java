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
package Deliquescence;

import Deliquescence.Panel.GamePanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;

/**
 * A Game of the game. It has the {@link Tile}s that make up gameplay.
 *
 * @author Josh
 */
public class Game extends JPanel {

    protected Board board;

    protected int turn;
    protected int currentPlayerID;
    protected Player currentPlayer;
    protected int numPlayers;

    /**
     * Array of the {@link Player}s in the game.
     */
    public Player[] players;

    protected boolean inGame;
    protected boolean inReaction;

    private final GamePanel gamePanel;

    /**
     * Create a new game board.
     *
     * @param parent The {@link GamePanel} that contains this board.
     * @param NumberOfPlayers The number of players in this board.
     * @param Rows The number of rows in this game.
     * @param Columns The number of columns in this game.
     * @param playerNames A String array containing friendly names of the players.
     * @param RandomizePlayerStart True to choose a random player to start.
     */
    public Game(GamePanel parent, int NumberOfPlayers, int Rows, int Columns, String[] playerNames, boolean RandomizePlayerStart) {
        this.board = new Board(this, Rows, Columns);
        numPlayers = NumberOfPlayers;
        this.gamePanel = parent;
        this.turn = 1;

        players = new Player[Config.getInt("MAX_PLAYERS") + 1];
        players[0] = new Player(0);
        for (int i = 1; i < playerNames.length; i++) {
            players[i] = new Player(i, playerNames[i]);
        }

        setDoubleBuffered(true);
        addMouseListener(new MyMouseAdapter());
        newGame(RandomizePlayerStart);
        //MyMouseAdapter will get clicks to the board
    }

    protected void newGame(boolean randomizePlayer) {
        //Create blank tiles
        board.field = new Tile[board.numCols][board.numRows];
        for (int x = 0; x < board.numCols; x++) {
            for (int y = 0; y < board.numRows; y++) {
                board.field[x][y] = new Tile(this.board, x, y);
            }
        }
        inGame = true;
        if (randomizePlayer) {
            Random random = new Random();

            setCurrentPlayerByID(random.nextInt(numPlayers) + 1);
        } else {
            setCurrentPlayerByID(1);
        }
        refreshDisplayPlayers();
        repaint();
    }

    protected boolean playerIsAlive(Player p) {
        if (this.turn <= numPlayers) {
            return true;//Players may be incorrectly labled as dead
        } else {
            return p.isAlive();
        }
    }

    protected boolean setCurrentPlayerByID(int pID) {
        if (!playerIsAlive(players[pID])) {
            return false;
        }
        this.currentPlayerID = pID;
        this.currentPlayer = players[pID];
        this.gamePanel.setPlayerStatus(currentPlayer.getColor(), currentPlayer.getDisplayName() + "'s Turn");
        return true;
    }

    protected void incrementPlayer(int curPlayerID) {
        int targetID = 0;
        if (curPlayerID == numPlayers) {
            targetID = 1;
        } else {
            targetID = curPlayerID + 1;
        }

        if (setCurrentPlayerByID(targetID)) {
            gamePanel.resetTimer();
            turn++;
        } else {
            incrementPlayer(targetID);
        }
    }

    protected void doReaction() throws StackOverflowError {
        //-Xss JVM arg will change stack size
        if (gameWon()) {//Do not run reaction if game is over
            return;
        }

        ArrayList<Tile> explodingTiles = new ArrayList<>();
        for (Tile tile : board.getAllTiles()) {//Get the tiles that need to explode
            if (tile.canExplode()) {
                explodingTiles.add(tile);
            }
        }

        ArrayList<Tile> increasedTiles = new ArrayList<>();
        for (Tile tile : explodingTiles) {//Explode the tiles and add to the neighbors
            increasedTiles.addAll(board.explodeTile(tile));
        }

        //Next iteration
        if (increasedTiles.size() > 0) {
            //Update the board, with delay so it looks nice
            paintImmediately(0, 0, board.numCols * Config.getInt("CELL_SIZE"), board.numRows * Config.getInt("CELL_SIZE"));
            sleep();

            doReaction();
        } else {//Do not iterate if nothing was changed
        }
    }

    private void sleep() {
        try {
            Thread.sleep(Config.getInt("REACTION_DELAY"));
        } catch (Exception er) {

        }
    }

    /**
     * Preform a move as the current player on a random tile.
     */
    public void RNGTurn() {
        if (!inGame || inReaction) {
            return;
        }
        while (!doTurn(board.getRandomTile())) {//Try random tiles until works

        }
    }

    public void SkipTurn() {//This might be glitchy and may need to be removed
        if (!inGame || inReaction) {
            return;
        }
        incrementPlayer(currentPlayerID);
    }

    /**
     * Attempts to add a particle to the specified {@link Tile}, but may not be successful. For example, will fail if the tile is not owned by the current player or the game is not running.
     *
     * @param onTile The tile that will be added to.
     *
     * @return True if the particle was successfully added.
     */
    public boolean doTurn(Tile onTile) {
        boolean dirty = false;
        Tile[][] fieldPreviousTemp = new Tile[board.numCols][board.numRows];//Temp because the turn may not be valid
        for (int fieldx = 0; fieldx < board.numCols; fieldx++) {
            for (int fieldy = 0; fieldy < board.numRows; fieldy++) {
                fieldPreviousTemp[fieldx][fieldy] = board.field[fieldx][fieldy].cloneTile();
            }
        }
        if (inGame) {
            if (onTile.getOwnerID() == 0) { //Unowned, can claim
                dirty = true;
                onTile.setOwner(currentPlayer);
                onTile.setNumberOfParticles(onTile.getNumberOfParticles() + 1);//Increase particle count
            } else { //Is owned
                if (onTile.getOwner() == currentPlayer) { //Make sure it is their tile
                    dirty = true;
                    onTile.setNumberOfParticles(onTile.getNumberOfParticles() + 1);//Increase particle count
                } else { //Cannot play on others tiles
                    return false;
                }
            }
        }
        if (dirty) {//Need to repaint and do reactions
            board.fieldPrevious = fieldPreviousTemp;

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
        return false;
    }

    private void refreshDisplayPlayers() {
        ArrayList<Player> displayPlayers = new ArrayList<>();
        if (turn <= numPlayers) {
            for (int i = 1; i <= numPlayers; i++) {
                players[i].setLiving(true);
            }
        }
        for (int i = 1; i <= numPlayers; i++) {
            displayPlayers.add(players[i]);
        }
        gamePanel.refreshPlayerList(displayPlayers.toArray(new Player[0]));
    }

    /**
     * Undos the last move made on the board. Can only undo once.
     */
    public void undo() {
        if (inReaction || !inGame) {
            return;
        }
        //Check if tiles are same
        boolean allSame = true;
        for (int x = 0; x < board.numCols; x++) {
            for (int y = 0; y < board.numRows; y++) {
                if (board.field[x][y].equals(board.fieldPrevious[x][y])) {
                } else {
                    allSame = false;
                    break;
                }
            }
        }

        if (allSame) {
            return;
        }

        board.field = board.fieldPrevious.clone();
        //Decrement player
        if (currentPlayerID == 1) {
            setCurrentPlayerByID(numPlayers);
        } else {
            setCurrentPlayerByID(currentPlayerID - 1);
        }
        turn--;

        repaint();
    }

    protected boolean gameWon() {
        refreshDisplayPlayers();

        //If the first turn is checked things dont work right
        if (turn <= numPlayers) {
            return false;
        }
        //Reset everyone to dead and fill in the live ones later
        for (Player player : players) {
            player.setLiving(false);
        }
        //Determine living players
        repaint();
        for (Tile tile : board.getAllTiles()) {
            if (tile.getNumberOfParticles() > 0) {
                tile.getOwner().setLiving(true);
            }
        }
        refreshDisplayPlayers();

        //check if more than one player living; if not winner is found
        int numberAlive = 0;
        for (Player p : players) {
            if (p.isAlive()) {
                numberAlive++;
            }
        }
        if (numberAlive > 1) {//no winner
            return false;
        } else {
            for (Player p : players) {
                if (p.isAlive()) {
                    inGame = false;
                    gamePanel.setPlayerStatus(currentPlayer.getColor(), p.getDisplayName() + " Wins!");
                    gamePanel.stopTimer();
                }
            }
            return true;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        //Clear everything, resolves a graphical glitch at bottom of grid
        g.setColor(gamePanel.getBackground());
        g.fillRect(0, 0, board.numCols * Config.getInt("CELL_SIZE") + 1000, board.numRows * Config.getInt("CELL_SIZE") + 1000);

        for (Tile tile : board.getAllTiles()) {
            //Draw particles (spheres)
            Image theImage;
            theImage = Config.getImageByPlayerID(tile.getOwnerID(), tile.getNumberOfParticles());

            g.drawImage(theImage, (tile.getX() * Config.getInt("CELL_SIZE")), (tile.getY() * Config.getInt("CELL_SIZE")), this);
        }
    }

    public class MyMouseAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            int clickedCol = x / Config.getInt("CELL_SIZE");
            int clickedRow = y / Config.getInt("CELL_SIZE");

            if ((clickedCol >= board.numCols) || (clickedRow >= board.numRows)) { //Out of bounds
                return;
            }

            Tile tile = board.field[clickedCol][clickedRow];
            if ((x < board.numCols * Config.getInt("CELL_SIZE")) && (y < board.numRows * Config.getInt("CELL_SIZE"))) { //in bounds
                clickFunction(tile);
            }
        }
    }

    public void clickFunction(Tile t) {
        doTurn(t);
    }
}
