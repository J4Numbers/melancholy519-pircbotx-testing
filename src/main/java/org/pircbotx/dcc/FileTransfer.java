/**
 * Copyright (C) 2010-2013 Leon Blakey <lord.quackstar at gmail.com>
 *
 * This file is part of PircBotX.
 *
 * PircBotX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PircBotX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PircBotX. If not, see <http://www.gnu.org/licenses/>.
 */
package org.pircbotx.dcc;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.pircbotx.Configuration;
import org.pircbotx.User;

/**
 * A general active DCC file transfer
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */
@RequiredArgsConstructor
public abstract class FileTransfer {
	@NonNull
	protected final Configuration configuration;
	@NonNull
	protected final Socket socket;
	@NonNull
	@Getter
	protected final User user;
	@NonNull
	@Getter
	protected final File file;
	@NonNull
	@Getter
	protected final long startPosition;
	@Getter
	protected long bytesTransfered;
	@Getter
	protected DccState state = DccState.INIT;
	protected final Object stateLock = new Object();

	/**
	 * Transfer the file to the user
	 * @throws IOException If an error occurred during transfer
	 */
	public void transfer() throws IOException {
		//Prevent being called multiple times
		if (state != DccState.INIT)
			synchronized (stateLock) {
				if (state != DccState.INIT)
					throw new RuntimeException("Cannot receive file twice (Current state: " + state + ")");
			}
		state = DccState.RUNNING;

		transferFile();

		state = DccState.DONE;
	}

	protected abstract void transferFile() throws IOException;
	
	/**
	 * Is the transfer finished?
	 * @return True if its finished
	 */
	public boolean isFinished() {
		return state == DccState.DONE; 
	}
}
