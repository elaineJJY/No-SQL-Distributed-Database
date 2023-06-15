package de.tum.node;

import de.tum.common.KVMessageBuilder;

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
	public void setLocalNode(Node node) {
		this.localNode = node;
	}

	public Node getLocalNode() {
		return this.localNode;
	}
	public SortedMap<String, Node> getRing() {
		return ring;
	}

	public SocketChannel createSocket(String host, int port) throws Exception {
		SocketChannel socketChannel =  SocketChannel.open(new InetSocketAddress(host, port));
		KVMessageBuilder.create().socketChannel(socketChannel).receive();// handle the "Hello Client" message
		return socketChannel;
	}

	public String getHash(Node node) {
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
			text += node.getRange(DataType.DATA).toString() + "," + node.getHost() + ":" + node.getPort() + ";";
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
				SocketChannel socketChannel = null;
				if (!address.equals(localNode.getHost() + ":" + localNode.getPort())) {
					socketChannel = createSocket(host, port);
				}
				Node node = new Node(host, port, socketChannel);
				ring.put(getHash(node), node);
			}
		} else {
			for (String address : addrAndHash.keySet()) {
				for (String addrOfNodesInOldRing : ring.keySet()) {
					if (ring.get(addrOfNodesInOldRing).toString().equals(address)) {
						continue;
					}
					String[] hostPort = address.split(":");
					String host = hostPort[0];
					int port = Integer.parseInt(hostPort[1]);
					// create socket channel for each node
					SocketChannel socketChannel = null;
					if (!address.equals(localNode.getHost() + ":" + localNode.getPort())) {
						socketChannel = createSocket(host, port);
					}
					Node node = new Node(host, port, socketChannel);
					ring.put(getHash(node), node);
				}
			}
		}
	}

	public Node getResponsibleNodeByKey(String key) {
		String hash = MD5Hash.hash(key);
		SortedMap<String, Node> tailMap = ring.tailMap(hash);
		if (tailMap.isEmpty()) {
			return ring.get(ring.firstKey());
		}
		return tailMap.get(tailMap.firstKey());
	}

	public Node getBackupNodeByKey(String key) {
		return getPreviousNode(getResponsibleNodeByKey(key));
	}
}
