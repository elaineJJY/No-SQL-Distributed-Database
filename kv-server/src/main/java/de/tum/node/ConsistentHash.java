package de.tum.node;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * ClassName: ConsistentHash Package: de.tum.node Description:
 *
 * @Author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @Create 2023/6/2 23:37
 * @Version 1.0
 */
public enum ConsistentHash {
	INSTANCE;
	private SortedMap<String, Node> ring = new TreeMap<>();  // <hash, node>

	//TODO: gRPC
	// initialization of currentRing
	public void updateRing(SortedMap<String, Node> ring) {
		this.ring = ring;
	}


	public Node getKey(Node node) {
		String nodeHash = MD5Hash.hash(node.toString());
		int i = 1;
		while (!ring.get(nodeHash).equals(node)) {
			nodeHash = MD5Hash.hash(nodeHash + String.valueOf(i++));
		}
		return ring.get(nodeHash);
	}

	public Node getNextNode(Node node){
		String nodeHash = MD5Hash.hash(node.toString());
		SortedMap<String, Node> tailMap = ring.tailMap(nodeHash);
		String nextHash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
		return ring.get(nextHash);
	}

	public Node getPreviousNode(Node node){
		String nodeHash = MD5Hash.hash(node.toString());
		SortedMap<String, Node> headMap = ring.headMap(nodeHash);
		String previousHash = headMap.isEmpty() ? ring.lastKey() : headMap.lastKey();
		return ring.get(previousHash);
	}

	@Override
	public String toString() {
		// print each node and its corresponding range, ip and port
		 // for each node: <kr-from>, <kr-to>, <ip:port>
		 String text = "";
		 for (String hash : ring.keySet()) {
		 	Node node = ring.get(hash);
			 //  <kr-from>, <kr-to>, <ip:port>
		 	text += node.getRange() + ", " + node.getHost() + ":" + node.getPort() + "\n";
		 }
		return text;
	}
}
