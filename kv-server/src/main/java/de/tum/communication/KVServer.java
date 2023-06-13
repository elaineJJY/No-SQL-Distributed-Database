package de.tum.communication;

import com.alibaba.fastjson2.JSON;
import de.tum.common.KVMessage;
import de.tum.common.KVMessageBuilder;
import de.tum.common.ServerLogger;
import de.tum.node.MetaData;
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
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class KVServer {
	private final MetaData metaData;
	private static final Logger LOGGER = ServerLogger.INSTANCE.getLogger();
	private static Selector selector;
	private static ServerSocketChannel ssChannel;
	private Node node;

	// detach read and write buffer
	private static final ByteBuffer readBuffer = ByteBuffer.allocate(1024);
	private static final ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

	public KVServer(Node node) {
		this.metaData = MetaData.INSTANCE;
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
		send(message, socketChannel);
//		ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes());
//		writeBuffer.put(message.getBytes());
//		socketChannel.write(ByteBuffer.wrap(message.getBytes()));
		LOGGER.info("Accept new client: " + socketChannel.getRemoteAddress());
	}

	public KVMessage requestToKVMessage(String request) {
		String[] tokens = request.split(" ");
		if (tokens.length < 3) {
			String regex = "^(\\w+)\\s*(\\w*)$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(request);
			if (matcher.find()) {
				String command = matcher.group(1);
				String key = matcher.group(2);
			}
			return KVMessageBuilder.create().command().key(key).build();
		} else {
			String regex = "^(\\w+)\\s+(\\w+)\\s+(.*)$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(request);
			if (matcher.find()) {
				String command = matcher.group(1);
				String key = matcher.group(2);
				String value = matcher.group(3);
			}
		}
	}

	private void read(SelectionKey selectionKey) throws Exception {
		readBuffer.clear();
		SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
		int len = socketChannel.read(readBuffer);
		if (len != -1) {
			readBuffer.flip();
			byte[] bytes = new byte[readBuffer.remaining()];
			readBuffer.get(bytes);
			String request = new String(bytes);	//receive request from client.
			socketChannel.configureBlocking(false);
			LOGGER.info("Register read event for client: " + socketChannel.getRemoteAddress());

			try {
				LOGGER.info("Register read event from another server: " + socketChannel.getRemoteAddress());
				LOGGER.info("Received request from another server:" + request);
				KVMessage msg = JSON.parseObject(request, KVMessage.class);
				process(msg, socketChannel);
			} catch (Exception e) {
				LOGGER.info("Register read event from client: " + socketChannel.getRemoteAddress());
				LOGGER.info("Received request from Client:" + request);
				process(requestToKVMessage(request), socketChannel);
			}
		}
		else {
			socketChannel.close(); // close channel
			selectionKey.cancel(); // cancel key
		}
	}

	private void send(String msg, SocketChannel socketChannel) throws IOException {
		String newMsg = msg + "\n";
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


	private void process(KVMessage msg, SocketChannel socketChannel) throws Exception {
		String command = msg.getCommand();
		String key = msg.getKey();
		Node resopnsibleNode = metaData.getResponsibleNodeByKey(key);

		if (!node.isResponsible(key)) {
			System.out.println("Node " + this.node.getPort() + " not responsible for key: " + key);
			send(JSON.toJSONString(msg), metaData.getResponsibleNodeByKey(key).getSocketChannel());
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
