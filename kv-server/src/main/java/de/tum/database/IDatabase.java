package de.tum.database;

import de.tum.node.Range;
import java.util.Map;

public interface IDatabase {

	void put(String key, Object value) throws Exception;

	Object get(String key) throws Exception;

	void delete(String key) throws Exception;

	Map getDataByRange(Range range) throws Exception;

	Map getAllData() throws Exception;

	void saveData(Map data) throws Exception;// Map<K, V> = HashMap<String, Object>
}