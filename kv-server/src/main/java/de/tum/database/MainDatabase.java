package de.tum.database;

import de.tum.cacheDisplacement.*;
import de.tum.common.ServerLogger;
import de.tum.node.MD5Hash;
import de.tum.node.Range;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
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
	private static final String DEFAULT_DIR = "src/main/java/de/tum/database/data/mainData";
	private Logger LOGGER = ServerLogger.INSTANCE.getLogger();
	private Cache cache;
	private SortedMap<String, String> hashToKeyMap;
	private ConcurrentHashMap<String, String> locks = new ConcurrentHashMap<>(); // key -> transaction id

	public MainDatabase(int capacity, String cacheDisplacementAlgorithm) {
		this.hashToKeyMap = new TreeMap<>();
		switch (cacheDisplacementAlgorithm) {
			case "LRU":
				cache = new LRU(capacity, DEFAULT_DIR);
				break;
			case "LFU":
				cache = new LFU(capacity, DEFAULT_DIR);
				break;
			case "FIFO":
				cache = new FIFO(capacity, DEFAULT_DIR);
				break;
			default:
				LOGGER.warning("Invalid cache displacement algorithm");
				break;
		}
	}

	public void lock(String key, String transactionId) throws Exception{
		if(locks.containsKey(key) && !locks.get(key).equals(transactionId)) {
			throw new Exception(key + " is locked");
		}
		locks.put(key, transactionId);
	}

	public void unlock(String key, String transactionId) throws Exception{
		if(locks.containsKey(key) && !locks.get(key).equals(transactionId)) {
			throw new Exception(key + " is locked by another transaction");
		}
		locks.remove(key);
	}

	public void setDirectory(String directory) {
		cache.setDirectory(directory);
	}

//	public String get(String key) throws Exception {
//		if(locks.containsKey(key) && locks.get(key).length() > 0){
//			throw new Exception(key + " is locked");
//		}
//		return cache.get(key);
//	}
	public String get(String key, String transactionId) throws Exception {
		if(locks.containsKey(key) && !locks.get(key).equals(transactionId)) {
			throw new Exception(key + " is locked");
		}
		return cache.get(key);
	}

//	public void put(String key, String value) throws Exception {
//		if(locks.containsKey(key) && locks.get(key).length() > 0) {
//			System.out.println("testdata");
//			throw new Exception(key + " is locked");
//		}
//		String hash = MD5Hash.hash(key);
//		hashToKeyMap.put(hash, key);
//		cache.put(key, value);
//	}

	public void put(String key, String value, String transactionId) throws Exception {
		if(locks.containsKey(key) && !locks.get(key).equals(transactionId)) {
//			System.out.println("testdata");
			throw new Exception(key + " is locked");
		}
		String hash = MD5Hash.hash(key);
		hashToKeyMap.put(hash, key);
		cache.put(key, value);
	}

//	public void delete(String key) throws Exception {
//		if(locks.containsKey(key) && locks.get(key).length() > 0) {
//			throw new Exception(key + " is locked");
//		}
//		hashToKeyMap.remove(key);
//		cache.delete(key);
//	}

	public void delete(String key, String transactionId) throws Exception {
		if(locks.containsKey(key) && !locks.get(key).equals(transactionId)) {
			throw new Exception(key + " is locked");
		}
		hashToKeyMap.remove(key);
		cache.delete(key);
	}

	public boolean hasKey(String key) throws Exception {
		if (hashToKeyMap.get(key) == null) {
			return false;
		}
		return true;
	}

	public HashMap<String, String> getDataByRange(Range range) throws Exception {
		HashMap<String, String> data = new HashMap<>();

		SortedMap<String, String> keysInRange;
		if (range.getTo().compareTo(range.getFrom()) < 0) {
			keysInRange = hashToKeyMap.tailMap(range.getFrom());
			keysInRange.putAll(hashToKeyMap.headMap(range.getTo()));
		} else {
			keysInRange = hashToKeyMap.subMap(range.getFrom(), range.getTo());
		}

		for (String key : keysInRange.values()) {
			data.put(key, cache.get(key));
		}
		return data;
	}

	public HashMap<String, String> getAllData() throws Exception {
		HashMap<String, String> data = new HashMap<>();
		for (String key : hashToKeyMap.values()) {
			data.put(key, cache.get(key));
		}
		return data;
	}

	public void saveAllData(HashMap<String, String> data) throws Exception {
		if (data != null) {
			for (Map.Entry<String, String> entry : data.entrySet()) {
				// Store each key and hash
				String hash = MD5Hash.hash(entry.getKey());
				hashToKeyMap.put(hash, entry.getKey());

				// Store key and value to cache
				cache.put(entry.getKey(), entry.getValue());
				System.out.println("save key: " + entry.getKey() + " to database");
			}
		}
	}

	public void deleteDataByRange(Range range) throws Exception {
		SortedMap<String, String> keysInRange;
		if (range.getTo().compareTo(range.getFrom()) < 0) {
			keysInRange = hashToKeyMap.tailMap(range.getFrom());
			keysInRange.putAll(hashToKeyMap.headMap(range.getTo()));
		} else {
			keysInRange = hashToKeyMap.subMap(range.getFrom(), range.getTo());
		}

		for (String key : keysInRange.values()) {
			cache.delete(key);
			hashToKeyMap.remove(key);
			System.out.println("delete key: " + key + " from database");
		}
	}
}