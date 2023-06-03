package de.tum.node;

import java.io.Serializable;

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

	public String getRange() {
		String hash = MD5Hash.hash(this.toString());
		Node nextNode = ConsistentHash.INSTANCE.getNextNode(this);
		String nextHash = MD5Hash.hash(nextNode.toString());
		return hash + " - " + nextHash;
	}

	@Override
	public String toString() {
		return host + ":" + port;
	}

	public Node getReplica() {
		return ConsistentHash.INSTANCE.getNextNode(this);
	}

	public Node isReplicaOf() {
		return ConsistentHash.INSTANCE.getPreviousNode(this);
	}

	/**
	 * Check if this node is responsible for the given (key, value) pair
	 * @param key
	 * @return
	 */
	public boolean isResponsible(String key) {
		String hash = MD5Hash.hash(key);
		Node nextNode = ConsistentHash.INSTANCE.getNextNode(this);
		String nextHash = MD5Hash.hash(nextNode.toString());
		return hash.compareTo(nextHash) < 0;
	}
}
