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
    private static final String DEFAULT_DIR = "src/main/java/de/tum/server/database/data/backupdata";
    private final int capacity;
    private final Map<String, Object> cache; // 缓存
    private final Map<String, Integer> freq; // 记录每个 key 的访问次数
    private final Map<Integer, LinkedHashSet<String>> freqMap; // 记录每个访问次数对应的 key 集合
    private int minFreq;

    public LFU(int capacity) {
        super();
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.freq = new HashMap<>();
        this.freqMap = new HashMap<>();
        this.minFreq = 0;
        getLOGGER().info("Init Cache with strategy LFU");
    }

    @Override
    public Object get(String key) throws Exception {
        Object value = cache.get(key);
        if (!cache.containsKey(key)) {
            value = getPersistentStorage().readFromDisk(key);
            if (value != null) {
                this.put(key, value);
                getPersistentStorage().deleteFromDisk(key);
            }
        }
        int f = freq.get(key);
        freq.put(key, f + 1); // 更新访问次数
        freqMap.get(f).remove(key); // 从旧的访问次数对应的 key 集合中移除
        if (f == minFreq && freqMap.get(f).isEmpty()) {
            minFreq++; // 如果该 key 是当前最少访问的且对应的 key 集合为空，则更新 minFreq
        }
        freqMap.computeIfAbsent(f + 1, k -> new LinkedHashSet<>()).add(key); // 加入新的访问次数对应的 key 集合
        return value;
    }

    @Override
    public void put(String key, Object value) throws Exception {
        if (cache.containsKey(key)) {
            cache.put(key, value);
            get(key); // 更新访问次数
            return;
        }
        if (cache.size() == capacity) {
            String evictKey = freqMap.get(minFreq).iterator().next(); // 获取访问次数最少的 key 集合中的第一个 key，即要被淘汰的 key
            freqMap.get(minFreq).remove(evictKey); // 从访问次数最少的 key 集合中移除该 key
            getPersistentStorage().storeToDisk(evictKey, cache.get(evictKey));
            cache.remove(evictKey); // 从缓存中移除该 key
            freq.remove(evictKey); // 从访问次数的记录中移除该 key
        }
        cache.put(key, value); // 加入缓存
        freq.put(key, 1); // 访问次数初始化为 1
        freqMap.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key); // 加入访问次数为 1 的 key 集合
        minFreq = 1; // 更新 minFreq
    }

    @Override
    public void delete(String key) throws Exception {
        if (!cache.containsKey(key)) {
            getPersistentStorage().deleteFromDisk(key);
        }
        int f = freq.get(key);
        freq.remove(key);
        freqMap.get(f).remove(key);
        cache.remove(key);
        if (f == minFreq && freqMap.get(f).isEmpty()) {
            minFreq++;
        }
    }
}
