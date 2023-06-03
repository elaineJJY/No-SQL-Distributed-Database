package de.tum.client;

import de.tum.common.Help;
import de.tum.kvStore.KVStore;
import de.tum.kvStore.KVStoreImp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Shell Class manage the input and output of the client side
 * and the connection between client and server
 * according to the input command from user
 * and the communication protocol
 * @author Jiajin Yi, Weijian Feng, Mingrun Ma
 * @version 1.0
 */
public class Shell {
	private final Logger logger = Logger.getLogger("Shell");
	private KVStore client;

	public Shell() {
		this.client = new KVStoreImp();
		this.logger.setLevel(Level.INFO);
	}

	/**
	 * To fetch a logger
	 * @return logger object
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * To set the log level of the logger
	 * @param newLevel the new log level
	 */
	public void setLoggerLevel(Level newLevel) {
		Level oldLevel = logger.getLevel();
		System.out.println("loglevel set from " + oldLevel.getName() + "to" + newLevel.getName());
		logger.setLevel(newLevel);
	}


	/**
	 * To distribute coming command with different handles
	 * @throws Exception if there is an error of handling command
	 */
	public void handleCommand() throws Exception {
		BufferedReader cons = new BufferedReader(new InputStreamReader(System.in));
		boolean quit = false;
		// handle different request
		while (!quit) {
			System.out.print("EchoClient> ");

			String input = cons.readLine();
			String[] tokens = input.trim().split("\\s+");
			// trim(): This method removes any leading or trailing whitespace characters from the input string.
			// split("\\s+"): This method splits the input string into an array of strings based on whitespace characters.

			switch(tokens[0]) {
				case "quit": {
					quit = true;
					client.stopConnection();
					logger.info("program shutdown");
					System.out.println("program shutdown");
					break;
				}
				case "put": {
					System.out.println(Arrays.toString(client.put(tokens[1], tokens[2])));
					break;
				}
				case "get": {
					System.out.println(Arrays.toString(client.get(tokens[1])));
					break;
				}
				case "delete": {
					System.out.println(Arrays.toString(client.delete(tokens[1])));
					break;
				}
				case "connect": {
					if (!client.isConnected()) {
						System.out.print((client.startConnection(tokens[1], Integer.parseInt(tokens[2]))).toString());
						logger.info("server connected");
					}
					else {
						System.out.println("Already connected");
					}
					break;
				}
				case "disconnect": {
					if (client.isConnected()) {
						client.stopConnection();
						logger.info("server disconnected");
					}
					else {
						System.out.println("Not connected");
					}
					break;
				}
				case "logLevel": {
					Level level = Level.parse(tokens[1]);
					if (level != null) {
						setLoggerLevel(level);
					} else {
						logger.severe("Invalid Level");
					}
					break;
				}
				case "help": {
					Help.helpDisplay();
					break;
				}
				default: {
					logger.severe("Unknown command");
					Help.helpDisplay();
					System.out.println("Unknown command");
					break;
				}
			}
		}
	}
}

