package de.tum.database;

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
	private static final String DEFAULT_DIR = "src/main/java/de/tum/server/database/data/backupdata";
	private SortedMap<String, String> hashToKeyMap;   // Store <Hash, Key>

	private static ExecutorService executor = Executors.newFixedThreadPool(1);

	public BackupDatabase() {
		this.hashToKeyMap = new TreeMap<>();
		this.persistentStorage = new PersistentStorage(DEFAULT_DIR);
	}

	public void setDirectory(String directory) {
		persistentStorage.setDirectory(directory);
	}

	public void put(String key, String value) throws Exception {
		String hash = MD5Hash.hash(key);
		hashToKeyMap.put(hash, key);
		persistentStorage.storeToDisk(key, value);
	}

	public String get(String key) throws Exception {
		return persistentStorage.readFromDisk(key);
	}

	public void delete(String key) throws Exception {
		hashToKeyMap.remove(key);
		persistentStorage.deleteFromDisk(key);
	}

	public HashMap<String, String> getDataByRange(Range range) throws Exception {
		HashMap<String, String> data = new HashMap<>();
		SortedMap<String, String> keysInRange = hashToKeyMap.subMap(range.getFrom(), range.getTo());
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
		for (Map.Entry<String, String> entry : data.entrySet()) {
			// Store each key and hash
			String hash = MD5Hash.hash(entry.getKey());
			hashToKeyMap.put(hash, entry.getKey());

			// Store key and value
			persistentStorage.storeToDisk(entry.getKey(), entry.getValue());
		}
	}

	public void deleteDataByRange(Range range) throws Exception {
		SortedMap<String, String> keysInRange = hashToKeyMap.subMap(range.getFrom(), range.getTo());
		for (String key : keysInRange.values()) {
			persistentStorage.deleteFromDisk(key);
			hashToKeyMap.remove(key);
		}
	}
}

