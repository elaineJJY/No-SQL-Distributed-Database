package de.tum.cacheDisplacement;

import java.util.LinkedList;
import de.tum.database.PersistentStorage;

/**
 * ClassName: FIFO
 * Package: de.tum.server.cacheDisplacement
 * Description: FIFO cache displacement algorithm. When the cache is full,
 * the first element in the cache will be replaced.
 *
 * @author Weijian Feng
 * @version 1.0
 */
public class FIFO extends Cache {
    private final int capacity;
    private final LinkedList<String> keys;
    private final LinkedList<String> values;


    public FIFO(int capacity, String directory) {
        super(directory);
        this.capacity = capacity;
        this.keys = new LinkedList<>();
        this.values = new LinkedList<>();
        getLOGGER().info("Init Cache with strategy FIFO");
    }

    @Override
    public void put(String key, String value) throws Exception {
        if (keys.size() >= capacity) {
            String oldKey = keys.removeFirst();
            getPersistentStorage().storeToDisk(oldKey, value);
            values.removeFirst();
        }
        keys.addLast(key);
        values.addLast(value);
    }

    @Override
    public String get(String key) throws Exception {
        int index = keys.indexOf(key);
        if (index >= 0) {
            return values.get(index);
        } else {
            String value = getPersistentStorage().readFromDisk(key);
            if (value!= null) {
                this.put(key, value);
                getPersistentStorage().deleteFromDisk(key);
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
        } else {
            getPersistentStorage().deleteFromDisk(key);
        }
    }
}
