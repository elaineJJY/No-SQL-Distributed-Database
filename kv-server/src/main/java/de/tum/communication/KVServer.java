package de.tum.communication;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import de.tum.common.*;
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
					accept(next);
//					selectionKeyIterator.remove();
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
//		while (!socketChannel.finishConnect()) {
//			// wait for connection
//		}
		socketChannel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);

		String message = "Hello client\n";
		send(message, socketChannel);
//		ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes());
//		writeBuffer.put(message.getBytes());
//		socketChannel.write(ByteBuffer.wrap(message.getBytes()));
	}

	public KVMessage requestToKVMessage(String request) {
		String[] tokens = request.split(" ");
		if (tokens.length < 3) {
			String regex = "^(\\w+)\\s+(.*)$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(request);
			String command = null;
			String key = null;
			if (matcher.find()) {
				command = matcher.group(1);
				key = matcher.group(2);
			}
			KVMessage message = new KVMessage();
			message.parserCommand(command);
			message.setKey(key);
			return message;
		} else {
			String regex = "^([a-zA-Z]+)\\s+([^\\s]+)\\s+(.*)$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(request);
			String command = null;
			String key = null;
			String value = null;
			if (matcher.find()) {
				command = matcher.group(1);
				key = matcher.group(2);
				value = matcher.group(3);
			}
			System.out.println(command);
			System.out.println(key);
			System.out.println(value);
			KVMessage message = new KVMessage();
			message.parserCommand(command);
			message.setKey(key);
			message.setValue(value);
			System.out.println(JSONObject.toJSONString(message));
			return message;
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

			//TODO: optimize the parse process
			try {
				KVMessage msg = JSON.parseObject(request, KVMessage.class);
				if (msg.getCommand() == null) {
					throw new Exception("NOT A KVMessage");
				}
				LOGGER.info("Received request from SERVER: " + socketChannel.getRemoteAddress() + " Request: " + request);
				String responseTOKVServer = KVMessageParser.processMessage(msg, this.node);
				send(responseTOKVServer, socketChannel); // socketChannel = KV-Server
			} catch (Exception e) {
//				e.printStackTrace();
				try {
					ECSMessage msg = JSON.parseObject(request, ECSMessage.class);
					if (msg.getCommand() == null) {
						throw new Exception("NOT A ECSMessage");
					}
					LOGGER.info("Received request from ECS: " + socketChannel.getRemoteAddress() + " Request: " + request);
					StatusCode returnCode = ECSMessageParser.processMessage(msg, this.node);
					send(returnCode.toString(), socketChannel); // socketChannel = ECSServer
				} catch (Exception exp) {
					if (request.contains("keyrange_read")) {
						String repsonse = "keyrange_read_success "+ metaData.getAllRange();
						repsonse = repsonse.substring(0, repsonse.length() - 1) + "\r\n";
						send(repsonse, socketChannel);
						LOGGER.info(repsonse);
						return;
					}
					if (request.contains("keyrange")) {
						String repsonse = "keyrange_success " + metaData.getRange() ;
						repsonse = repsonse.substring(0, repsonse.length() - 1) + "\r\n";
						send(repsonse, socketChannel);
						LOGGER.info(repsonse);
						return;
					}
					process(requestToKVMessage(request), socketChannel); // socketChannel = Client
				}
			}
		}
		else {
			socketChannel.close(); // close channel
			selectionKey.cancel(); // cancel key
		}
	}

	// Send to client
	private void send(String msg, SocketChannel socketChannel) throws IOException {
		socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
		LOGGER.info("Sent:" + msg);
	}

	private void process(KVMessage msg, SocketChannel clientSocketChannel) throws Exception {
		String key = msg.getKey();
		String value = msg.getValue();
		Node resopnsibleNode = metaData.getResponsibleNodeByKey(key);
		KVMessage.Command command = msg.getCommand();
		//Remote
		if (!this.node.isResponsible(key)) {
			System.out.println("Node " + this.node.getPort() + " not responsible for key: " + key);
			System.out.println("Responsible Node: " + resopnsibleNode.getPort());
			if (command == KVMessage.Command.PUT) {
				String message = "server_not_responsible";
				send(message, clientSocketChannel);
				return;
			}
			String response = KVMessageBuilder.create()
					.command(msg.getCommand())
					.key(key)
					.value(value)
					.statusCode(StatusCode.REDIRECT)
					.socketChannel(resopnsibleNode.getSocketChannel())
					.send()
					.receive();
			send(response, clientSocketChannel); // send response from other KV-Server back to client socket
			return;
		}
		String response = KVMessageParser.processMessage(msg, this.node);
		send(response, clientSocketChannel);
	}
}

	// local command from client
//		switch (command) {
//			case "put":
//				putCommandHandler(msg, clientSocketChannel);
//				break;
//			case "get":
//				getCommandHandler(msg, clientSocketChannel);
//				break;
//			case "delete":
//				deleteCommandHandler(msg, clientSocketChannel);
//				break;
//			case "quit":
//				clientSocketChannel.close();
//				break;
//			default:
//				send("error Unknown Command", clientSocketChannel);
//		}

//	private void putCommandHandler(KVMessage message, SocketChannel socketChannel) throws Exception {
//		try {
//			for (int i = 2; i < tokens.length; i++) {
//				if (i > 2) {
//					sb.append(" ");
//				}
//				sb.append(tokens[i]);
//			}
//			String value = sb.toString();
//			if (responsibleNode.hasKey(tokens[1])) {
//				String msg = responsibeNode.put(tokens[1], value);
//				String msg = "put_update " + tokens[1];
//				send(msg, socketChannel);
//			} else {
//				responsibleNode.put(tokens[1], value);
//				String msg = "put_success " + tokens[1];
//				send(msg, socketChannel);
//			}
//		} catch (Exception e) {
//			String msg = "put_error";
//			send(msg, socketChannel);
//		}
//	}
//
//
//	private void getCommandHandler(Node responsibleNode, String[] tokens, SocketChannel clientSocketChannel) throws IOException {
//		try {
//			String value = responsibleNode.get(tokens[1]);
//			if (value != null) {
//				String msg = "get_success " + tokens[1] + " " + value;
//				send(msg, clientSocketChannel);
//			}
//			else {
//				String msg = "get_error " + tokens[1];
//				send(msg, clientSocketChannel);
//			}
//
//		} catch (Exception e) {
//			String msg = "get_error " + tokens[1];
//			send(msg, clientSocketChannel);
//		}
//	}
//
//	/**
//	 * Command Handler for delete
//	 * @param tokens
//	 * @param socketChannel
//	 * @throws IOException
//	 */
//
//	private void deleteCommandHandler(Node responsibleNode, String[] tokens, SocketChannel socketChannel) throws IOException {
//		try {
//			String value = responsibleNode.get(tokens[1]);
//			if (value != null) {
//				responsibleNode.delete(tokens[1]);
//				String msg = "delete_success " + tokens[1];
//				send(msg, socketChannel);
//			}
//			else {
//				String msg = "delete_error " + tokens[1];
//				send(msg, socketChannel);
//			}
//		} catch (Exception e) {
//			String msg = "delete_error" + tokens[1];
//			send(msg, socketChannel);
//		}
//	}

