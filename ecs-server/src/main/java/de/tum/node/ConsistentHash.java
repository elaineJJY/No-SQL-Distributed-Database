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
	private SortedMap<String, NodeProxy> ring = new TreeMap<>();  // <hash, node>

	public SortedMap<String, NodeProxy> getRing() {
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

	public void removeNode(NodeProxy nodeProxy) {
		String nodeHash = getHash(nodeProxy);
		if (ring.size() == 1) {
			ring.remove(nodeHash);
			nodeProxy.closeRpcChannel();
		}
		else {
			NodeProxy previousNodeProxy = getPreviousNode(nodeProxy);
			NodeProxy nextNodeProxy = getNextNode(nodeProxy);
			ring.remove(nodeHash);
			//nodeProxy.closeRpcChannel();
			System.out.println("Channel of ECS to KVServer<"
				+ nodeProxy.getHost() + ":" + nodeProxy.getPortForClient() + "> is closed");
			previousNodeProxy.recover(nodeProxy);
			nextNodeProxy.recover(nodeProxy);
			updateRingForAllNodes(nodeProxy);
		}
	}

	/**
	 * Get the node hash corresponding to the node
	 *
	 * @param nodeProxy
	 * @return hash value
	 */
	public String getHash(NodeProxy nodeProxy) {
		return MD5Hash.hash(nodeProxy.getHost() + ":" + nodeProxy.getPortForClient());
	}

	/**
	 * Get the next node given the current node
	 *
	 * @param nodeProxy
	 * @return next node
	 */
	public NodeProxy getNextNode(NodeProxy nodeProxy) {
		String nodeHash = getHash(nodeProxy);
		// tailMap: returns a view of the portion of this map whose keys are greater than or equal to fromKey
		SortedMap<String, NodeProxy> tailMap = ring.tailMap(nodeHash + 1);
		tailMap.remove(nodeHash);
		String nextHash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
		return ring.get(nextHash);
	}

	/**
	 * Get the previous node given the current node
	 *
	 * @param nodeProxy
	 * @return previous node
	 */
	public NodeProxy getPreviousNode(NodeProxy nodeProxy) {
		String nodeHash = getHash(nodeProxy);
		SortedMap<String, NodeProxy> headMap = ring.headMap(nodeHash);
		String previousHash = headMap.isEmpty() ? ring.lastKey() : headMap.lastKey();
		return ring.get(previousHash);
	}

	/**
	 * Update ring for all nodes
	 *
	 * @param except: node that should not be updated
	 */
	private void updateRingForAllNodes(NodeProxy except) {
		for (NodeProxy nodeProxy : ring.values()) {
			if (nodeProxy.equals(except)) {
				continue;
			}
			System.out.println(ring);
			nodeProxy.updateRing(ring);
		}
	}
}
