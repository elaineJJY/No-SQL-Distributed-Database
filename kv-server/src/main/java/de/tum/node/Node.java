package de.tum.node;

import de.tum.communication.Server;
import de.tum.database.BackupDatabase;
import de.tum.database.IDatabase;
import de.tum.database.MainDatabase;
import java.io.Serializable;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.SortedMap;

public class Node implements Serializable {

	private String host;
	private int port;

	private Server server;


	public Node(String host, int port, Server server) {
		this.host = host;
		this.port = port;
		this.server = server;
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

	// will be called when this node joins the ring
	public void init() {
		Node nextNode = ConsistentHash.INSTANCE.getNextNode(this);
		Node previousNode = ConsistentHash.INSTANCE.getPreviousNode(this);
		String data = nextNode.copy(DataType.DATA, getRange(DataType.DATA));
		String backup = previousNode.copy(DataType.BACKUP, getRange(DataType.BACKUP));
	}

	// will be called when a node leaves the ring
	public void recover(Node removedNode) {

		String removedHash = ConsistentHash.INSTANCE.getHash(removedNode);

		// recover data from the removed node
		if (ConsistentHash.INSTANCE.getPreviousNode(this).equals(removedNode)) {
			Node newPreviousNode = ConsistentHash.INSTANCE.getPreviousNode(removedNode);
			Range dataRangeOfRemovedNode = new Range(ConsistentHash.INSTANCE.getHash(newPreviousNode), removedHash);
			try {
				removedNode.heartbeat(); // check whether the removed node is alive
				server.mainDatabase.saveAllData(newPreviousNode.copy(DataType.DATA, dataRangeOfRemovedNode));
			}
			catch (Exception e) {
				System.out.println("Node " + removedNode + " is dead");
				server.mainDatabase.saveAllData(newPreviousNode.copy(DataType.BACKUP, dataRangeOfRemovedNode));
			}
		}
		// recover backup from the removed node
		if (ConsistentHash.INSTANCE.getNextNode(this).equals(removedNode)) {
			Node newNextNode = ConsistentHash.INSTANCE.getNextNode(removedNode);
			Range backupRangeOfRemovedNode = new Range(removedHash, ConsistentHash.INSTANCE.getHash(newNextNode));
			try {
				removedNode.heartbeat(); // check whether the removed node is alive
				server.backupDatabase.saveAllData(newNextNode.copy(DataType.BACKUP, backupRangeOfRemovedNode));
			}
			catch (Exception e) {
				System.out.println("Node " + removedNode + " is dead");
				server.backupDatabase.saveAllData(newNextNode.copy(DataType.DATA, backupRangeOfRemovedNode));
			}
		}

	}

	public void deleteExpiredData() {
		String dataRange = getRange(DataType.DATA);
		String backupRange = getRange(DataType.BACKUP);
		//TODO: Database.onlyKeep(dataRange, DataType.DATA);
		//TODO: Database.onlyKeep(backupRange, DataType.BACKUP);
	}


	/**TODO grpc
	 * Get data from another node to this node
	 *
	 * @param where
	 * @param range
	 * @return the data that in this range
	 */
	public Data copy(DataType where, Range range) {
		IDatabase database = where == DataType.DATA ? server.mainDatabase : server.backupDatabase;
		return database.getDataByRange(range);
	}

}
