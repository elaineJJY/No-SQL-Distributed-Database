package de.tum.cacheDisplacement;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ClassName: LRU
 * Package: de.tum.server.cacheDisplacement
 * Description: LRU (Least Recently Used) is a caching algorithm that discards the least recently used items first.
 *
 * @author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @version 2.0 Test passed
 */
public class LRU extends Cache {
    private Map<String, String> cache;
    private final int capacity;

    public LRU(int capacity, String directory) {
        super(directory);
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true);
        getLOGGER().info("Init Cache with strategy LRU");
    }

    @Override
    public synchronized void put(String key, String value) throws Exception {
        if (cache.size() >= capacity) {
            String oldKey = cache.keySet().iterator().next();
            cache.remove(oldKey);
        }
        cache.put(key, value);
        getPersistentStorage().storeToDisk(key, value);
    }

    @Override
    public synchronized String get(String key) throws Exception {
        String value = cache.get(key);
        // if the value is not in the cache, then read from disk
        if (value == null) {
            value = getPersistentStorage().readFromDisk(key);
            if (value != null) {
                this.put(key, value);
            }
        }
        return value;
    }

    @Override
    public synchronized void delete(String key) throws Exception {
        cache.remove(key);
        getPersistentStorage().deleteFromDisk(key);
    }

    public static void main(String[] args) {
        FIFO lru = new FIFO(2, "src/main/java/de/tum/database/data/mainData");
        try {
            lru.put("1", "a");
            lru.put("1", "b");
            lru.put("3", "c");
            lru.put("4", "d");
            lru.delete("4");
            System.out.println(lru.get("1"));
            System.out.println(lru.get("3"));
            System.out.println(lru.get("4"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}