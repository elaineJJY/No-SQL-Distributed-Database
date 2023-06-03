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

	public Node getNextNode(Node node) {
		String nodeHash = MD5Hash.hash(node.toString());
		SortedMap<String, Node> tailMap = ring.tailMap(nodeHash);
		String nextHash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
		return ring.get(nextHash);
	}

	public Node getPreviousNode(Node node) {
		String nodeHash = MD5Hash.hash(node.toString());
		SortedMap<String, Node> headMap = ring.headMap(nodeHash);
		String previousHash = headMap.isEmpty() ? ring.lastKey() : headMap.lastKey();
		return ring.get(previousHash);
	}


}
