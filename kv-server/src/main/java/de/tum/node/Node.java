package de.tum.node;

import java.io.Serializable;
import java.util.SortedMap;

public class Node implements Serializable {

	private String host;
	private int port;

	public Node(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public String getHost() {
		return host;
	}

	/**
	 * Get the range of this node in the ring
	 * @return String range
	 */
	public String getRange() {
		String hash = ConsistentHash.INSTANCE.getKey(this).toString();
		Node nextNode = ConsistentHash.INSTANCE.getNextNode(this);
		String nextHash = ConsistentHash.INSTANCE.getKey(nextNode).toString();
		return hash + " - " + nextHash;
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
	//TODO: old code doesnt feel right
	public boolean isResponsible(String key) throws NullPointerException {
		String currHash = ConsistentHash.INSTANCE.getKey(this).toString();
		//Node nextNode = ConsistentHash.INSTANCE.getNextNode(this);
		//String nextHash = ConsistentHash.INSTANCE.getKey(nextNode).toString();
		//return hash.compareTo(nextHash) < 0;

		String keyHash = MD5Hash.hash(key);
		Node prevNode = ConsistentHash.INSTANCE.getPreviousNode(this);
		String prevHash = ConsistentHash.INSTANCE.getKey(prevNode).toString();
		return (currHash.compareTo(prevHash) > 0 && keyHash.compareTo(currHash) <= 0);
	}

	// will be called when this node joins the ring
	public void init() {
		Node nextNode = ConsistentHash.INSTANCE.getNextNode(this);
		Node previousNode = ConsistentHash.INSTANCE.getPreviousNode(this);
		String data = nextNode.transfer(Database.DATA, getRange());
		String backup = previousNode.transfer(Database.BACKUP, getRange());
	}

	// will be called when a node leaves the ring
	public void recover(Node isReplicaOf, Node myReplica, String newRange) {
		String backup = isReplicaOf.copy(Database.DATA, isReplicaOf.getRange());
		String data = myReplica.copy(Database.BACKUP, newRange);
	}

	/**TODO grpc
	 * TODO 能不能做先copy，等esc发消息了再把这部分的range删除
	 * Transfer data from this node to another node, and delete the data from this
	 *
	 * @param where
	 * @param range
	 * @return the data that has been transferred
	 */
	public String transfer(Database where, String range) {
		String data = copy(where, range);
		String rangeToBeDeleted = range;
		return "Transfer:" + range;
	}

	/**TODO grpc
	 * Copy data from another node to this node
	 *
	 * @param where
	 * @param range
	 * @return the data that has been copied
	 */
	public String copy(Database where, String range) {
		return "Copy:" + range;
	}

	public enum Database {
		DATA, BACKUP
	}
}
