package de.tum.server.cacheDisplacement;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ClassName: LRU
 * Package: de.tum.server.cacheDisplacement
 * Description: LRU (Least Recently Used) is a caching algorithm that discards the least recently used items first.
 *
 * @author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @version 2.0
 */
public class LRU implements Cache {

    private Map<String, Object> cache;
    private final int capacity;

    public LRU(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true);
        LOGGER.info("Init Cache with strategy LRU");
    }

    @Override
    public synchronized void put(String key, Object value) throws Exception {
        if (cache.size() >= capacity) {
            String oldKey = cache.keySet().iterator().next();
            cache.remove(oldKey);
        }
        cache.put(key, value);
        CacheManager.storeToDisk(key, value);
    }

    @Override
    public synchronized Object get(String key) throws Exception {
        Object value = cache.get(key);
        // if the value is not in the cache, then read from disk
        if (value == null) {
            value = CacheManager.readFromDisk(key);
            if (value != null) {
                this.put(key, value);
            }
        }
        return value;
    }

    @Override
    public synchronized void delete(String key) throws Exception {
        cache.remove(key);
        CacheManager.deleteFromDisk(key);
    }
}