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

	public SortedMap<String, Node> getRing() {
		return ring;
	}

	public void addNode(Node node) throws Exception {
		String nodeHash = MD5Hash.hash(node.toString());
		// in case of hash collision
		if (ring.containsKey(nodeHash)) {
			int i = 1;
			while (ring.containsKey(nodeHash)) {
				nodeHash = MD5Hash.hash(node.toString() + String.valueOf(i++));
			}
		}
		ring.put(nodeHash, node); // add node to the ring //
		// TODO: gRPC
		node.updateRing(ring);
		node.init(); // initialize node in the KV_Server
		updateRingForAllNodes();
		getPreviousNode(node).deleteExpiredData(DataType.DATA, node.getRange(DataType.DATA));
		getNextNode(node).deleteExpiredData(DataType.BACKUP, node.getRange(DataType.BACKUP));
	}

	public void removeNode(Node node) throws Exception {

		// if node still alive, set node to read-only
		try {
			// node.setReadOnly();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String nodeHash = getHash(node);
		Node previousNode = getPreviousNode(node);
		Node nextNode = getNextNode(node);
		ring.remove(nodeHash);

		previousNode.recover(node);
		nextNode.recover(node);
		updateRingForAllNodes();
	}

	/**
	 * Get the node hash corresponding to the node
	 *
	 * @param node
	 * @return hash value
	 */
	public String getHash(Node node) {
		String nodeHash = MD5Hash.hash(
				node.toString()); // hash value of the node, key is string <ip:port>
		int i = 1;
		while (!ring.get(nodeHash).equals(node)) {
			nodeHash = MD5Hash.hash(nodeHash + String.valueOf(i++));
		}
		return nodeHash;
	}

	/**
	 * Get the next node given the current node
	 *
	 * @param node
	 * @return next node
	 */
	public Node getNextNode(Node node) {
		String nodeHash = getHash(node);
		// tailMap: returns a view of the portion of this map whose keys are greater than or equal to fromKey
		SortedMap<String, Node> tailMap = ring.tailMap(nodeHash);
		String nextHash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
		return ring.get(nextHash);
	}

	/**
	 * Get the previous node given the current node
	 *
	 * @param node
	 * @return previous node
	 */
	public Node getPreviousNode(Node node) {
		String nodeHash = getHash(node);
		SortedMap<String, Node> headMap = ring.headMap(nodeHash);
		String previousHash = headMap.isEmpty() ? ring.lastKey() : headMap.lastKey();
		return ring.get(previousHash);
	}

	private void updateRingForAllNodes() {
		for (Node node : ring.values()) {
			node.updateRing(ring);
		}
	}
}
