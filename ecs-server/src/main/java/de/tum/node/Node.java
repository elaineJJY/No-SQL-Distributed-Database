package de.tum.node;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.SortedMap;

public interface Node {

	int getPort();

	String getHost();

	long heartbeat();

	Range getRange(DataType dataType);

	boolean equals(Object obj);

	String toString();

	boolean isResponsible(String key) throws NullPointerException;

	void init() throws Exception;

	void recover(Node removedNode) throws Exception;

	void updateRing(SortedMap<String, Node> ring);

	void deleteExpiredData(DataType dataType, Range range) throws Exception;

	HashMap<String, Object> copy(DataType where, Range range) throws Exception;
}
