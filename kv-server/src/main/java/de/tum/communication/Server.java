package de.tum.communication;

import de.tum.common.ServerLogger;
import de.tum.node.ConsistentHash;
import de.tum.server.database.Database;
import de.tum.node.Node;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.logging.Logger;
import de.tum.common.*;
import grpc_api.KVServerProto;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Server {
	private final ConsistentHash metaData;
	private Node node;
	private Database database;
	private static final Logger LOGGER = ServerLogger.INSTANCE.getLogger();
	//单例模式，设置selector为static
	private static Selector selector;
	private static ServerSocketChannel ssChannel;

	// 分离读写缓冲区，按需设置server的写缓冲区
	private static ByteBuffer readBuffer = ByteBuffer.allocate(1024);
	private static ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

	public Server () {
		this.database = Database.INSTANCE;
		this.metaData = ConsistentHash.INSTANCE;
	}

	/**
	 * Start server
	 * @param address server address
	 * @param port server port to listen
	 * @param helpUsage whether to display help information
	 * @throws Exception
	 */
	public void start(String address, int port, boolean helpUsage) throws Exception {
		this.node = new Node(address, port);

		if (helpUsage) Help.helpDisplay();
		// open selector
		ssChannel = ServerSocketChannel.open();
		// set non-blocking mode
		ssChannel.configureBlocking(false);
		// bind port to listen
		ssChannel.bind(new InetSocketAddress(address, port));
		// get selector
		selector = Selector.open();
		// register listen channel to selector
		ssChannel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("Server is listening on port " + port);

		// register server to ECS
		//registerToECS(address, port);
		registerToECS("127.0.0.1", 5152);
		// 无参select()方法会一直阻塞直到有事件发生
		while (selector.select() > 0) {
			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
			while (selectionKeyIterator.hasNext()) {
				SelectionKey next = selectionKeyIterator.next();
				if (next.isAcceptable()) {
					accept(next);
				} else if (next.isReadable()) {
					read(next);
				}
				selectionKeyIterator.remove();
			}
		}
	}

	/**
	 * Register server to ECS
	 * @param address ECS address
	 * @param port ECS port
	 *
	 */
	public void registerToECS(String address, int port) {
		ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(address, port).usePlaintext().build();
		// 2. 创建stub 代理对象
		try {
			grpc_api.ECSServiceGrpc.ECSServiceBlockingStub ecsService = grpc_api.ECSServiceGrpc.newBlockingStub(managedChannel);
			// 3. 完成rpc调用
			// 3.1 准备请求参数
			// 填充参数
			KVServerProto.NodeMessage.Builder nodeMessageBuilder = grpc_api.KVServerProto.NodeMessage.newBuilder()
					.setHost(address)
					.setPort(port);

			grpc_api.KVServerProto.InitRequest.Builder builder = grpc_api.KVServerProto.InitRequest.newBuilder();

			builder.setIpPort(nodeMessageBuilder.build());
			grpc_api.KVServerProto.InitRequest initRequest = builder.build();
			// 3.2 调用rpc服务，获取响应内容
			grpc_api.KVServerProto.InitResponse helloResponse = ecsService.init(initRequest);

			String result = KVServerProto.InitResponse.getDescriptor().getName();
			System.out.println("result = " + result);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			managedChannel.shutdown();
		}
	}

	/**
	 * Handler to accept new client
	 * @param key
	 * @throws IOException
	 */
	private void accept(SelectionKey key) throws IOException {
		writeBuffer.clear();
		SocketChannel socketChannel = ssChannel.accept();
		socketChannel.configureBlocking(false);
		socketChannel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
		String message = "Hello client\n";
		ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes());
		writeBuffer.put(message.getBytes());
		socketChannel.write(ByteBuffer.wrap(message.getBytes()));
		LOGGER.info("Accept new client: " + socketChannel.getRemoteAddress());
	}

	/**
	 * Handler to read message from client and echo certain message back to client
	 * @param key
	 * @throws Exception
	 */
	private void read(SelectionKey key) throws Exception {
		readBuffer.clear(); //清空读缓存区
		SocketChannel socketChannel = (SocketChannel) key.channel();
		int len = socketChannel.read(readBuffer);
		if (len != -1) {
			readBuffer.flip(); // 重置position
			byte[] bytes = new byte[readBuffer.remaining()]; // 根据缓冲区的数据长度创建字节数组
			readBuffer.get(bytes); // 将缓冲区的数据读到字节数组中
			String request = new String(bytes).trim();
			System.out.println("Server received: " + request);
			socketChannel.configureBlocking(false);
			//key.interestOps(SelectionKey.OP_READ); // 关心读事件?
			LOGGER.info("Register read event for client: " + socketChannel.getRemoteAddress());

			//read到一条request，交给process处理
			//String request =  new String(readBuffer.array());
			LOGGER.info("Received request:" + request);
			process(request, socketChannel);
		}
		else {
			socketChannel.close(); // close channel
			key.cancel(); // cancel key
		}
	}

	/**
	 * Send message to client
	 * @param msg
	 * @param socketChannel
	 * @throws IOException
	 */
	private void send(String msg, SocketChannel socketChannel) throws IOException {
		String newMsg = msg + "\n";
		socketChannel.write(ByteBuffer.wrap(newMsg.getBytes()));
	}

	/**
	 * Command Handler for put
	 * @param tokens
	 * @param socketChannel
	 * @throws IOException
	 */
	private void putCommandHandler(String[] tokens, SocketChannel socketChannel) throws IOException {
		try {
			StringBuilder sb = new StringBuilder();
			for (int i = 2; i < tokens.length; i++) {
				if (i > 2) {
					sb.append(" ");
				}
				sb.append(tokens[i]);
			}
			String value = sb.toString();
			if (database.get(tokens[1]) != null) {
				database.put(tokens[1], value);
				String msg = "put_update " + tokens[1];
				send(msg, socketChannel);
			} else {
				database.put(tokens[1], value);
				String msg = "put_success " + tokens[1];
				send(msg, socketChannel);
			}
		} catch (Exception e){
			String msg = "put_error";
			send(msg, socketChannel);
		}
	}

	/**
	 * Command Handler for get
	 * @param tokens
	 * @param socketChannel
	 * @throws IOException
	 */

	private void getCommandHandler(String[] tokens, SocketChannel socketChannel) throws IOException {
		try {
			Object value = database.get(tokens[1]);
			if (value != null) {
				String msg = "get_success " + tokens[1] + " " + value.toString();
				send(msg, socketChannel);
			}
			else {
				String msg = "get_error " + tokens[1];
				send(msg, socketChannel);
			}
		} catch (Exception e) {
			String msg = "get_error " + tokens[1];
			send(msg, socketChannel);
		}
	}

	/**
	 * Command Handler for delete
	 * @param tokens
	 * @param socketChannel
	 * @throws IOException
	 */

	private void deleteCommandHandler(String[] tokens, SocketChannel socketChannel) throws IOException {
		try {
			Object value = database.get(tokens[1]);
			if (value != null) {
				database.delete(tokens[1]);
				String msg = "delete_success " + tokens[1];
				send(msg, socketChannel);
			}
			else {
				String msg = "delete_error " + tokens[1];
				send(msg, socketChannel);
			}
		} catch (Exception e) {
			String msg = "delete_error" + tokens[1];
			send(msg, socketChannel);
		}
	}

	/**
	 * Process request from client which is distributed by read handler
	 * @param request
	 * @param socketChannel
	 * @throws Exception
	 */
	private void process(String request, SocketChannel socketChannel) throws Exception {
		String[] tokens = request.trim().split("\\s+");
		String key = tokens[1];
		if (node.isResponsible(key)) {
			switch(tokens[0]) {
				case "put": putCommandHandler(tokens, socketChannel); break;
				case "get": getCommandHandler(tokens, socketChannel); break;
				case "quit": socketChannel.close(); break;
				case "delete": deleteCommandHandler(tokens, socketChannel); break;
				default: send("error Unknown Command", socketChannel);
			}
		} else {
			send("keyrange_success" + metaData.toString(), socketChannel);
		}
	}
}
