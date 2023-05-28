package de.tum;

import java.io.IOException;

import de.tum.service.Registry;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class ECSServer {

	private Server server;
	public static void main(String[] args) {
		final ECSServer server = new ECSServer();
		try {
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			server.blockUntilShutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void start() throws IOException {
		int port = 800;
		server = ServerBuilder.forPort(port)
				.addService(new Registry())
				.build()
				.start();
		System.out.println("grpc server start!");
	}

	private void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}
}