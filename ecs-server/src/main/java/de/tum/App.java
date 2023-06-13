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
		int port = parseCommand.getPort();
		String address = parseCommand.getAddress();
		boolean helpUsage = parseCommand.getHelpUsage();
		ServerLogger.INSTANCE.init(parseCommand.getLogLevel(), parseCommand.getLogFile(), logger);

		ECSServer ecsServer = new ECSServer(address, port);
		ecsServer.start(helpUsage);
		ecsServer.blockUntilShutdown();
	}
}




//	public void test() {
//		ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 5200).usePlaintext().build();
//		KVServiceGrpc.KVServiceBlockingStub kvServiceStub = KVServiceGrpc.newBlockingStub(managedChannel);
//		Node node = new Node("localhost", 5151);
//		try {
//			kvServiceStub.toString(Empty.newBuilder().build());
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		} finally {
//			managedChannel.shutdown();
//		}
//	}
























//	public static void main(String[] args) throws IOException {
//		// parse args
//		ParseCommand parseCommand = new ParseCommand(args);
//		int port = parseCommand.getPort();
//		String address = parseCommand.getAddress();
//		ServerLogger.INSTANCE.init(parseCommand.getLogLevel(), parseCommand.getLogFile(), logger);
//
//		ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 5152).usePlaintext().build();
//		while (true) {
//			// 2. 创建stub 代理对象
//			try {
//				KVServiceGrpc.KVServiceBlockingStub kvServiceBlockingStub = KVServiceGrpc.newBlockingStub(managedChannel);
//				// 3. 完成rpc调用
//				// 3.1 准备请求参数
//				// 填充参数
//				Empty request = Empty.newBuilder().build();
//				Empty response = kvServiceBlockingStub.toString(request);
//				System.out.println(response);
//			} catch (Exception e) {
//				System.out.println("ToString failed");
//			} finally {
//				managedChannel.shutdown();
//			}
//		}
//	}

//	public static void main(String[] args) throws IOException {
//		//final App server = new App();
//		ParseCommand parseCommand = new ParseCommand(args);
//		// parse args
//		int port = parseCommand.getPort();
//		String address = parseCommand.getAddress();
//		ServerLogger.INSTANCE.init(parseCommand.getLogLevel(), parseCommand.getLogFile(), logger);
//
//		try {
//			// 创建ServerSocket，监听指定IP和端口
//			ServerSocket serverSocket = new ServerSocket(5154, 50, InetAddress.getByName("localhost"));
//			System.out.println("ECS starts, waiting for connection...");
//
//			while (true) {
//				// 接受客户端连接
//				Socket clientSocket = serverSocket.accept();
//				System.out.println("KVServer " + clientSocket.getInetAddress().getHostAddress() + " connected");
//				// rpc init
//				ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 5152).usePlaintext().build();
//				// 2. 创建stub 代理对象
//				try {
//					ECSServiceGrpc.ECSServiceBlockingStub registerService = ECSServiceGrpc.newBlockingStub(managedChannel);
//					// 3. 完成rpc调用
//					// 3.1 准备请求参数
//					// 填充参数
//
//					// 3.2 调用rpc服务，获取响应内容
//					registerService.toString(Empty.newBuilder().build());
//
////					String result = registerResponse.getRepeatedField();
////					System.out.println("result = " + result);
//				} catch (Exception e) {
//					throw new RuntimeException(e);
//				} finally {
//					managedChannel.shutdown();
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

//}