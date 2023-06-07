package de.tum.database;

import de.tum.cacheDisplacement.*;
import de.tum.common.ServerLogger;
import de.tum.node.MD5Hash;
import de.tum.node.Range;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * ClassName: Database
 * Package: de.tum.server
 * Description: Implementation of persistent database
 *
 * @author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @create 2023/5/9 20:32
 * @version 1.0
 */
public class MainDatabase implements IDatabase {
	private Logger LOGGER = ServerLogger.INSTANCE.getLogger();
	private Cache cache;
	private SortedMap<String, String> hashToKeyMap;

	public MainDatabase(int capacity, String cacheDisplacementAlgorithm) {
		this.hashToKeyMap = new TreeMap<>();
		switch (cacheDisplacementAlgorithm) {
			case "LRU":
				cache = new LRU(capacity);
				break;
			case "LFU":
				cache = new LFU(capacity);
				break;
			case "FIFO":
				cache = new FIFO(capacity);
				break;
			default:
				LOGGER.warning("Invalid cache displacement algorithm");
				break;
		}
	}

	public Object get(String key) throws Exception {
		return cache.get(key);
	}

	public void put(String key, Object value) throws Exception {
		String hash = MD5Hash.hash(key);
		hashToKeyMap.put(hash, key);
		cache.put(key, value);
	}

	public void delete(String key) throws Exception {
		hashToKeyMap.remove(key);
		cache.delete(key);
	}

	public HashMap<String, Object> getDataByRange(Range range) throws Exception {
		HashMap<String, Object> data = new HashMap<>(); // Map to store the gotten data
		SortedMap<String, String> keysInRange = hashToKeyMap.subMap(range.getFrom(), range.getTo());
		for (String key : keysInRange.values()) {
			data.put(key, cache.get(key));
		}
		return data;
	}

	public HashMap<String, Object> getAllData() throws Exception {
		HashMap<String, Object> data = new HashMap<>();
		for (String key : hashToKeyMap.values()) {
			data.put(key, cache.get(key));
		}
		return data;
	}

	public void saveAllData(HashMap<String, Object> data) throws Exception {
		for (Map.Entry<String, Object> entry : data.entrySet()) {
			// Store each key and hash
			String hash = MD5Hash.hash(entry.getKey());
			hashToKeyMap.put(hash, entry.getKey());

			// Store key and value to cache
			cache.put(entry.getKey(), entry.getValue());
		}
	}
}