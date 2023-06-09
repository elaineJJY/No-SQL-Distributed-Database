package de.tum.database;

import de.tum.node.Range;
import java.util.HashMap;

public interface IDatabase {

	void put(String key, String value) throws Exception;

	String get(String key) throws Exception;

	void delete(String key) throws Exception;

	HashMap<String, String> getDataByRange(Range range) throws Exception;

	HashMap<String, String> getAllData() throws Exception;

	void saveAllData(HashMap<String, String> map) throws Exception;// Map<K, V> = HashMap<String, Object>

	void deleteDataByRange(Range range) throws Exception;

}