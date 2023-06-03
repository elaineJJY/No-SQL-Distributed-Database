package de.tum;

import java.io.IOException;

import de.tum.communication.ECSServer;
import de.tum.communication.ParseCommand;
import de.tum.communication.Registry;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class App {

	private Server server;
	public static void main(String[] args) {
		final App server = new App();
		ParseCommand parseCommand = new ParseCommand(args);

		// parse args
		int port = parseCommand.getPort();
		String address = parseCommand.getAddress();

		ECSServer ecsServer = new ECSServer();
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