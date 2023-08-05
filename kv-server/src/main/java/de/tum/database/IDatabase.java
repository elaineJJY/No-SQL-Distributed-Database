package de.tum.database;

import de.tum.node.Range;
import java.util.HashMap;

public interface IDatabase {

	void put(String key, String value) throws Exception;

	String get(String key) throws Exception;

	void delete(String key) throws Exception;

	void put(String key, String value, String transactionId) throws Exception;

	String get(String key, String transactionId) throws Exception;

	void delete(String key, String transactionId) throws Exception;

	HashMap<String, String> getDataByRange(Range range) throws Exception;

	HashMap<String, String> getAllData() throws Exception;

	void saveAllData(HashMap<String, String> map) throws Exception;// Map<K, V> = HashMap<String, Object>

	void deleteDataByRange(Range range) throws Exception;
	boolean hasKey(String key) throws Exception;

	void lock(String key) throws Exception;
	void unlock(String key) throws Exception;
}