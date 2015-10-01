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

/**
 * A player in the game. Contains name, ID, if the player is alive, etc.
 *
 * @author Josh
 */
public class Player {

    protected int id;
    protected boolean alive;
    protected String name;

    /**
     * This is needed for networking...
     * kryonet needs a no-arg constructor
     *
     * Dont actually use this?
     * ...shady af
     */
    public Player() {
    }

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
