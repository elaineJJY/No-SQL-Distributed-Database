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

	public int getPort() {
		return port;
	}

	public String getHost() {
		return host;
	}

	public long heartbeat() {
		return new Date().getTime();
	}

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
	public boolean isResponsible(String key) throws NullPointerException {
		String keyHash = ConsistentHash.INSTANCE.getHash(this);
		Node prevNode = ConsistentHash.INSTANCE.getPreviousNode(this);
		String prevHash = ConsistentHash.INSTANCE.getHash(prevNode);
		return (keyHash.compareTo(key) >= 0 && prevHash.compareTo(key) < 0);
	}

	public void init() throws Exception {
		Node nextNode = ConsistentHash.INSTANCE.getNextNode(this);
		Node previousNode = ConsistentHash.INSTANCE.getPreviousNode(this);
		mainDatabase.saveAllData(nextNode.copy(DataType.DATA, getRange(DataType.DATA)));
		backupDatabase.saveAllData(previousNode.copy(DataType.BACKUP, getRange(DataType.BACKUP)));
		server = new KVServer(this);
	}


	public void recover(Node removedNode) throws Exception {

		String removedHash = ConsistentHash.INSTANCE.getHash(removedNode);

		// recover data from the removed node
		if (ConsistentHash.INSTANCE.getPreviousNode(this).equals(removedNode)) {
			Node newPreviousNode = ConsistentHash.INSTANCE.getPreviousNode(removedNode);
			Range dataRangeOfRemovedNode = new Range(ConsistentHash.INSTANCE.getHash(newPreviousNode), removedHash);
			try {
				removedNode.heartbeat(); // check whether the removed node is alive
				mainDatabase.saveAllData(newPreviousNode.copy(DataType.DATA, dataRangeOfRemovedNode));
			}
			catch (Exception e) {
				System.out.println("Node " + removedNode + " is dead");
				// recover data from the backup
				mainDatabase.saveAllData(newPreviousNode.copy(DataType.BACKUP, dataRangeOfRemovedNode));
			}
		}
		// recover backup from the removed node
		if (ConsistentHash.INSTANCE.getNextNode(this).equals(removedNode)) {
			Node newNextNode = ConsistentHash.INSTANCE.getNextNode(removedNode);
			Range backupRangeOfRemovedNode = new Range(removedHash, ConsistentHash.INSTANCE.getHash(newNextNode));
			try {
				removedNode.heartbeat(); // check whether the removed node is alive
				backupDatabase.saveAllData(newNextNode.copy(DataType.BACKUP, backupRangeOfRemovedNode));
			}
			catch (Exception e) {
				System.out.println("Node " + removedNode + " is dead");
				// recover data from the backup
				backupDatabase.saveAllData(newNextNode.copy(DataType.DATA, backupRangeOfRemovedNode));
			}
		}
	}

	public void updateRing(SortedMap<String, Node> ring) {
		ConsistentHash.INSTANCE.update(ring);
	}

	public void deleteExpiredData(DataType dataType, Range range) throws Exception {
		IDatabase database = dataType == DataType.DATA ? mainDatabase : backupDatabase;
		database.deleteDataByRange(range);
	}

	/**
	 * Get data from another node to this node
	 *
	 * @param where
	 * @param range
	 * @return the data that in this range
	 */
	public HashMap<String, Object> copy(DataType where, Range range) throws Exception {
		IDatabase database = where == DataType.DATA ? mainDatabase : backupDatabase;
		return database.getDataByRange(range);
	}

	public Object get(String key) throws Exception {
		return mainDatabase.get(key);
	}
	public void put(String key, String value) throws Exception {
		mainDatabase.put(key, value);
	}
	public void delete(String key) throws Exception {
		mainDatabase.delete(key);
	}
}
