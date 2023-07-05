package de.tum.node;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import de.tum.common.*;
import de.tum.communication.KVServer;
import de.tum.database.IDatabase;

import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.jar.JarException;
import java.util.logging.Logger;

public class Node {
	private final Logger LOGGER = ServerLogger.INSTANCE.getLogger();
	KVServer server;
	private String host;
	private int port;
	private IDatabase mainDatabase;
	private IDatabase backupDatabase;
	private SocketChannel socketChannel;
	private SocketChannel ecsSocketChannel; //Only for local node

	// remote Node
	public Node (String host, int port, SocketChannel socketChannel) {
		this.host = host;
		this.port = port;
		this.socketChannel = socketChannel;
	}

	// local Node
	public Node(String host, int port, IDatabase mainDatabase, IDatabase backupDatabase) throws Exception {
		this.host = host;
		this.port = port;
		this.mainDatabase = mainDatabase;
		this.backupDatabase = backupDatabase;
		socketChannel = null;
	}

	/**
	 * Start NIO KVServer
	 * @throws Exception
	 */
	public void startKVServer() throws Exception {
		server = new KVServer(this);
		server.start(host, port);
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public void setECSSocketChannel(SocketChannel ecsSocketChannel) {
		this.ecsSocketChannel = ecsSocketChannel;
	}

	public String getHost() { return host; }

	public int getPort() { return port; }

	public long heartbeat() { return new Date().getTime(); }

	/**
	 * Get the range of this node in the ring
	 * @return String range
	 */
	public Range getRange(DataType dataType) {
		Node from = null;
		Node to = null;
		if (dataType == DataType.DATA) {
			from = MetaData.INSTANCE.getPreviousNode(this);
			to = this;
		}
		if (dataType == DataType.BACKUP) {
			from = this;
			to = MetaData.INSTANCE.getNextNode(this);
		}
		if (from == null || to == null) {
			throw new NoSuchElementException("No such node");
		}
		return new Range(MetaData.INSTANCE.getHash(from), MetaData.INSTANCE.getHash(to));
	}

	/**
	 * Override the equals method, which will be used to compare two nodes according to their toString <ip:port>
	 * @param obj
	 * @return
	 */
	// TODO
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Node) {
			Node node = (Node) obj;
			return node.toString().equals(this.toString());
		}
		return false;
	}

	/**
	 * Print the node in the format of string <ip:port>, which will be used as the key in the Hash ring
	 * @return <ip:port> string
	 */
	@Override
	public String toString() { return host + ":" + port; }

	/**
	 * Check if this node is responsible for the given (key, value) pair
	 * @param key
	 * @return true if this node is responsible for the given (key, value) pair
	 */
	// isResponsible, copy, get, put, delete will only be called by other KVServer
	public boolean isResponsible(String key) throws NullPointerException {
		if (MetaData.INSTANCE.getRing().size() == 1) {
			return true;
		}
		String keyHash = MD5Hash.hash(key);	//get hash of key

		String currNodeHash = MetaData.INSTANCE.getHash(this);	//get hash of this node
		SortedMap<String, Node> tailMap = MetaData.INSTANCE.getRing().tailMap(keyHash);
		if (tailMap.isEmpty()) {
			return MetaData.INSTANCE.getRing().firstKey().equals(currNodeHash);
		} else {
			return tailMap.firstKey().equals(currNodeHash);
		}
	}

	/**
	 * Get range of data from the remote node
	 *
	 * @param where
	 * @param range
	 * @return the data that in this range
	 */
	public HashMap<String, String> copy(DataType where, Range range) throws Exception {
		try {
			// only for remote node
			if (this.socketChannel!=null){
				String response = KVMessageBuilder.create()
						.command(KVMessage.Command.COPY)
						.dataType(where)
						.range(range)
						.socketChannel(this.socketChannel)
						.send()
						.receive();
				LOGGER.info("Get Data from other Node:" + response.trim());
				try{
					JSON.parse(response);
				}
				catch (JSONException e) {
					response =  KVMessageBuilder.create()
							.command(KVMessage.Command.COPY)
							.dataType(where)
							.range(range)
							.socketChannel(this.socketChannel)
							.send()
							.receive();
				}
				HashMap<String, String> data = (HashMap<String, String>) JSONObject.parseObject(response, HashMap.class);
				return data;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		IDatabase database = where==DataType.DATA ? this.mainDatabase : this.backupDatabase;
		return database.getDataByRange(range);
	}
	public HashMap<String, String> getDataByRange(DataType where, Range range) throws Exception{
		IDatabase database = where==DataType.DATA ? mainDatabase : backupDatabase;
		return database.getDataByRange(range);
	}

	public String get(String key) throws Exception {
		if (isResponsible(key)){
			return mainDatabase.get(key);
		}
		return backupDatabase.get(key);
	}

	public StatusCode put(String key, String value) throws Exception {
		StatusCode code = StatusCode.SERVER_ERROR;
		try {
			if (isResponsible(key)){
				code = mainDatabase.hasKey(key) ? StatusCode.put_update : StatusCode.put_success;
				mainDatabase.put(key, value);
				LOGGER.info("Put data on database " + this.port + " <" + key + ":" + value + ">");
			}
		}
		finally {
			return code;
		}

	}

	//TODO
	public CompletableFuture<StatusCode> putDataToBackupNode(String key, String value) throws Exception  {
		List<Node> backupNodes = MetaData.INSTANCE.getBackupNodeByKey(key);
		// no backups
		if (backupNodes.isEmpty()) {
			backupDatabase.put(key, value);
			return CompletableFuture.completedFuture(StatusCode.OK); // Return the result immediately
		}
		CompletableFuture<StatusCode> finalResult = new CompletableFuture<>();
		CompletableFuture<Void> allFutures = CompletableFuture.allOf(
				backupNodes.stream()
						.filter(node -> !node.equals(this))
						.map(backUpNode -> CompletableFuture.supplyAsync(() -> {
							try {
								KVMessageBuilder.create()
										.command(KVMessage.Command.PUT)
										.key(key)
										.value(value)
										.dataType(DataType.BACKUP)
										.socketChannel(backUpNode.getSocketChannel())
										.send()
										.receive();
								return StatusCode.put_success; // Return the desired result
							} catch (Exception e) {
								// Handle exceptions here
								return StatusCode.SERVER_ERROR; // Return an appropriate error code
							}
						}))
						.toArray(CompletableFuture[]::new)
		);

		allFutures.thenRun(() -> {
			for (Node backupNode : backupNodes) {
				if (backupNode.equals(this)) {
					try {
						backupDatabase.put(key, value);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					finalResult.complete(StatusCode.put_success);
					break;
				}
			}
			if (!finalResult.isDone()) {
				finalResult.complete(StatusCode.SERVER_ERROR); // Return an appropriate error code
			}
		});

		return finalResult;

	}

	public void putBackup(String key, String value) throws Exception {
		backupDatabase.put(key, value);
		LOGGER.info("Put backup data on database " + this.port + " <" + key + ":" + value + ">");
	}
	public void deleteBackup(String key) throws Exception {
		backupDatabase.delete(key);
	}

	public StatusCode delete(String key) throws Exception {
		StatusCode code = StatusCode.SERVER_ERROR;
		try {
			if (isResponsible(key)){
				code = mainDatabase.hasKey(key) ? StatusCode.OK : StatusCode.NOT_FOUND;
				mainDatabase.delete(key);
				LOGGER.info("Delete data on database " + this.port + ": " + key );
			}
		}
		finally {
			return code;
		}
	}

	// TODO
	public CompletableFuture<StatusCode> deleteDataFromBackupNode(String key) throws Exception {
		List<Node> backupNodes = MetaData.INSTANCE.getBackupNodeByKey(key);

		if (backupNodes.isEmpty()) {
			backupDatabase.delete(key);
			return CompletableFuture.completedFuture(StatusCode.OK); // Return the result immediately
		}
		CompletableFuture<StatusCode> finalResult = new CompletableFuture<>();
		CompletableFuture<Void> allFutures = CompletableFuture.allOf(
				backupNodes.stream()
						.filter(node -> !node.equals(this))
						.map(backupNode -> CompletableFuture.supplyAsync(() -> {
							try {
								KVMessageBuilder.create()
										.command(KVMessage.Command.DELETE)
										.key(key)
										.dataType(DataType.BACKUP)
										.socketChannel(backupNode.getSocketChannel())
										.send()
										.receive();
								return StatusCode.OK; // Return the desired result
							} catch (Exception e) {
								// Handle exceptions here
								return StatusCode.SERVER_ERROR; // Return an appropriate error code
							}
						}))
						.toArray(CompletableFuture[]::new)
		);

		allFutures.thenRun(() -> {
			finalResult.complete(StatusCode.OK); // Return the result immediately
		});

		return finalResult;

	}

	public boolean hasKey(String key) throws Exception {
		return mainDatabase.hasKey(key);
	}

	// init, recover, updateRing, deleteExpiredData will only be called by ECS
	public StatusCode init() {
		try{
			System.out.println("KVServer:" + this.host + ":" + this.port + " starts data transfer");
			if (MetaData.INSTANCE.getRing().size() != 1) {
				Node nextNode = MetaData.INSTANCE.getNextNode(this);
				if (!nextNode.equals(this)) {
					HashMap<String, String> mainData = nextNode.copy(DataType.DATA, getRange(DataType.DATA));
					mainDatabase.saveAllData(mainData);
				}
				if (MetaData.INSTANCE.getRing().size() > 2) {
					Node previousNode = MetaData.INSTANCE.getPreviousNode(this);
					if (!previousNode.equals(this)) {
						HashMap<String, String> backup = previousNode.copy(DataType.BACKUP, getRange(DataType.BACKUP));
						backupDatabase.saveAllData(backup);
					}
				}
			}
			System.out.println("Initiation finish, server is ready");
			return StatusCode.OK;
		} catch(Exception e) {
			System.out.println("Data transfer fail");
			return StatusCode.SERVER_ERROR;
		}
	}


	// TODO: Exception
	public StatusCode recover(Node removedNode) {
		try {
			String removedHash = MetaData.INSTANCE.getHash(removedNode);

			// recover data from the removed node
			// If the removed node is the previous node of this node
			Node previousNode = MetaData.INSTANCE.getPreviousNode(this);
			System.out.println(MetaData.INSTANCE.getRing().toString());
			System.out.println("previous node: " + previousNode);

			// recover the main database
			if (MD5Hash.hash(previousNode.getHost() + ":" + previousNode.getPort()).equals(removedHash)) {
				Node newPreviousNode = MetaData.INSTANCE.getPreviousNode(removedNode);
				System.out.println("new previous node: " + newPreviousNode);
				Range dataRangeOfRemovedNode = new Range(MetaData.INSTANCE.getHash(newPreviousNode), removedHash);
				System.out.println("data range of removed node: " + dataRangeOfRemovedNode);
//			try {
//				removedNode.heartbeat(); // check whether the removed node is alive
//				mainDatabase.saveAllData(newPreviousNode.copy(DataType.DATA, dataRangeOfRemovedNode));
//			}
//			catch (Exception e) {
//				System.out.println("Node " + removedNode + " is dead");
//				// recover data from the backup
//				mainDatabase.saveAllData(newPreviousNode.copy(DataType.BACKUP, dataRangeOfRemovedNode));
//			}
				// if work flow goes here, it means the removed node is dead, and we should also close
				// form this node to the removed node
				//TODO
				if (!newPreviousNode.equals(this)) {
					mainDatabase.saveAllData(newPreviousNode.copy(DataType.BACKUP, dataRangeOfRemovedNode));
				} else {
					mainDatabase.saveAllData(this.copy(DataType.BACKUP, dataRangeOfRemovedNode));
				}
			}

			// recover backup from the removed node
			// If the removed node is the next node of this node
			Node nextNode = MetaData.INSTANCE.getNextNode(this);
			System.out.println(MetaData.INSTANCE.getRing().toString());
			if (MD5Hash.hash(nextNode.getHost() + ":" + nextNode.getPort()).equals(removedHash)) {
				Node newNextNode = MetaData.INSTANCE.getNextNode(removedNode); // backupNode 1
				Range backupRangeOfRemovedNode = new Range(removedHash, MetaData.INSTANCE.getHash(newNextNode));
//			try {
//				removedNode.heartbeat(); // check whether the removed node is alive
//				backupDatabase.saveAllData(newNextNode.copy(DataType.BACKUP, backupRangeOfRemovedNode));
//			}
//			catch (Exception e) {
//				System.out.println("Node " + removedNode + " is dead");
//				// recover data from the backup
//				backupDatabase.saveAllData(newNextNode.copy(DataType.DATA, backupRangeOfRemovedNode));
//			}
				if (!newNextNode.equals(this)) {
					backupDatabase.saveAllData(newNextNode.copy(DataType.DATA, backupRangeOfRemovedNode));
				} else {
					backupDatabase.saveAllData(this.copy(DataType.DATA, backupRangeOfRemovedNode));
				}
			}
			// prepre Node
			Node newBackupNode = MetaData.INSTANCE.getPreviousNode(removedNode);
			Node newBackupNode2 = MetaData.INSTANCE.getPreviousNode(newBackupNode);
			if (MetaData.INSTANCE.getHash(this).equals(MetaData.INSTANCE.getHash(newBackupNode2))) {
				Range backupRangeOfRemovedNode = new Range(removedHash, MetaData.INSTANCE.getHash(MetaData.INSTANCE.getNextNode(removedNode)));
//			try {
//				removedNode.heartbeat(); // check whether the removed node is alive
//				backupDatabase.saveAllData(newNextNode.copy(DataType.BACKUP, backupRangeOfRemovedNode));
//			}
//			catch (Exception e) {
//				System.out.println("Node " + removedNode + " is dead");
//				// recover data from the backup
//				backupDatabase.saveAllData(newNextNode.copy(DataType.DATA, backupRangeOfRemovedNode));
//			}
				if (!newBackupNode.equals(this)) {
					backupDatabase.saveAllData(newBackupNode.copy(DataType.BACKUP, backupRangeOfRemovedNode));
				}
			}

			return StatusCode.OK;
		} catch (Exception e) {
			return StatusCode.SERVER_ERROR;
		}
	}

	public StatusCode updateMetaData(HashMap<String, String> addrAndHash) throws Exception {
		try {
			MetaData.INSTANCE.update(addrAndHash);
			return StatusCode.OK;
		} catch (Exception e) {
			return StatusCode.SERVER_ERROR;
		}
	}

	public StatusCode deleteExpiredData(DataType dataType, Range range) throws Exception {
		try {
			IDatabase database = dataType == DataType.DATA ? mainDatabase : backupDatabase;
			database.deleteDataByRange(range);
			return StatusCode.OK;
		} catch (Exception e) {
			return StatusCode.SERVER_ERROR;
		}
	}
}
