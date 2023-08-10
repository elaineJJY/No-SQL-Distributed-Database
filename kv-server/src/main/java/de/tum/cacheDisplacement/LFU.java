package de.tum.cacheDisplacement;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * ClassName: LFU
 * Package: de.tum.server.cacheDisplacement
 * Description:
 *
 * @Author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @Create 2023/5/10 10:18
 * @Version 1.0
 */
public class LFU extends Cache {
    private final int capacity;
    private final Map<String, String> cache;
    private final Map<String, Integer> freq; // record the frequency of each key
    private final Map<Integer, LinkedHashSet<String>> freqMap; // record the keys with the same frequency
    private int minFreq;

    public LFU(int capacity, String directory) {
        super(directory);
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.freq = new HashMap<>();
        this.freqMap = new HashMap<>();
        this.minFreq = 0;
        getLOGGER().info("Init Cache with strategy LFU");
    }

    @Override
    public String get(String key) throws Exception {
        String value = cache.get(key);
        if (!cache.containsKey(key)) {
            value = getPersistentStorage().readFromDisk(key);
            if (value != null) {
                this.put(key, value);
            } else {
                return null;
            }
        }
        int f = freq.get(key);
        freq.put(key, f + 1); // update the frequency
        freqMap.get(f).remove(key); // remove the key from the set of the old frequency
        if (f == minFreq && freqMap.get(f).isEmpty()) {
            minFreq++; // if the set of the old frequency is empty, update the minFreq
        }
        freqMap.computeIfAbsent(f + 1, k -> new LinkedHashSet<>()).add(key); // add the key to the set of the new frequency
        return value;
    }

    @Override
    public void put(String key, String value) throws Exception {
        if (cache.containsKey(key)) {
            cache.put(key, value);
            get(key); // update the frequency
            return;
        }
        if (cache.size() == capacity) {
            String evictKey = freqMap.get(minFreq).iterator().next(); // get the first key in the set of the minFreq, which is the least recently used
            freqMap.get(minFreq).remove(evictKey);
            cache.remove(evictKey);
            freq.remove(evictKey);
        }
        cache.put(key, value);
        freq.put(key, 1);
        freqMap.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
        minFreq = 1;
        getPersistentStorage().storeToDisk(key, value);
    }

    @Override
    public void delete(String key) throws Exception {
        int f = freq.get(key);
        freq.remove(key);
        freqMap.get(f).remove(key);
        cache.remove(key);
        if (f == minFreq && freqMap.get(f).isEmpty()) {
            minFreq++;
        }
        getPersistentStorage().deleteFromDisk(key);
    }
}
