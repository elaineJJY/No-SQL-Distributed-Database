package de.tum.communication;

import de.tum.common.Help;
import de.tum.common.ServerLogger;
import de.tum.common.StatusCode;
import de.tum.node.ConsistentHash;

import de.tum.node.INode;
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
	private INode node;

	// detach read and write buffer
	private static final ByteBuffer readBuffer = ByteBuffer.allocate(1024);
	private static final ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
	private static final HashMap<String, HashMap<String, String>> snapshots = new HashMap<>();
	public KVServer(Node node) {
		this.metaData = ConsistentHash.INSTANCE;
		this.node = node;
	}

	/**
	 * Start server
	 *
	 * @param address server address
	 * @param port    server port to listen
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
		LOGGER.info(
			"KVServer is listening on port " + port + ", ready to receive data from KVClient");

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
	 *
	 * @param key
	 * @throws IOException
	 */
	private void accept(SelectionKey key) throws IOException {
		writeBuffer.clear();
		SocketChannel socketChannel = ssChannel.accept();
		socketChannel.configureBlocking(false);
		socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		String message = "Hello client\n";
		ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes());
		writeBuffer.put(message.getBytes());
		socketChannel.write(ByteBuffer.wrap(message.getBytes()));
		LOGGER.info("Accept new client: " + socketChannel.getRemoteAddress());
	}

	/**
	 * Handler to read message from client and echo certain message back to client
	 *
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
		} else {
			socketChannel.close(); // close channel
			key.cancel(); // cancel key
		}
	}

	/**
	 * Send message to client
	 *
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
	 *
	 * @param tokens
	 * @throws IOException
	 */
	private StatusCode putCommandHandler(INode responsibleNode, INode backupNode, String[] tokens) throws Exception {
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
				return StatusCode.UPDATED_CONTENT;
			} else {
				responsibleNode.put(tokens[1], value);
				backupNode.putBackup(tokens[1], value);
				return StatusCode.PUT_CONTENT;
			}
		} catch (Exception e) {
			throw new Exception("put error");
		}
	}

	/**
	 * Command Handler for get
	 *
	 * @param tokens
	 * @throws IOException
	 */

	private String getCommandHandler(INode responsibleNode, String[] tokens) throws Exception {
		String value = responsibleNode.get(tokens[1]);
		if (value == null) {
			throw new Exception("not found");
		}
		return value;
	}

	/**
	 * Command Handler for delete
	 *
	 * @param tokens
	 * @throws IOException
	 */

	private StatusCode deleteCommandHandler(INode responsibleNode, String[] tokens) throws Exception {
		String value = responsibleNode.get(tokens[1]);
		if (value != null) {
			responsibleNode.delete(tokens[1]);
			return StatusCode.DELETE_CONTENT;
		} else {
			throw new Exception("delete error");
		}
	}

	/**
	 * Process request from client which is distributed by read handler
	 *
	 * @param request
	 * @param socketChannel
	 * @throws Exception
	 */
	private void process(String request, SocketChannel socketChannel) throws Exception {
		String[] tokens = request.trim().split("\\s+");
		String key = tokens.length > 1 ? tokens[1] : "";
		INode resopnsibleNode = this.node;
		if (!node.isResponsible(key)) {
			resopnsibleNode = metaData.getResponsibleServerByKey(key);
			// TODO: Print Port is not correct
			System.out.println("Node " + this.node.getPort() + " not responsible for key: " + key);
			// Actually get rpcPort
			System.out.println("Responsible Node: " + resopnsibleNode.getPort());
		}
		INode backupNode = metaData.getBackupNodeByKey(key);
		System.out.println("Backup Node: " + backupNode.getPort());

		try {
			switch (tokens[0]) {
				case "put": {
					StatusCode returnValue = putCommandHandler(resopnsibleNode, backupNode, tokens);
					if (returnValue == StatusCode.UPDATED_CONTENT) {
						send("put_updated " + tokens[1], socketChannel);
					}
					if (returnValue == StatusCode.PUT_CONTENT){
						send("put_success " + tokens[1], socketChannel);
					}
					break;
				}
				case "get":
					String value = getCommandHandler(resopnsibleNode, tokens);
					send("get_success " + tokens[1] + " " + value, socketChannel);
					break;
				case "delete":
					deleteCommandHandler(resopnsibleNode, tokens, socketChannel);
					send("delete_success" + tokens[1], socketChannel)''
					break;
				case "quit":
					socketChannel.close();
					break;
				case "multi":
					transaction(socketChannel);
					break;
				default:
					throw new Exception("Invalid Command");
			}
		} catch (Exception e) {
			send("error " + e.getMessage(), socketChannel);
		}
	}



	private void transaction(SocketChannel socketChannel) throws Exception {
//		send("start transaction", socketChannel);
//		List<String> requests = new ArrayList<>();
//		String request = "";
//		int t = 0;
//		// read request
//		while (true) {
//			readBuffer.clear();
//			int len = socketChannel.read(readBuffer);
//			if (len > 0) {
//				readBuffer.flip();
//				byte[] bytes = new byte[readBuffer.remaining()];
//				readBuffer.get(bytes);
//				request = new String(bytes).trim();
//				if (request.equals("commit")) {
//					break;
//				}
//				if (request.split("\\s+").length < 2) {
//					send("Invalid Input, discard all requests", socketChannel);
//					break;
//				}
//				requests.add(request);
//				send("queued", socketChannel);
//			}
//			if (len == -1) {
//				socketChannel.close(); // close channel
//			}
//			LOGGER.info(t + "-Read: " + request);
//		}
//
//		// process request
//		HashMap<String, String> history = new HashMap<>();
//		StringBuilder response = new StringBuilder();
//		int i = 0;
//
//		while (i++ < requests.size()) {
//			LOGGER.info("Processing request: " + requests.get(i - 1));
//			String[] tokens = requests.get(i - 1).trim().split("\\s+");
//
//			try {
//				String key = tokens[1];
//				INode resopnsibleNode = this.node;
//				if (!node.isResponsible(key)) {
//					resopnsibleNode = metaData.getResponsibleServerByKey(key);
//				}
//				INode backupNode = metaData.getBackupNodeByKey(key);
//
//				switch (tokens[0]) {
//					case "put":
//						StatusCode returnValue = putCommandHandler(resopnsibleNode, backupNode, tokens);
//						if (returnValue == StatusCode.UPDATED_CONTENT) {
//							response.append("put_updated ").append(tokens[1]).append("\n");
//						}
//						if (returnValue == StatusCode.PUT_CONTENT) {
//							response.append("put_success ").append(tokens[1]).append("\n");
//						}
//						break;
//					case "get":
//						String value = getCommandHandler(resopnsibleNode, tokens);
//						response.append("get_success ").append(tokens[1]).append(" ").append(value).append("\n");
//						break;
//					case "delete":
//						StatusCode deleteStatus = deleteCommandHandler(resopnsibleNode, tokens);
//						responses.add("delete_success" + tokens[1] + "\n");
//						break;
//					default:
//						rollingBack(history);
//						response.append("Invalid Command, Rolling Back\n");
//						break;
//				}
//
//				// if success, add to history
//				if (!history.containsKey(key)) {
//					history.put(key, resopnsibleNode.get(key));
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				LOGGER.info("Error: " + e.getMessage());
//				response.append(request).append(" error ").append(e.getMessage()).append(" rolling back\n");
//				if (history.size() > 0) {
//					rollingBack(history);
//				}
//			}
//		}
		try {
			List<String> requests = collectTransactions(socketChannel);
			List<String> responses = invoke(requests);
			String response = String.join("", responses);
			LOGGER.info("send: " + response);
			send(response, socketChannel);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void rollBack(String transactionId) throws Exception {
		HashMap<String, String> backup = snapshots.get(transactionId);
		LOGGER.info("Rolling back");
		for (String key : backup.keySet()) {
			INode responsibleNode = metaData.getResponsibleServerByKey(key);
			INode backupNode = metaData.getBackupNodeByKey(key);
			responsibleNode.put(key, backup.get(key));
			backupNode.putBackup(key, backup.get(key));
		}
	}

	// listen to transaction request from the client and write them to a list
	private List<String> collectTransactions(SocketChannel socketChannel) throws Exception {
		List<String> requests = new ArrayList<>();
		String request = "";
		int t = 0;
		// read request
		while (true) {
			readBuffer.clear();
			int len = socketChannel.read(readBuffer);
			if (len > 0) {
				readBuffer.flip();
				byte[] bytes = new byte[readBuffer.remaining()];
				readBuffer.get(bytes);
				request = new String(bytes).trim();
				if (request.equals("commit")) {
					break;
				}
				if (request.split("\\s+").length < 2) {
					send("Invalid Input, discard all requests", socketChannel);
					throw new Exception("INVSAL");
				}
				requests.add(request);
				send("queued", socketChannel);
			}
			if (len == -1) {
				socketChannel.close(); // close channel
			}
			LOGGER.info(t + "-Read: " + request);
		}
		return requests;
	}

	/**
	 * Parse transaction request and invoke the corresponding Node
	 * @param allCommands
	 * @return the list of all responses
	 */
	private List<String> invoke(List<String> allCommands) throws Exception {
		String transactionId = UUID.randomUUID().toString();
		HashMap<INode,List<String>> map = new HashMap<>(); //node and correspond requests
		for (String command : allCommands){
			String[] tokens = command.trim().split("\\s+");
			String key = tokens[1];
			INode resopnsibleNode =  metaData.getResponsibleServerByKey(key);
			if (!map.containsKey(resopnsibleNode)){
				map.put(resopnsibleNode, new ArrayList<>());
			}
			map.get(resopnsibleNode).add(command);
		}

		// execute locally and remotely
		boolean failed = false;
		List<String> responses = new ArrayList<>();
		for (INode node : map.keySet()){
			//TODO: executeTransactions RPC
			List<String> response = node.executeTransactions(map.get(node), transactionId); // rpc
			// if any contains "Rolling Back", rollback
			for (String r : response){
				if (r.contains("Rolling Back")){
					failed = true;
					break;
				}
			}
			responses.addAll(response); // rpc
		}

		// if any node fails, rollback
		if(failed){
			for (INode node : map.keySet()){
				//TODO: executeTransactions RPC
				node.rollBack(transactionId); // rpc
			}
		}

		return responses;
	}

	/**
	 * Execute transaction request, which only be locally executed
	 * @param localCommands the list of transaction request
	 * @return the list of response
	 */
	public List<String> executeTransactions(List<String> localCommands, String transactionId) throws Exception {
		// process request
		HashMap<String, String> history = snapshots.getOrDefault(transactionId, new HashMap<>());
		snapshots.put(transactionId, history);
		List<String> responses = new ArrayList<>();
		int i = 0;

		while (i++ < localCommands.size()) {
			LOGGER.info("Processing request: " + localCommands.get(i - 1));
			String[] tokens = localCommands.get(i - 1).trim().split("\\s+");

			try {
				String key = tokens[1];
				INode resopnsibleNode = this.node;
				if (!node.isResponsible(key)) {
					resopnsibleNode = metaData.getResponsibleServerByKey(key);
				}
				INode backupNode = metaData.getBackupNodeByKey(key);

				switch (tokens[0]) {
					case "put":
						StatusCode putStatus = putCommandHandler(resopnsibleNode, backupNode, tokens);
						if (putStatus == StatusCode.UPDATED_CONTENT) {
							responses.add("put_updated " + tokens[1] + "\n");
						}
						if (putStatus == StatusCode.PUT_CONTENT) {
							responses.add("put_success " + tokens[1] + "\n");
						}
						break;
					case "get":
						String value = getCommandHandler(resopnsibleNode, tokens);
						responses.add("get_success " + tokens[1] + " " + value + "\n");
						break;
					case "delete":
						StatusCode deleteStatus = deleteCommandHandler(resopnsibleNode, tokens);
						responses.add("delete_success" + tokens[1] + "\n");
						break;
					default:
						rollBack(transactionId);
						responses.add("Invalid Command, Rolling Back\n");
						break;
				}

				// if success, add to history
				if (!history.containsKey(key)) {
					history.put(key, resopnsibleNode.get(key));
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.info("Error: " + e.getMessage());
				responses.add(localCommands.get(i - 1) + " error " + e.getMessage() + " Rolling back\n");
				if (history.size() > 0) {
					rollBack(transactionId);
				}
			}
		}
		return responses;
	}
}