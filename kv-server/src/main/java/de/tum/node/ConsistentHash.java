package de.tum.node;

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
	 * ring: <hash, INode>
	 */
	private SortedMap<String, INode> ring = new TreeMap<>();

	public SortedMap<String, INode> getRing() { return ring; }

	/**
	 * Get the node hash corresponding to the node
	 * @param node
	 * @return hash value
	 */
	public String getHash(INode node) {
		String nodeHash = MD5Hash.hash(node.toString()); // hash value of the node, key is string <ip:port>
		int i = 1;
		while (!ring.get(nodeHash).equals(node)) {
			nodeHash = MD5Hash.hash(nodeHash + String.valueOf(i++));
		}
		return nodeHash;
	}

	/**
	 * Get the next node given the current node
	 * @param node
	 * @return next node
	 */
	public INode getNextNode(INode node){
		String nodeHash = getHash(node);
		// tailMap: returns a view of the portion of this map whose keys are greater than or equal to fromKey
		SortedMap<String, INode> tailMap = ring.tailMap(nodeHash);
		String nextHash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
		return ring.get(nextHash);
	}

	/**
	 * Get the previous node given the current node
	 * @param node
	 * @return previous node
	 */
	public INode getPreviousNode(INode node){
		String nodeHash = getHash(node);
		SortedMap<String, INode> headMap = ring.headMap(nodeHash);
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
			INode node = ring.get(hash);
			//  <kr-from>, <kr-to>, <ip:port>
			text += node.getRange(DataType.DATA) + ", " + node.getHost() + ":" + node.getPort() + "\n";
		}
		return text;
	}

	public void update(SortedMap<String, INode> ring) {
		this.ring = ring;
	}

	public INode getResponsibleServerByKey(String key) {
		String hash = MD5Hash.hash(key);
		SortedMap<String, INode> tailMap = ring.tailMap(hash);
		if (tailMap.isEmpty()) {
			return ring.get(ring.firstKey());
		}
		return tailMap.get(tailMap.firstKey());
	}
}
