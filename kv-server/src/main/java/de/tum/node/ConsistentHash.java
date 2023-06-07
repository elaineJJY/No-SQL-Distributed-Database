package de.tum.node;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * ClassName: ConsistentHash
 * Package: de.tum.node
 * Description: Provides a consistent hash ring for the nodes
 *
 * @Author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @Create 2023/6/2 23:37
 * @Version 1.0
 */
public enum ConsistentHash {
	INSTANCE;
	/**
	 * ring: <hash, node>
	 */
	private SortedMap<String, Node> ring = new TreeMap<>();

	//TODO: gRPC
	// initialization of currentRing
	public void updateRing(SortedMap<String, Node> ring) {
		this.ring = ring;
	}


	/**
	 * Get the node hash corresponding to the key
	 * @param node
	 * @return hash value
	 */
	public Node getKey(Node node) {
		String nodeHash = MD5Hash.hash(node.toString()); // hash value of the node, key is string <ip:port>
		int i = 1;
		while (!ring.get(nodeHash).equals(node)) {
			nodeHash = MD5Hash.hash(nodeHash + String.valueOf(i++));
		}
		return ring.get(nodeHash);
	}

	/**
	 * Get the next node given the current node
	 * @param node
	 * @return next node
	 */
	public Node getNextNode(Node node){
		String nodeHash = MD5Hash.hash(node.toString());
		// tailMap: returns a view of the portion of this map whose keys are greater than or equal to fromKey
		SortedMap<String, Node> tailMap = ring.tailMap(nodeHash);
		String nextHash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
		return ring.get(nextHash);
	}

	//TODO: poosible alternative using iterator?
	// No Iterator calculaiton in Java, not to be confirmed


	/**
	 * Get the previous node given the current node
	 * @param node
	 * @return previous node
	 */
	public Node getPreviousNode(Node node){
		String nodeHash = MD5Hash.hash(node.toString());
		SortedMap<String, Node> headMap = ring.headMap(nodeHash);
		String previousHash = headMap.isEmpty() ? ring.lastKey() : headMap.lastKey();
		return ring.get(previousHash);
	}

	/**
	 * print each node and its corresponding range, ip and port
	 * @return String <kr-from>, <kr-to>, <ip:port>
	 */
	@Override
	public String toString() {
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