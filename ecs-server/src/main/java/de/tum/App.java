package de.tum;

import de.tum.communication.ServerLogger;
import java.io.IOException;

import de.tum.communication.ECSServer;
import de.tum.communication.ParseCommand;
import de.tum.communication.Registry;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.util.logging.Logger;

public class App {

	private Server server;
	private static final Logger logger = Logger.getLogger("ECS Server");

	public static void main(String[] args) throws IOException {
		final App server = new App();
		ParseCommand parseCommand = new ParseCommand(args);

		// parse args
		int port = parseCommand.getPort();
		String address = parseCommand.getAddress();
		ServerLogger.INSTANCE.init(parseCommand.getLogLevel(), parseCommand.getLogFile(), logger);

		ECSServer ecsServer = new ECSServer(port);
		try {
			ecsServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			ecsServer.blockUntilShutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}