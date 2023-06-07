package de.tum.database;

import de.tum.node.Range;
import java.util.HashMap;

public interface IDatabase {

	void put(String key, Object value) throws Exception;

	Object get(String key) throws Exception;

	void delete(String key) throws Exception;

	HashMap<String, Object> getDataByRange(Range range) throws Exception;

	HashMap<String, Object> getAllData() throws Exception;

	void saveAllData(HashMap<String, Object> map) throws Exception;// Map<K, V> = HashMap<String, Object>

	void deleteDataByRange(Range range) throws Exception;

}