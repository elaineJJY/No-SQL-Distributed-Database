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

}
