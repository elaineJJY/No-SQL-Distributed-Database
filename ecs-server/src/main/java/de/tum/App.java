package de.tum;

import de.tum.communication.ServerLogger;
import java.io.IOException;

import de.tum.communication.ParseCommand;
import io.grpc.Server;
import java.util.logging.Logger;

public class App {
	private Server server;
	private static final Logger logger = Logger.getLogger("ECS Server");

	public static void main(String[] args) throws IOException {
		//final App server = new App();
		ParseCommand parseCommand = new ParseCommand(args);
		// parse args
		int port = parseCommand.getPort();
		String address = parseCommand.getAddress();
		ServerLogger.INSTANCE.init(parseCommand.getLogLevel(), parseCommand.getLogFile(), logger);
	}
}