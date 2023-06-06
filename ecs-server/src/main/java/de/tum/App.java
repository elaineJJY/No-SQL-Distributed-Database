package de.tum;

import de.tum.communication.ServerLogger;
import java.io.IOException;

import de.tum.communication.ParseCommand;
import de.tum.communication.grpc_service.ECSServiceImpl;
import de.tum.node.ConsistentHash;
import io.grpc.Server;
import io.grpc.ServerBuilder;

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
		try {
			ServerBuilder serverBuilder = ServerBuilder.forPort(5152);
			serverBuilder.addService(new ECSServiceImpl());
			Server server = serverBuilder.build();
			server.start();
			server.awaitTermination();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}