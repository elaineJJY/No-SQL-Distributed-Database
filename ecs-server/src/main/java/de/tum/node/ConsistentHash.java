package de.tum.node;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
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
	private SortedMap<String, Node> ring = new TreeMap<>();

	public SortedMap<String, Node> getRing() { return ring; }

	public void addNode(Node node) throws Exception {
		String nodeHash = getHash(node);
		// in case of hash collision
//		if (ring.containsKey(nodeHash)) {
//			int i = 1;
//			while (ring.containsKey(nodeHash)) {
//				nodeHash = MD5Hash.hash(node.toString() + String.valueOf(i++));
//			}
//		}
		ring.put(nodeHash, node);
		boolean updateRingSuccess = node.updateRing(ring);
		if (!updateRingSuccess) {
			ring.remove(nodeHash);
			throw new Exception("Update MetaData of KVServer<" + node.getHost() + "> failed");
		}

		boolean nodeInitSuccess = node.init();
		if (!nodeInitSuccess) {
			ring.remove(nodeHash);
			throw new Exception("Initialization Node of KVServer<" + node.getHost() + "> failed");
		}

		updateRingForAllNodes(node);
		if (ring.size() > 1) {
			getPreviousNode(node).deleteExpiredData(DataType.BACKUP, node.getRange(DataType.BACKUP));
			getNextNode(node).deleteExpiredData(DataType.DATA, node.getRange(DataType.DATA));
		}

//		if (node.init()) {
//			updateRingForAllNodes(node);
//			if (ring.size() > 1) {
//				getPreviousNode(node).deleteExpiredData(DataType.BACKUP, node.getRange(DataType.BACKUP));
//				getNextNode(node).deleteExpiredData(DataType.DATA, node.getRange(DataType.DATA));
//			}
//		}
//		else {
//			System.out.println("Initialization of KVServer<" + node.getHost() + "> failed");
//			ring.remove(nodeHash);
//		}
	}

	public void removeNode(Node node) throws Exception {

		// if node still alive, set node to read-only
		try {
			//TODO
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
		updateRingForAllNodes(node);
	}


	public String getHash(Node node) {
		String nodeHash = MD5Hash.hash(node.toString()); // hash value of the node, key is string <ip:port>
//		int i = 1;
//		while (!ring.get(nodeHash).equals(node)) {
//			nodeHash = MD5Hash.hash(nodeHash + String.valueOf(i++));
//		}
		return nodeHash;
	}

	public Node getNextNode(Node node) {
		String nodeHash = getHash(node);
		// tailMap: returns a view of the portion of this map whose keys are greater than or equal to fromKey
		SortedMap<String, Node> tailMap = ring.tailMap(nodeHash + 1);
		// The node we need is next node of the first node in the tailMap
		String nextHash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
		return ring.get(nextHash);
	}

	/**
	 * Get the previous node given the current node
	 * @param node
	 * @return previous node
	 */
	public Node getPreviousNode(Node node){
		String nodeHash = getHash(node);
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
			text += node.getRange(DataType.DATA) + ", " + node.getHost() + ":" + node.getPort() + "\n";
		}
		return text;
	}

	private void updateRingForAllNodes(Node except) throws Exception {
		for (Node node : ring.values()) {
			if (node.equals(except)) {
				continue;
			}
			node.updateRing(ring);
		}
	}
}
