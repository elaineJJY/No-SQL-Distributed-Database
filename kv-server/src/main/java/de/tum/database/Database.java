package de.tum.server.database;

import de.tum.common.ServerLogger;
import de.tum.server.cacheDisplacement.Cache;
import de.tum.server.cacheDisplacement.FIFO;
import de.tum.server.cacheDisplacement.LFU;
import de.tum.server.cacheDisplacement.LRU;

import java.util.logging.Logger;

/**
 * ClassName: Database
 * Package: de.tum.server
 * Description: Implementation of persistent database
 *
 * @Author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @Create 2023/5/9 20:32
 * @Version 1.0
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

	public Object get(String key) throws Exception {
		return cache.get(key);
	}

	public void put(String key, Object value) throws Exception {
		cache.put(key, value);
	}

	public void delete(String key) throws Exception {
		cache.delete(key);
	}
}