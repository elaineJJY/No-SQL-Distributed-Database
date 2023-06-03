package de.tum.node;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
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


	/** TODO: For K-V Store only
	 * Get the node that the key belongs to
	 * @param key data key
	 * @return node
	 */
	public Node getNode(String key) {
		if (ring.isEmpty()) {
			return null;
		}

		String keyHash = MD5Hash.hash(key);
		if (!ring.containsKey(keyHash)) {
			SortedMap<String, Node> tailMap = ring.tailMap(keyHash);
			keyHash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
		}
		return ring.get(keyHash);
	}

	public void addNode(Node node) {
		String nodeHash = MD5Hash.hash(node.toString());
		ring.put(nodeHash, node);
	}

	public void removeNode(Node node) {
		String nodeHash = MD5Hash.hash(node.toString());
		ring.remove(nodeHash);
	}

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
