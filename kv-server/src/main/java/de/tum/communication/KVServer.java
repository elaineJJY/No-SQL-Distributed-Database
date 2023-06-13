package de.tum.communication;

import com.alibaba.fastjson2.JSON;
import de.tum.common.KVMessage;
import de.tum.common.ServerLogger;
import de.tum.node.ConsistentHash;
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

public class KVServer {
	private final ConsistentHash metaData;
	private static final Logger LOGGER = ServerLogger.INSTANCE.getLogger();
	private static Selector selector;
	private static ServerSocketChannel ssChannel;
	private Node node;

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

	private void read(SelectionKey selectionKey) throws Exception {
		readBuffer.clear(); // clear buffer
		SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
		int len = socketChannel.read(readBuffer);
		if (len != -1) {
			readBuffer.flip(); // reset position
			byte[] bytes = new byte[readBuffer.remaining()]; // 根据缓冲区的数据长度创建字节数组
			readBuffer.get(bytes); // 将缓冲区的数据读到字节数组中

			String request = new String(bytes).trim();

			String command = request.split(" ")[0];
			String key = request.split(" ")[1];

			KVMessage msg = JSON.parseObject(request, KVMessage.class);


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

	private void send(KVMessage msg, SocketChannel socketChannel) throws IOException {
		String newMsg = JSON.toJSONString(msg);
		socketChannel.write(ByteBuffer.wrap(newMsg.getBytes()));
	}

	private void putCommandHandler(Node responsibleNode, Node backupNode, String[] tokens, SocketChannel socketChannel) throws IOException {
		try {
			StringBuilder sb = new StringBuilder();
			for (int i = 2; i < tokens.length; i++) {
				if (i > 2) {
					sb.append(" ");
				}
				sb.append(tokens[i]);
			}
			String value = sb.toString();
			if (responsibleNode.hasKey(tokens[1])) {
				backupNode.putBackup(tokens[1], value);
				responsibleNode.put(tokens[1], value);
				String msg = "put_update " + tokens[1];
				send(msg, socketChannel);
			} else {
				responsibleNode.put(tokens[1], value);
				backupNode.putBackup(tokens[1], value);
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
			String value = responsibleNode.get(tokens[1]);
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

	private void deleteCommandHandler(Node responsibleNode, Node backupNode, String[] tokens, SocketChannel socketChannel) throws IOException {
		try {
			String value = responsibleNode.get(tokens[1]);
			if (value != null) {
				responsibleNode.delete(tokens[1]);
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
	private void process(KVMessage msg, SocketChannel socketChannel) throws Exception {
		String command = msg.getCommand();
		String key = msg.getKey();
		Node resopnsibleNode = metaData.getResponsibleNodeByKey(key);

		if (!node.isResponsible(key)) {
			System.out.println("Node " + this.node.getPort() + " not responsible for key: " + key);
			send(msg, metaData.getSocketForNode(resopnsibleNode));
			System.out.println("Responsible Node: " + resopnsibleNode.getPort());
		}

		Node backupNode = metaData.getBackupNodeByKey(key);
		System.out.println("Backup Node: " + backupNode.getPort());

		switch (command) {
			case "put":
				putCommandHandler(resopnsibleNode, backupNode, tokens, socketChannel);
				break;
			case "get":
				getCommandHandler(resopnsibleNode, tokens, socketChannel);
				break;
			case "delete":
				deleteCommandHandler(resopnsibleNode, backupNode, tokens, socketChannel);
				break;
			case "quit":
				socketChannel.close();
				break;
			default:
				send("error Unknown Command", socketChannel);
		}
	}
}
