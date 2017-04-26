/*
 * Copyright (c) 2015, Josh Baird
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

import com.esotericsoftware.minlog.Log;
import java.util.concurrent.Callable;

/**
 * Class for creating updaters to run things in the background
 *
 * @author Josh Baird
 */
public class Updater {

	public final static int DEFAULT_DELAY = 1000;

	/**
	 * Create a new Thread that will periodically run the given function
	 *
	 * @param description Short description of updaters function
	 * @param function Callable to call periodically
	 * @param delayMills The delay between update ticks in milliseconds
	 *
	 * @return A Thread, not started
	 */
	public static Thread createUpdater(final String description, final Callable function, final int delayMills) {

		Runnable updater = new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						Log.trace("updater: " + description, "Updater tick");

						function.call();
						Thread.sleep(delayMills);
					}
				} catch (InterruptedException ex) {
					Log.info("updater: " + description, "Updater was interrupted", ex);
				} catch (Exception e) {
					Log.error("updater: " + description, e);
				}
			}
		};

		Thread updaterThread = new Thread(updater, "updater: " + description);
		return updaterThread;
	}

	/**
	 * Create a new Thread that will periodically run the given function
	 *
	 * @param description Short description of updaters function
	 * @param function Callable to call periodically
	 *
	 * @return A Thread, not started yet
	 */
	public static Thread createUpdater(final String description, final Callable function) {
		return createUpdater(description, function, DEFAULT_DELAY);
	}
}
