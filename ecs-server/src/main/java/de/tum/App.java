package de.tum;

import de.tum.communication.ECSServer;
import de.tum.communication.ServerLogger;

import de.tum.communication.ParseCommand;

import java.net.ServerSocket;
import java.util.SortedMap;
import java.util.logging.Logger;

public class App {

	private static final Logger logger = Logger.getLogger("ECS Server");
	public static void main(String[] args) throws Exception {
		// parse args
		ParseCommand parseCommand = new ParseCommand(args);
		int port = parseCommand.getPort();
		String address = parseCommand.getAddress();
		boolean helpUsage = parseCommand.getHelpUsage();
		ServerLogger.INSTANCE.init(parseCommand.getLogLevel(), parseCommand.getLogFile(), logger);

		ECSServer ecsServer = new ECSServer(address, port);
		ecsServer.start(helpUsage);

		ecsServer.blockUntilShutdown();
	}
}
