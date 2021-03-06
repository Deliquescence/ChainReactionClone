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
package deliquescence;

import deliquescence.panel.GamePanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javax.swing.JPanel;

/**
 * An instance of the game, handles all the game logic.
 */
public class Game extends JPanel {

	public Board board;

	protected int turn;
	protected int currentPlayerIndex;
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
	 * Create a new game.
	 *
	 * @param parent The {@link GamePanel} that contains this game.
	 * @param NumberOfPlayers The number of players in this game.
	 * @param Rows The number of rows in this game.
	 * @param Columns The number of columns in this game.
	 * @param players An array containing players, of which the first {@code NumberOfPlayers} will be used.
	 * @param RandomizePlayers True to choose a random player to start.
	 */
	public Game(GamePanel parent, int NumberOfPlayers, int Rows, int Columns, Player[] players, boolean RandomizePlayers) {

		this.board = new Board(this, Rows, Columns);
		this.numPlayers = NumberOfPlayers;
		this.gamePanel = parent;
		this.turn = 1;
		this.currentPlayerIndex = 0;

		this.players = Arrays.copyOf(players, NumberOfPlayers);

		setDoubleBuffered(true);
		addMouseListener(new MyMouseAdapter());
		newGame(RandomizePlayers);
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

			ArrayList<Player> shuffle = new ArrayList<>(Arrays.asList(players));

			java.util.Collections.shuffle(shuffle);

			shuffle.toArray(players);
		}
		setCurrentPlayerByIndex(0); //Since the entire order was shuffled, it doesn't really matter who starts

		refreshPlayersDisplay();
		repaint();
	}

	protected boolean playerIsAlive(Player p) {
		if (this.turn <= numPlayers) {
			return true; //Players may be incorrectly labled as dead
		} else {
			return p.isAlive();
		}
	}

	private boolean setCurrentPlayerByIndex(int index) {
		if (!playerIsAlive(players[index])) {
			return false;
		}
		this.currentPlayerIndex = index;
		this.currentPlayer = players[this.currentPlayerIndex];
		this.gamePanel.setPlayerStatus(currentPlayer.getColor(), currentPlayer.getDisplayName() + "'s Turn");

		return true;
	}

	public boolean setCurrentPlayer(Player thePlayer) {
		for (int i = 0; i < this.players.length; i++) {
			if (this.players[i].equals(thePlayer)) {
				return setCurrentPlayerByIndex(i);
			}
		}

		return false;
	}

	protected void incrementPlayer() {
		int newIndex = this.currentPlayerIndex + 1;
		if (newIndex >= numPlayers) {
			newIndex = 0;
		}

		if (setCurrentPlayerByIndex(newIndex)) {
			gamePanel.resetTimer();
			turn++;
		} else {
			//The next player is dead
			this.currentPlayerIndex = newIndex;
			incrementPlayer();
		}
	}

	protected void doReaction() throws StackOverflowError {
		//-Xss JVM arg will change stack size
		if (gameWon()) { //Do not run reaction if game is over
			return;
		}

		ArrayList<Tile> explodingTiles = new ArrayList<>();
		for (Tile tile : board.getAllTiles()) { //Get the tiles that need to explode
			if (tile.canExplode()) {
				explodingTiles.add(tile);
			}
		}

		ArrayList<Tile> increasedTiles = new ArrayList<>();
		for (Tile tile : explodingTiles) { //Explode the tiles and add to the neighbors
			increasedTiles.addAll(board.explodeTile(tile));
		}

		//Next iteration
		if (increasedTiles.size() > 0) { //Do not iterate if nothing was changed
			//Update the board, with delay so it looks nice
			paintImmediately(0, 0, board.numCols * Config.getInt("CELL_SIZE"), board.numRows * Config.getInt("CELL_SIZE"));
			refreshPlayersDisplay();
			sleep();

			doReaction();
		}
	}

	private void sleep() {
		try {
			Thread.sleep(Config.getInt("REACTION_DELAY"));
		} catch (Exception er) {
		}
	}

	/**
	 * Perform a move as the current player on a random tile.
	 */
	public void RNGTurn() {
		if (!inGame || inReaction) {
			return;
		}
		while (!doTurn(board.getRandomTile())) { //Try random tiles until works
		}
	}

	public void SkipTurn() { //This might be glitchy and may need to be removed
		if (!inGame || inReaction) {
			return;
		}
		incrementPlayer();
	}

	/**
	 * Checks if a move is valid for the specified tile and player.
	 * Will fail if the tile is not owned by the current player or the game is not running.
	 *
	 * @param onTile The tile to check
	 * @param onPlayer The player to check
	 *
	 * @return if the move is valid
	 */
	public boolean moveIsValid(Tile onTile, Player onPlayer) {
		if (inGame) {
			if (onTile.getOwner() == null) { //Unowned, can claim
				return true;
			} else { //Is owned
				return onTile.getOwner() == onPlayer; //Make sure it is their tile
				//Cannot play on others tiles
			}
		} else {
			return false;
		}
	}

	/**
	 * Attempts to add a particle to the specified {@link Tile}, but may not be successful if move is invalid.
	 *
	 * @param onTile The tile that will be added to.
	 *
	 * @return True if the particle was successfully added.
	 */
	public boolean doTurn(Tile onTile) {

		if (moveIsValid(onTile, currentPlayer)) {

			//Generate undo information
			Tile[][] fieldPreviousTemp = new Tile[board.numCols][board.numRows];
			for (int fieldx = 0; fieldx < board.numCols; fieldx++) {
				for (int fieldy = 0; fieldy < board.numRows; fieldy++) {
					fieldPreviousTemp[fieldx][fieldy] = board.field[fieldx][fieldy].clone();
				}
			}
			board.fieldPrevious = fieldPreviousTemp;

			//Increase particle count and update owner
			onTile.setOwner(currentPlayer);
			onTile.setNumberOfParticles(onTile.getNumberOfParticles() + 1);

			//Do the reaction
			inReaction = true;
			try {
				doReaction();
			} catch (StackOverflowError er) {
			}
			inReaction = false;

			if (!gameWon()) {
				incrementPlayer(); //Only increment if not won
			}

			repaint();

			return true;
		}

		return false; //Move wasn't valid
	}

	/*
	 * Update the list of players on the side to show who is alive
	 */
	private void refreshPlayersDisplay() {
		updateLivingPlayers();
		gamePanel.refreshPlayerList(this.players);
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
		if (this.currentPlayerIndex == 0) {
			setCurrentPlayerByIndex(numPlayers - 1);
		} else {
			setCurrentPlayerByIndex(this.currentPlayerIndex - 1);
		}
		turn--;

		repaint();
	}

	/*
	 * For each player in this game, determine if they are actually alive and
	 * setLiving() accordingly.
	 */
	protected void updateLivingPlayers() {
		//If not everyone has taken their first turn, everyone is alive
		//Otherwise, set everyone to dead and check tiles for who is actually alive
		for (Player player : players) {
			player.setLiving(turn <= numPlayers);
		}
		//Determine living players
		for (Tile tile : board.getAllTiles()) {
			if (tile.getNumberOfParticles() > 0) {
				tile.getOwner().setLiving(true);
			}
		}
	}

	protected boolean gameWon() {

		refreshPlayersDisplay();

		//If the first turn is checked things dont work right
		if (turn <= numPlayers) {
			return false;
		}

		//check if more than one player living; if not winner is found
		int numberAlive = 0;
		for (Player p : players) {
			if (p.isAlive()) {
				numberAlive++;
			}
		}
		if (numberAlive > 1) { //no winner
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

			int playerNum = 0;
			if (tile.getOwner() != null) {
				playerNum = tile.getOwner().getNumber();
			}

			theImage = Config.getImageByPlayerID(playerNum, tile.getNumberOfParticles());

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
			if ((x < board.numCols * Config.getInt("CELL_SIZE")) &&
					(y < board.numRows * Config.getInt("CELL_SIZE"))) { //in bounds
				clickFunction(tile);
			}
		}
	}

	public void clickFunction(Tile t) {
		doTurn(t);
	}
}
