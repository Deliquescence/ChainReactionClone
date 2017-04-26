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

/**
 * A tile of the board. Contains owner, number of particles, etc.
 *
 * @author Josh
 */
public class Tile implements Cloneable {

	private final int xPos, yPos;
	private int particleCapacity; //Particles needed to explode

	private Player owner;
	private int numParticles;

	/**
	 * Create a blank tile with no owner or particles.
	 *
	 * @param b The parent {@link Game} of this tile.
	 * @param x The X-Coordinate of this tile.
	 * @param y The Y-Coordinate of this tile.
	 */
	public Tile(Board b, int x, int y) {
		this(b, x, y, null, 0);
	}

	/**
	 * Create a tile with the specified owner.
	 *
	 * @param b The parent {@link Game} of this tile.
	 * @param x The X-Coordinate of this tile.
	 * @param y The Y-Coordinate of this tile.
	 * @param owner The {@link Player} who owns this tile.
	 */
	public Tile(Board b, int x, int y, Player owner) {
		this(b, x, y, owner, 0);
	}

	/**
	 * Create a tile with the specified owner and number of particles.
	 *
	 * @param b The parent {@link Game} of this tile.
	 * @param x The X-Coordinate of this tile.
	 * @param y The Y-Coordinate of this tile.
	 * @param owner The {@link Player} who owns this tile.
	 * @param numParticles How many particles this tile has.
	 */
	public Tile(Board b, int x, int y, Player owner, int numParticles) {
		this(x, y, owner, numParticles, setupCapacity(b, x, y));
	}

	/**
	 * Create a tile with the specified owner, number of particles, and capacity (rather than board)
	 *
	 * @param x The X-Coordinate of this tile.
	 * @param y The Y-Coordinate of this tile.
	 * @param owner The {@link Player} who owns this tile.
	 * @param numParticles How many particles this tile has.
	 * @param capacity The number of particles needed to explode.
	 */
	public Tile(int x, int y, Player owner, int numParticles, int capacity) {
		this.xPos = x;
		this.yPos = y;
		this.numParticles = numParticles;
		this.owner = owner;

		this.particleCapacity = capacity;
	}

	private static int setupCapacity(Board b, int x, int y) {
		if (x == 0 || x == b.getCols() - 1) { //On left or right edge
			if (y == 0 || y == b.getRows() - 1) { //In a corner
				return 2;
			} else {//On left or right edge, not in corner
				return 3;
			}
		} else if (y == 0 || y == b.getRows() - 1) {//On top or bottom edge
			//If it was a corner it should have been found
			return 3;
		} else {
			return 4;
		}
	}

	/**
	 * Gets the X coordinate of this tile.
	 *
	 * @return The X coordinate of this tile.
	 */
	public int getX() {
		return this.xPos;
	}

	/**
	 * Gets the Y coordinate of this tile.
	 *
	 * @return The y coordinate of this tile.
	 */
	public int getY() {
		return this.yPos;
	}

	/**
	 * Gets the owner of this tile.
	 *
	 * @return The owner of this tile, or null if it is not owned.
	 */
	public Player getOwner() {
		return this.owner;
	}

	/**
	 * Gets the number of particles in this tile.
	 *
	 * @return The number of particles in this tile.
	 */
	public int getNumberOfParticles() {
		return this.numParticles;
	}

	/**
	 * Sets the owner of this tile.
	 *
	 * @param Owner The owner of this tile.
	 */
	public void setOwner(Player Owner) {
		this.owner = Owner;
	}

	/**
	 * Sets the number of particles in this tile.
	 *
	 * @param n The number of particles in this tile.
	 */
	public void setNumberOfParticles(int n) {

		this.numParticles = n;
	}

	/**
	 * Determines if this particle will explode with the number of particles it currently has.
	 *
	 * @return True if the tile will explode.
	 */
	public boolean canExplode() {
		return this.numParticles >= this.particleCapacity;
	}

	/**
	 * Gets a new tile object with the same properties as this one.
	 *
	 * @return A new {link Tile} object with the same data.
	 */
	@Override
	public Tile clone() {
		return new Tile(this.getX(), this.getY(), this.getOwner(), this.getNumberOfParticles(), this.particleCapacity);
	}

	/**
	 *
	 * Check if one tile is equal to (has the same data as) another.
	 *
	 * @param compare The Tile to compare to.
	 *
	 * @return True if the Tiles are identical.
	 */
	public boolean equals(Tile compare) {
		return this.getNumberOfParticles() == compare.getNumberOfParticles() &&
				this.getOwner() == compare.getOwner() &&
				this.getX() == compare.getX() &&
				this.getY() == compare.getY();
	}
}
