package de.tum.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import de.tum.common.Help;

/**
 * The Shell Class manage the input and output of the client side and the connection between client
 * and server according to the input command from user and the communication protocol
 *
 * @author Jiajin Yi, Weijian Feng, Mingrun Ma
 * @version 1.0
 */
public class Shell {

	private final Logger logger = Logger.getLogger("Shell");
	private final Client client;

	public Shell() {
		this.client = Client.INSTANCE;
		this.logger.setLevel(Level.INFO);
	}

	/**
	 * To fetch a logger
	 *
	 * @return logger object
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * To set the log level of the logger
	 *
	 * @param newLevel the new log level
	 */
	public void setLoggerLevel(Level newLevel) {
		Level oldLevel = logger.getLevel();
		System.out.println("loglevel set from " + oldLevel.getName() + "to" + newLevel.getName());
		logger.setLevel(newLevel);
	}

	/**
	 * To send a message to the server
	 *
	 * @param msg the message to be sent
	 * @throws Exception if there is an error of sending message
	 */
	public void send(byte[] msg) throws Exception {
		client.send(msg);
		Thread.sleep(500);
		System.out.println(new String(client.receive()));
	}

	/**
	 * To disconnect from the server
	 *
	 * @param tokens the input command
	 * @throws Exception if there is an error of disconnecting
	 */
	public void connect(String[] tokens) throws Exception {
		String address = tokens[1];
		int port;

		// check input port is integer
		if (tokens[2].matches("\\d+")) {
			port = Integer.parseInt(tokens[2]);
		} else {
			throw new IOException("Incorrect Input");
		}

		// handle connect request
		client.startConnection(address, port);  // connect cdb.dis.cit.tum.de 5551
		if (client.isConnected()) {
			System.out.println(new String(client.receive()));
		} else {
			logger.severe("Cannot Connect To Server");
		}
	}

	/**
	 * To distribute coming command with different handles
	 *
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

			switch (tokens[0]) {
				case "quit": {
					quit = true;
					client.stopConnection();
					logger.info("program shutdown");
					System.out.println("program shutdown");
					break;
				}
				case "put":
				case "delete":
				case "get":
				case "keyrange": {
					if (client.isConnected()) {
						send(input.getBytes());
					} else {
						logger.info("Send Fail, Not Connect To Server");
					}
					break;
				}
				case "connect": {
					connect(tokens);
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
				case "disconnect": {
					if (client.isConnected()) {
						client.stopConnection();
						logger.info("server disconnected");
					} else {
						System.out.println("Not connected");
					}
					break;
				}
				case "multi":
				case "commit": {
					if (client.isConnected()) {
						send(input.getBytes());
					} else {
						logger.info("Send Fail, Not Connect To Server");
					}
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

