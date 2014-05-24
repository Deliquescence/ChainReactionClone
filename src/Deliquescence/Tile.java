/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Deliquescence;

/**
 * A tile of the board. Contains owner, number of particles, etc.
 *
 * @author Josh
 */
public class Tile {

    private final int xPos, yPos;
    private final int particleCapacity; //Particles needed to explode
    private final Board board;

    private Player owner;
    private int numParticles;

    /**
     * Create a blank tile with no owner or particles.
     *
     * @param b The parent {@link Board} of this tile.
     * @param x The X-Coordinate of this tile.
     * @param y The Y-Coordinate of this tile.
     */
    public Tile(Board b, int x, int y) {
        this(b, x, y, null, 0);
    }

    /**
     * Create a tile with the specified owner.
     *
     * @param b The parent {@link Board} of this tile.
     * @param x The X-Coordinate of this tile.
     * @param y The Y-Coordinate of this tile.
     * @param Owner The {@link Player} who owns this tile.
     */
    public Tile(Board b, int x, int y, Player Owner) {
        this(b, x, y, Owner, 0);
    }

    /**
     * Create a tile with the specified owner and number of particles.
     *
     * @param b The parent {@link Board} of this tile.
     * @param x The X-Coordinate of this tile.
     * @param y The Y-Coordinate of this tile.
     * @param Owner The {@link Player} who owns this tile.
     * @param Particles How many particles this tile has.
     */
    public Tile(Board b, int x, int y, Player Owner, int Particles) {
        this.board = b;
        this.xPos = x;
        this.yPos = y;
        this.numParticles = Particles;

        if (Owner != null) {
            this.owner = Owner;
        } else {
            this.owner = new Player(0);
        }

        if (x == 0 || x == b.getCols() - 1) { //On left or right edge
            if (y == 0 || y == b.getRows() - 1) { //In a corner
                this.particleCapacity = 2;
            } else {//On left or right edge, not in corner
                this.particleCapacity = 3;
            }
        } else if (y == 0 || y == b.getRows() - 1) {//On top or bottom edge
            //If it was a corner it should have been found
            this.particleCapacity = 3;
        } else {
            this.particleCapacity = 4;
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
     * @return The owner of this tile.
     */
    public Player getOwner() {
        return this.owner;
    }

    /**
     * Gets the numeric owner of this tile.
     *
     * @return The owner of this tile.
     */
    public int getOwnerID() {
        return this.owner.getNumber();
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
     * Gets the {@link Board} this tile is in.
     *
     * @return The parent {@link Board} of this tile.
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Gets a new tile object with the same properties as this one.
     *
     * @return A new {link Tile} object with the same data.
     */
    public Tile cloneTile() {
        return new Tile(this.getBoard(), this.getX(), this.getY(), this.getOwner(), this.getNumberOfParticles());
    }

    public boolean equals(Tile compare) {
        if (this.getBoard() == compare.getBoard() &&
                this.getNumberOfParticles() == compare.getNumberOfParticles() &&
                this.getOwner() == compare.getOwner() &&
                this.getX() == compare.getX() &&
                this.getY() == compare.getY()) {
            return true;
        } else {
            return false;
        }
    }
}
