/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Deliquescence;

/**
 * A player in the game. Contains name, ID, if the player is alive, etc.
 *
 * @author Josh
 */
public class Player {

    private int id;
    private boolean alive;
    private String name;

    /**
     * Create a player with the specified numeric ID. Is assumed to be dead.
     *
     * @param n The numeric ID of this player.
     */
    public Player(int n) {
        this(n, "");
    }

    /**
     * Create a player with the specified numeric ID and string name.
     *
     * @param n The numeric ID of this player.
     * @param name The name of this player.
     */
    public Player(int n, String name) {
        this.id = n;
        this.name = name;
        this.alive = false;
    }

    /**
     * Gets the numeric ID of this player.
     *
     * @return The numeric ID of this player.
     */
    public int getNumber() {
        return this.id;
    }

    /**
     * Gets the configured color of this player.
     *
     * @return The color of this player.
     */
    public java.awt.Color getColor() {
        return Colorizer.getPlayerColor(this.id);
    }

    /**
     * You can use this to kill the player.
     *
     * @param life If this player is alive.
     *
     * @see isAlive()
     */
    public void setLiving(boolean life) {
        this.alive = life;
    }

    /**
     * Gets if this player is alive.
     *
     * @return If this player is alive.
     *
     * @see setLiving
     */
    public boolean isAlive() {
        return this.alive;
    }

    /**
     * Sets the friendly name of this player.
     *
     * @param name The name of this player.
     *
     * @see getName()
     * @see getDisplayName()
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the friendly name of this player. Returns "" if no name is set.
     *
     * @return The name of this player.
     *
     * @see getDisplayName()
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the display name of this player. Returns string "Player {ID}" if no name is set.
     *
     * @return The friendly display name of this player.
     *
     * @see getName()
     */
    public String getDisplayName() {
        if (this.name != "") {
            return this.name;
        } else {
            return "Player " + Integer.toString(this.id);
        }
    }
}
