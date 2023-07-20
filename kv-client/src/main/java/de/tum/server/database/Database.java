package de.tum.server.database;

import de.tum.common.ServerLogger;
import de.tum.server.cacheDisplacement.*;

import java.util.logging.Logger;

/**
 * ClassName: Database
 * Package: de.tum.server
 * Description: Implementation of persistent database
 *
 * @author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @version 1.0
 */
public enum Database {
	INSTANCE;
	private static final Logger LOGGER = ServerLogger.INSTANCE.getLogger();
	private Cache cache;

	public void init(int capacity, String cacheDisplacementAlgorithm) {
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

	public void setDirectory(String directory) {
		CacheManager.setDirectory(directory);
	}

	/**
	 * General method to get the value of the key by calling the corresponding method of the cache
	 * @param key key of the key-value pair
	 * @return Object
	 * @throws Exception Exception
	 */
	public Object get(String key) throws Exception {
		return cache.get(key);
	}

	/**
	 * General method to put the key-value pair into the cache by calling the corresponding method of the cache
	 * @param key key of the key-value pair
	 * @param value value of the key-value pair
	 * @throws Exception Exception
	 */
	public void put(String key, Object value) throws Exception {
		cache.put(key, value);
	}

	/**
	 * General method to delete the key-value pair from the cache by calling the corresponding method of the cache
	 * @param key key of the key-value pair
	 * @throws Exception Exception
	 */
	public void delete(String key) throws Exception {
		cache.delete(key);
	}
}