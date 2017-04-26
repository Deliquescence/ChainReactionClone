/*
 * Copyright (c) 2015, Deliquescence <Deliquescence1@gmail.com>
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

import java.util.ArrayList;
import java.util.Random;

/**
 * A Board of a game, contains all the {@link Tile}s
 */
public class Board {

	protected final Game parentGame;

	protected final int numRows;
	protected final int numCols;

	protected Tile[][] field;
	protected Tile[][] fieldPrevious;

	public Board(Game parent, int rows, int cols) {
		this.parentGame = parent;
		this.numRows = rows;
		this.numCols = cols;
	}

	protected Tile[] getSurroundingTiles(Tile t) {
		ArrayList<Tile> tiles = new ArrayList<>();
		//All these trys are somewhat sketchy
		//But putting all the .add's in one would sometimes stop the lower ones from being called
		try {
			tiles.add(field[t.getX() - 1][t.getY()]);
		} catch (ArrayIndexOutOfBoundsException ex) {
			//Ignore
		}

		try {
			tiles.add(field[t.getX() + 1][t.getY()]);
		} catch (ArrayIndexOutOfBoundsException ex) {
			//Ignore
		}

		try {
			tiles.add(field[t.getX()][t.getY() - 1]);
		} catch (ArrayIndexOutOfBoundsException ex) {
			//Ignore
		}

		try {
			tiles.add(field[t.getX()][t.getY() + 1]);
		} catch (ArrayIndexOutOfBoundsException ex) {
			//Ignore
		}

		return tiles.toArray(new Tile[0]);
	}

	protected Tile[] getAllTiles() {
		return getAllTiles(field);
	}

	protected Tile[] getAllTiles(Tile[][] theField) {
		ArrayList<Tile> tiles = new ArrayList<>();
		for (int x = 0; x < numCols; x++) {
			for (int y = 0; y < numRows; y++) {
				tiles.add(theField[x][y]);
			}
		}

		return tiles.toArray(new Tile[0]);
	}

	protected ArrayList<Tile> explodeTile(Tile tile) { //Retruns altered tiles
		ArrayList<Tile> dirtyTiles = new ArrayList<>(); //Does not include initiating tile
		for (Tile t : getSurroundingTiles(tile)) {
			t.setOwner(tile.getOwner());
			t.setNumberOfParticles(t.getNumberOfParticles() + 1);
			dirtyTiles.add(t);
		}
		if (tile.getNumberOfParticles() == 5) {
			tile.setNumberOfParticles(1); //If two particles explode into a tile of three, it should be left with one after it explodes four.
		} else {
			tile.setNumberOfParticles(0);
			tile.setOwner(null);
		}

		return dirtyTiles;
	}

	/**
	 * Gets the number of columns in this board.
	 *
	 * @return The number of columns.
	 */
	public int getCols() {
		return this.numCols;
	}

	/**
	 * Gets the number of rows in this board.
	 *
	 * @return The rows of columns.
	 */
	public int getRows() {
		return this.numRows;
	}

	/**
	 * Used for RNG turns.
	 *
	 * @return The random tile that was selected.
	 */
	public Tile getRandomTile() {
		Random random = new Random();
		int x = random.nextInt(numCols);
		int y = random.nextInt(numRows);

		return field[x][y];
	}

	/**
	 *
	 * Get a tile from given coordinates
	 *
	 * @param x The x coordinate
	 * @param y The y coordinate
	 *
	 * @return The tile on the coordinates
	 */
	public Tile getTile(int x, int y) {
		return field[x][y];
	}
}
