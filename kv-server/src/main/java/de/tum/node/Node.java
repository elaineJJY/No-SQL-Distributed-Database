package de.tum.node;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import de.tum.common.KVMessage;
import de.tum.common.KVMessageBuilder;
import de.tum.common.StatusCode;
import de.tum.communication.KVServer;
import de.tum.database.IDatabase;

import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.SortedMap;

public class Node {

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
		String response = KVMessageBuilder.create()
				.command(KVMessage.Command.COPY)
				.dataType(where)
				.range(range)
				.sendAndRespond(socketChannel);
		HashMap<String, String> data = JSON.parseObject(response, new TypeReference<HashMap<String, String>>() {});
		return data;
	}

	public String get(String key) throws Exception {
		if (isResponsible(key)){
			return mainDatabase.get(key);
		}
		return "";
	}

	public StatusCode put(String key, String value) throws Exception {
		StatusCode code = StatusCode.SERVER_ERROR;
		try {
			if (isResponsible(key)){
				code = mainDatabase.hasKey(key) ? StatusCode.put_update : StatusCode.put_success;
				mainDatabase.put(key, value);
				System.out.println("Put data on database " + this.port + " <" + key + ":" + value + ">");

				// put data into the backup Node
				Node backUpNode = MetaData.INSTANCE.getBackupNodeByKey(key);
				KVMessageBuilder.create()
						.command(KVMessage.Command.PUT)
						.key(key)
						.value(value)
						.dataType(DataType.BACKUP)
						.sendAndRespond(backUpNode.getSocketChannel());
			}
		}
		finally {
			return code;
		}

	}

	public void putBackup(String key, String value) throws Exception {
		backupDatabase.put(key, value);
		System.out.println("Put backup data on database " + this.port + " <" + key + ":" + value + ">");
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
				System.out.println("Delete data on database " + this.port + ": " + key );

				// delete data from backup node
				KVMessageBuilder.create()
						.command(KVMessage.Command.DELETE)
						.key(key)
						.dataType(DataType.BACKUP)
						.sendAndRespond(MetaData.INSTANCE.getBackupNodeByKey(key).getSocketChannel());
			}
		}
		finally {
			return code;
		}
	}

	public boolean hasKey(String key) throws Exception {
		return mainDatabase.hasKey(key);
	}

	// init, recover, updateRing, deleteExpiredData will only be called by ECS
	public StatusCode init() {
		try{
			System.out.println("Start Data Transfer");
			if (MetaData.INSTANCE.getRing().size() != 1) {
				Node nextNode = MetaData.INSTANCE.getNextNode(this);
				Node previousNode = MetaData.INSTANCE.getPreviousNode(this);
				if (!nextNode.equals(this)) {
					HashMap<String, String> mainData = nextNode.copy(DataType.DATA, getRange(DataType.DATA));
					mainDatabase.saveAllData(mainData);
				}
				if (!previousNode.equals(this)) {
					HashMap<String, String> backup = previousNode.copy(DataType.BACKUP, getRange(DataType.BACKUP));
					backupDatabase.saveAllData(backup);
				}
			}
			System.out.println("Start KVServer: " + this.host + ":" + this.port);
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
			if (MD5Hash.hash(previousNode.getHost() + ":" + previousNode.getPort()).equals(removedHash)) {
				Node newPreviousNode = MetaData.INSTANCE.getPreviousNode(removedNode);
				Range dataRangeOfRemovedNode = new Range(MetaData.INSTANCE.getHash(newPreviousNode), removedHash);
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
				if(!newPreviousNode.equals(this)) {
					mainDatabase.saveAllData(newPreviousNode.copy(DataType.BACKUP, dataRangeOfRemovedNode));
				}
			}

			// recover backup from the removed node
			// If the removed node is the next node of this node
			Node nextNode = MetaData.INSTANCE.getNextNode(this);
			if (MD5Hash.hash(nextNode.getHost() + ":" + nextNode.getPort()).equals(removedHash)) {
				Node newNextNode = MetaData.INSTANCE.getNextNode(removedNode);
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
