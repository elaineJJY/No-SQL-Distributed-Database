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





	//TODO: gRPC
	public void init() {

	}

	//TODO: gRPC
	public void recover() {

	}

	public void updateRing(SortedMap<String, Node> ring) {

	}
}
