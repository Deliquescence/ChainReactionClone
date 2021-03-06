/*
 * Copyright (c) 2016, Josh Baird
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

import java.util.Collection;

/**
 * Random utilities
 */
public class Utils {

	/**
	 * Given a collection of Players, make an array of their display names
	 *
	 * @param players the collection to convert
	 * @return a String array of display names
	 */
	public static String[] playersToStrings(Collection<Player> players) {
		String[] names = new String[players.size()];
		int i = 0;
		for (Player p : players) {
			names[i++] = p.getDisplayName();
		}

		return names;
	}

	/**
	 * Given an array of Strings, make an array of players with those names.
	 * Note the array is 1-indexed.
	 *
	 * @param names array of names
	 * @return an array of players
	 */
	public static Player[] namesToPlayers(String[] names) {
		Player[] players = new Player[names.length];
		for (int i = 0; i < players.length; i++) {
			players[i] = new Player(i + 1, names[i]);
			//The player id is +1 because while there is no longer a zeroth player,
			//Players are still 1-indexed with regards to colorization and such
		}

		return players;
	}
}
