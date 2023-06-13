package de.tum.node;

import java.net.InetSocketAddress;
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
public enum MetaData {
	INSTANCE;
	private SortedMap<String, Node> ring = new TreeMap<>(); // hash and node

	private Node localNode;
	private void setLocalNode(Node node) {
		this.localNode = node;
	}

	private Node getLocalNode() {
		return this.localNode;
	}
	public SortedMap<String, Node> getRing() {
		return ring;
	}

	public SocketChannel createSocket(String host, int port) throws Exception {
		return SocketChannel.open(new InetSocketAddress(host, port));
	}

	public String getHash(Node node) {
//		String nodeHash = MD5Hash.hash(node.toString()); // hash value of the node, key is string <ip:port>
////		int i = 1;
//		while (!ring.get(nodeHash).equals(node)) {
//			nodeHash = MD5Hash.hash(nodeHash + String.valueOf(i++));
//		}

		return MD5Hash.hash(node.getHost() + ":" + node.getPort());
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

	//newRing: <ip:port, hash>
	public void update(HashMap<String, String> addrAndHash) throws Exception {

		if (ring.isEmpty()) {
			for (String address : addrAndHash.keySet()) {
				String[] hostPort = address.split(":");
				String host = hostPort[0];
				int port = Integer.parseInt(hostPort[1]);
				// create socket channel for each node
				SocketChannel socketChannel = createSocket(host, port);

				Node node = new Node(host, port, socketChannel);
				ring.put(getHash(node), node);
			}
		}
		for (String address : addrAndHash.keySet()) {
			for (String addrOfNodesInOldRing : ring.keySet()) {
				if (address.equals(addrOfNodesInOldRing)) {
					continue;
				}
				String[] hostPort = address.split(":");
				String host = hostPort[0];
				int port = Integer.parseInt(hostPort[1]);
				// create socket channel for each node
				SocketChannel socketChannel = createSocket(host, port);

				Node node = new Node(host, port, socketChannel);
				ring.put(getHash(node), node);
			}
		}
	}

	public Node getResponsibleNodeByKey(String key) {
		String hash = MD5Hash.hash(key);
		SortedMap<String, Node> tailMap = ring.tailMap(hash);
		Node responsibleNode = tailMap.get(tailMap.firstKey());
		if (tailMap.isEmpty()) {
			responsibleNode = ring.get(ring.firstKey());
		}
		return responsibleNode;
	}

	public Node getBackupNodeByKey(String key) {
		return getPreviousNode(getResponsibleNodeByKey(key));
	}
}
