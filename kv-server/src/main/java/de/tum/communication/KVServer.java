package de.tum.communication;

import de.tum.common.Help;
import de.tum.common.ServerLogger;
import de.tum.node.ConsistentHash;

import de.tum.node.Node;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.logging.Logger;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class KVServer {
	private final ConsistentHash metaData;
	private static final Logger LOGGER = ServerLogger.INSTANCE.getLogger();
	private static Selector selector;
	private static ServerSocketChannel ssChannel;
	private Node node;
	public static final int KV_LISTEN_ECS_PORT = 5200;

	// detach read and write buffer
	private static final ByteBuffer readBuffer = ByteBuffer.allocate(1024);
	private static final ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

	public KVServer(Node node) {
		this.metaData = ConsistentHash.INSTANCE;
		this.node = node;
	}

	/**
	 * Start server
	 * @param address server address
	 * @param port server port to listen
	 * @throws Exception
	 */
	public void start(String address, int port) throws Exception {

		//Now KVServer needs to provide RPC service for ECSService
		ServerBuilder rpcServerBuilder = ServerBuilder.forPort(KV_LISTEN_ECS_PORT);
		rpcServerBuilder.addService(node);
		Server rpcServer = rpcServerBuilder.build();
		rpcServer.start();
		LOGGER.info("RPC service published on port: " + KV_LISTEN_ECS_PORT + ", waiting to receive heartbeat from ECS/Other Servers");

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
		LOGGER.info("KVServer is listening on port " + port + ", ready to receive data from KVClient");

		// select() method without parameter will block until at least one event occurs
		while (selector.select() > 0) {
			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
			while (selectionKeyIterator.hasNext()) {
				SelectionKey next = selectionKeyIterator.next();
				if (next.isAcceptable()) {
					System.out.println("Acceptable");
					accept(next);
				} else if (next.isReadable()) {
					read(next);
				}
				selectionKeyIterator.remove();
			}
		}

		rpcServer.awaitTermination();
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
		readBuffer.clear(); // clear buffer
		SocketChannel socketChannel = (SocketChannel) key.channel();
		int len = socketChannel.read(readBuffer);
		if (len != -1) {
			readBuffer.flip(); // reset position
			byte[] bytes = new byte[readBuffer.remaining()]; // 根据缓冲区的数据长度创建字节数组
			readBuffer.get(bytes); // 将缓冲区的数据读到字节数组中
			String request = new String(bytes).trim();
			System.out.println("Server received: " + request);
			socketChannel.configureBlocking(false);
			//key.interestOps(SelectionKey.OP_READ); // 关心读事件?
			LOGGER.info("Register read event for client: " + socketChannel.getRemoteAddress());

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
	private void putCommandHandler(Node responsibleNode, String[] tokens, SocketChannel socketChannel) throws IOException {
		try {
			StringBuilder sb = new StringBuilder();
			for (int i = 2; i < tokens.length; i++) {
				if (i > 2) {
					sb.append(" ");
				}
				sb.append(tokens[i]);
			}
			String value = sb.toString();
			if (responsibleNode.get(tokens[1]) != null) {
				responsibleNode.put(tokens[1], value);
				String msg = "put_update " + tokens[1];
				send(msg, socketChannel);
			} else {
				responsibleNode.put(tokens[1], value);
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

	private void getCommandHandler(Node responsibleNode, String[] tokens, SocketChannel socketChannel) throws IOException {
		try {
			Object value = responsibleNode.get(tokens[1]);
			if (value != null) {
				String msg = "get_success " + tokens[1] + " " + value;
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

	private void deleteCommandHandler(Node responsibleNode, String[] tokens, SocketChannel socketChannel) throws IOException {
		try {
			Object value = node.get(tokens[1]);
			if (value != null) {
				node.delete(tokens[1]);
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
		Node resopnsibleNode = this.node;
		if (!node.isResponsibleLocal(key)) {
			System.out.println("Not responsible for key: " + key);
			resopnsibleNode = metaData.getResponsibleServerByKey(key);
		}
		switch(tokens[0]) {
			case "put": putCommandHandler(resopnsibleNode, tokens, socketChannel); break;
			case "get": getCommandHandler(resopnsibleNode, tokens, socketChannel); break;
			case "delete": deleteCommandHandler(resopnsibleNode, tokens, socketChannel); break;
			case "quit": socketChannel.close(); break;
			default: send("error Unknown Command", socketChannel);
		}
	}
}
