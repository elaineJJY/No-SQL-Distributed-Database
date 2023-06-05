package de.tum.node;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

	// TODO: initialization of whole hash ring
	//public void initRing()


	private void updateRingForAllNodes() {
		for (Node node : ring.values()) {
			node.updateRing(ring);
		}
	}

	public void addNode(Node node) {
		String nodeHash = MD5Hash.hash(node.toString());
		if (ring.containsKey(nodeHash)) {
			int i = 1;
			while (ring.containsKey(nodeHash)) {
				nodeHash = MD5Hash.hash(node.toString() + String.valueOf(i++));
			}
		}
		node.updateRing(ring);
		node.init();
		ring.put(nodeHash, node);
		updateRingForAllNodes();
	}

	public void removeNode(Node node) {
		String nodeHash = MD5Hash.hash(node.toString());
		getPreviousNode(node).recover();
		getNextNode(node).recover();
		ring.remove(nodeHash);
		updateRingForAllNodes();
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
