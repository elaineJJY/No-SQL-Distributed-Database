package de.tum.server.cacheDisplacement;

import java.util.LinkedList;

/**
 * ClassName: FIFO
 * Package: de.tum.server.cacheDisplacement
 * Description: FIFO cache displacement algorithm. When the cache is full,
 * the first element in the cache will be replaced.
 *
 * @author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @version 1.0
 */
public class FIFO implements Cache {

    private final int capacity;
    private final LinkedList<String> keys;
    private final LinkedList<Object> values;

    public FIFO(int capacity) {
        this.capacity = capacity;
        this.keys = new LinkedList<>();
        this.values = new LinkedList<>();
        LOGGER.info("Init Cache with strategy FIFO");
    }

    @Override
    public void put(String key, Object value) throws Exception {
        if (keys.size() >= capacity) {
            keys.removeFirst();
            values.removeFirst();
        }
        if (keys.contains(key)) {
            int index = keys.indexOf(key);
            keys.remove(index);
            values.remove(index);
        }
        keys.addLast(key);
        values.addLast(value);
        CacheManager.storeToDisk(key, value);
    }

    @Override
    public Object get(String key) throws Exception {
        int index = keys.indexOf(key);
        if (index >= 0) {
            return values.get(index);
        } else {
            Object value = CacheManager.readFromDisk(key);
            if (value!= null) {
                this.put(key, value);
            }
            return value;
        }
    }

    @Override
    public void delete(String key) throws Exception {
        // if not finding the key, return -1
        int index = keys.indexOf(key);
        if (index >= 0) {
            keys.remove(index);
            values.remove(index);
        }
        CacheManager.deleteFromDisk(key);
    }
}
