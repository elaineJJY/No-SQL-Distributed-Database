package de.tum.node;


import de.tum.communication.KVServer;
import de.tum.database.BackupDatabase;
import de.tum.database.IDatabase;
import de.tum.database.MainDatabase;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.SortedMap;

public class Node implements Serializable {

	private KVServer server;
	private String host;
	private int port;
	private IDatabase mainDatabase;
	private IDatabase backupDatabase;

	public Node(String host, int port, IDatabase mainDatabase, IDatabase backupDatabase){
		this.host = host;
		this.port = port;
		this.mainDatabase = mainDatabase;
		this.backupDatabase = backupDatabase;
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
			from = ConsistentHash.INSTANCE.getPreviousNode(this);
			to = this;
		}
		if (dataType == DataType.BACKUP) {
			from = this;
			to = ConsistentHash.INSTANCE.getNextNode(this);
		}
		if (from == null || to == null) {
			throw new NoSuchElementException("No such node");
		}
		return new Range(ConsistentHash.INSTANCE.getHash(from), ConsistentHash.INSTANCE.getHash(to));
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
	public String toString() {
		return host + ":" + port;
	}

	/**
	 * Check if this node is responsible for the given (key, value) pair
	 * @param key
	 * @return true if this node is responsible for the given (key, value) pair
	 */
	// isResponsible, copy, get, put, delete will only be called by other KVServer
	public boolean isResponsible(String key) throws NullPointerException {
		if (ConsistentHash.INSTANCE.getRing().size() == 1) {
			return true;
		}
		String keyHash = MD5Hash.hash(key);	//get hash of key

		String currNodeHash = ConsistentHash.INSTANCE.getHash(this);	//get hash of this node

		SortedMap<String, Node> tailMap = ConsistentHash.INSTANCE.getRing().tailMap(keyHash);
		if (tailMap.isEmpty()) {
			return ConsistentHash.INSTANCE.getRing().firstKey().equals(currNodeHash);
		} else {
			return tailMap.firstKey().equals(currNodeHash);
		}
	}

	/**
	 * Get data from another node to this node
	 *
	 * @param where
	 * @param range
	 * @return the data that in this range
	 */
	public HashMap<String, String> copy(DataType where, Range range) throws Exception {
		IDatabase database = where == DataType.DATA ? mainDatabase : backupDatabase;
		return database.getDataByRange(range);
	}

	public void get(String key) throws Exception {
		mainDatabase.get(key);
	}

	public void put(String key, String value) throws Exception {
		mainDatabase.put(key, value);
		System.out.println("Put data on database " + this.port + " <" + key + ":" + value + ">");
	}

	public void putBackup(String key, String value) throws Exception {
		backupDatabase.put(key, value);
		System.out.println("Put backup data on database " + this.port + " <" + key + ":" + value + ">");
	}

	public void delete(String key) throws Exception {
		mainDatabase.delete(key);
		System.out.println("Delete data on database " + this.port + ": " + key );
	}

	public boolean hasKey(String key) throws Exception {
		return mainDatabase.hasKey(key);
	}

	// init, recover, updateRing, deleteExpiredData will only be called by ECS
	public void init() throws Exception {
		// Data transfer
		System.out.println("Start Data Transfer");
		if (ConsistentHash.INSTANCE.getRing().size() != 1) {
			Node nextNode = ConsistentHash.INSTANCE.getNextNode(this);
			Node previousNode = ConsistentHash.INSTANCE.getPreviousNode(this);

			HashMap<String, String> mainData = nextNode.copy(DataType.DATA, getRange(DataType.DATA));
			mainDatabase.saveAllData(mainData);

			HashMap<String, String> backup = previousNode.copy(DataType.BACKUP, getRange(DataType.BACKUP));
			backupDatabase.saveAllData(backup);
//			mainDatabase.saveAllData(nextNode.copy(DataType.DATA, getRange(DataType.DATA)));
//			backupDatabase.saveAllData(previousNode.copy(DataType.BACKUP, getRange(DataType.BACKUP)));
		}
		// Start KVServer
		System.out.println("Start KVServer: " + this.host + ":" + this.port);
	}

	public void startKVServer() throws Exception {
		server = new KVServer(this);
		server.start(host, port);
	}

	// TODO: Exception
	public void recover(Node removedNode) throws Exception {

		String removedHash = ConsistentHash.INSTANCE.getHash(removedNode);

		// recover data from the removed node
		// If the removed node is the previous node of this node
		Node previousNode = ConsistentHash.INSTANCE.getPreviousNode(this);
		if (MD5Hash.hash(previousNode.getHost() + ":" + previousNode.getPort()).equals(removedHash)) {
			Node newPreviousNode = ConsistentHash.INSTANCE.getPreviousNode(removedNode);
			Range dataRangeOfRemovedNode = new Range(ConsistentHash.INSTANCE.getHash(newPreviousNode), removedHash);
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
			mainDatabase.saveAllData(newPreviousNode.copy(DataType.BACKUP, dataRangeOfRemovedNode));
		}

		// recover backup from the removed node
		// If the removed node is the next node of this node
		Node nextNode = ConsistentHash.INSTANCE.getNextNode(this);
		if (MD5Hash.hash(nextNode.getHost() + ":" + nextNode.getPort()).equals(removedHash)) {
			Node newNextNode = ConsistentHash.INSTANCE.getNextNode(removedNode);
			Range backupRangeOfRemovedNode = new Range(removedHash, ConsistentHash.INSTANCE.getHash(newNextNode));
//			try {
//				removedNode.heartbeat(); // check whether the removed node is alive
//				backupDatabase.saveAllData(newNextNode.copy(DataType.BACKUP, backupRangeOfRemovedNode));
//			}
//			catch (Exception e) {
//				System.out.println("Node " + removedNode + " is dead");
//				// recover data from the backup
//				backupDatabase.saveAllData(newNextNode.copy(DataType.DATA, backupRangeOfRemovedNode));
//			}
			backupDatabase.saveAllData(newNextNode.copy(DataType.DATA, backupRangeOfRemovedNode));
		}
	}

	public void updateRing(SortedMap<String, Node> ring) {
		ConsistentHash.INSTANCE.update(ring);
	}

	public void deleteExpiredData(DataType dataType, Range range) throws Exception {
		IDatabase database = dataType == DataType.DATA ? mainDatabase : backupDatabase;
		database.deleteDataByRange(range);
	}
}
