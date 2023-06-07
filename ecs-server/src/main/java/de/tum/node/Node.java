package de.tum.node;

import java.io.Serializable;
import java.util.SortedMap;

public interface Node {
	public long heartbeat();
	public String toString();
	public void init();
	public void deleteExpiredData();
	public void recover(Node removedNode);
	public void updateRing(SortedMap<String, Node> ring);
}
