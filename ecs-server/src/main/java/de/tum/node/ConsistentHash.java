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
	private HashMap<Node, SocketChannel> serverSockets = new HashMap<>();

	public SortedMap<String, Node> getRing() {
		return ring;
	}

	public void addNode(NodeProxy nodeProxy) throws io.grpc.StatusRuntimeException {
		String nodeHash = getHash(nodeProxy);
		// in case of hash collision
		if (ring.containsKey(nodeHash)) {
			int i = 1;
			while (ring.containsKey(nodeHash)) {
				nodeHash = MD5Hash.hash(nodeProxy.toString() + String.valueOf(i++));
			}
		}
		ring.put(nodeHash, nodeProxy);
		nodeProxy.updateRing(ring);
		nodeProxy.init();
		updateRingForAllNodes(nodeProxy);
		if (ring.size() > 1) {
			getPreviousNode(nodeProxy).deleteExpiredData(DataType.BACKUP, nodeProxy.getRange(DataType.BACKUP));
			getNextNode(nodeProxy).deleteExpiredData(DataType.DATA, nodeProxy.getRange(DataType.DATA));
		}
		nodeProxy.startKVServer();
	}

	public SocketChannel createSocketForNode(Node node) throws Exception {
		return SocketChannel.open(new InetSocketAddress(node.getHost(), node.getPort()));
	}

	public SocketChannel getSocketForNode(Node node) {
		return serverSockets.get(node);
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

	public void update(SortedMap<String, Node> newRing) throws Exception {
		// update the socket channel for each node
		if (serverSockets.isEmpty()) {
			for (Node node: newRing.values()) {
				SocketChannel socketChannel = createSocketForNode(node);
				serverSockets.put(node, socketChannel);
			}
		} else {
			for (Node node: serverSockets.keySet()) {
				for (Node nodeInNewRing: newRing.values(
				)) {
					if (!node.equals(nodeInNewRing)) {
						serverSockets.remove(node);
						SocketChannel socketChannel = createSocketForNode(nodeInNewRing);
						serverSockets.put(nodeInNewRing, socketChannel);
					}
				}
			}
		}
		// update the ring
		this.ring = newRing;
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

	public SocketChannel getServerSocketByNode(Node node) {
		return serverSockets.get(node);
	}

}
