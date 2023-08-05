package de.tum.database;

import de.tum.cacheDisplacement.LRU;
import de.tum.node.MD5Hash;
import de.tum.node.Range;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackupDatabase implements IDatabase {
	private PersistentStorage persistentStorage;
	private static final String DEFAULT_DIR = "src/main/java/de/tum/database/data/backupData";
	private SortedMap<String, String> hashToKeyMap;   // Store <Hash, Key>

	private static ExecutorService executor = Executors.newFixedThreadPool(1);

	public BackupDatabase() {
		this.hashToKeyMap = new TreeMap<>();
		this.persistentStorage = new PersistentStorage(DEFAULT_DIR);
	}

	public void setDirectory(String directory) {
		persistentStorage.setDirectory(directory);
	}

//	public void put(String key, String value) throws Exception {
//		String hash = MD5Hash.hash(key);
//		hashToKeyMap.put(hash, key);
//		persistentStorage.storeToDisk(key, value);
//		System.out.println("finish put in backup: " + hashToKeyMap.get(key));
//	}

	public void put(String key, String value, String transactionId) throws Exception {
		String hash = MD5Hash.hash(key);
		hashToKeyMap.put(hash, key);
		persistentStorage.storeToDisk(key, value);
		System.out.println("finish put in backup: " + hashToKeyMap.get(key));
	}

//	public String get(String key) throws Exception {
//		return persistentStorage.readFromDisk(key);
//	}

	public String get(String key, String transactionId) throws Exception {
		return persistentStorage.readFromDisk(key);
	}

//	public void delete(String key) throws Exception {
//		hashToKeyMap.remove(key);
//		persistentStorage.deleteFromDisk(key);
//	}

	public void delete(String key, String transactionId) throws Exception {
		hashToKeyMap.remove(key);
		persistentStorage.deleteFromDisk(key);
	}

	public boolean hasKey(String key) throws Exception {
		if (persistentStorage.readFromDisk(key) == null) {
			return true;
		}
		return false;
	}

	@Override
	public void lock(String key, String transactionId) throws Exception {

	}

	@Override
	public void unlock(String key, String transactionId) throws Exception {

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
			data.put(key, persistentStorage.readFromDisk(key));
		}
		return data;
	}

	public HashMap<String, String> getAllData() throws Exception {
		HashMap<String, String> data = new HashMap<>();
		for (String key : hashToKeyMap.values()) {
			data.put(key, persistentStorage.readFromDisk(key));
		}
		return data;
	}

	public void saveAllData(HashMap<String, String> data) throws Exception {
		if (data != null) {
			for (Map.Entry<String, String> entry : data.entrySet()) {
				// Store each key and hash
				String hash = MD5Hash.hash(entry.getKey());
				hashToKeyMap.put(hash, entry.getKey());

				// Store key and value
				persistentStorage.storeToDisk(entry.getKey(), entry.getValue());
				System.out.println("save key: " + entry.getKey() + " to backup database");
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
			persistentStorage.deleteFromDisk(key);
			hashToKeyMap.remove(key);
			System.out.println("delete key: " + key + " from backup database");
		}
	}

	public static void main(String[] args) {
		BackupDatabase lru = new BackupDatabase();
		try {
			lru.put("1", "a", null);
			lru.put("1", "b", null);
			lru.put("3", "c", null);
			lru.put("4", "d", null);
			System.out.println(lru.get("1", null));
			System.out.println(lru.get("3", null));
			System.out.println(lru.get("4", null));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

