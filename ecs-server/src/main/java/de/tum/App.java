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


		//ECSServer ecsServer = new Server(port);
		try {
			// 1. 绑定端口
			ServerBuilder serverBuilder = ServerBuilder.forPort(5152);
			// 2. 发布服务
			serverBuilder.addService(new ECSServiceImpl());
			// 3. 创建服务对象
			Server server = serverBuilder.build();
			// 4. 启动服务
			server.start();
			server.awaitTermination();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}