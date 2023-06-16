package de.tum;

import de.tum.communication.ECSServer;
import de.tum.communication.ServerLogger;

import de.tum.communication.ParseCommand;

import java.util.logging.Logger;

public class App {

	private static final Logger logger = Logger.getLogger("ECS Server");
	public static void main(String[] args) throws Exception {
		// parse args
		ParseCommand parseCommand = new ParseCommand(args);
		//String bootStrapServerAddress = parseCommand.getBootstrapServerAddress();
		String address = parseCommand.getAddress();
		int port = parseCommand.getPort();
		boolean helpUsage = parseCommand.getHelpUsage();
		ServerLogger.INSTANCE.init(parseCommand.getLogLevel(), parseCommand.getLogFile(), logger);

		// String ecsHost = bootStrapServerAddress.split(":")[0];
		// int ecsPort = Integer.parseInt(bootStrapServerAddress.split(":")[1]);
		ECSServer ecsServer = new ECSServer(address, port);
		ecsServer.start(helpUsage);
	}
}