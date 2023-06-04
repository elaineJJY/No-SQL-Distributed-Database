package de.tum.server.cacheDisplacement;

import de.tum.node.MD5Hash;
import java.io.*;
import java.util.SortedMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Map.Entry;
import java.util.concurrent.Future;

public class CacheManager {
    // store hash and correponded
    private SortedMap<String, String> cache;
    private static String directory = "src/main/java/de/tum/server/database/data/";
    static ExecutorService executor = Executors.newFixedThreadPool(1);

    public static void setDirectory(String directory) {
        CacheManager.directory = directory;

    }

    public static void storeToDisk(String key, Object value) throws IOException {
        Callable<Entry<String, String>> writingTask = new Callable<Entry<String, String>>() {
            @Override
            public Entry<String, String> call() throws Exception {
                String hash = MD5Hash.hash(key);
                String fileName = directory + "/"  + key + ".dat";
                FileWriter fileWriter = new FileWriter(fileName);
                // Return null for testing purpose
                // TODO
                return null;
            }
        };
        Future<Entry<String, String>> future = executor.submit(writingTask);
    }

    public static Object readFromDisk(String key) throws Exception {
        Callable<Object> readingTask = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                String fileName = directory + "/"  + key + ".dat";
                if (!new File(fileName).exists()) {
                    return null;
                }
                FileInputStream fis = new FileInputStream(fileName);
                ObjectInputStream ois = new ObjectInputStream(fis);
                Object value = ois.readObject();
                ois.close();
                fis.close();
                return value;
            }
        };
        Future<Object> future = executor.submit(readingTask);
        return future.get();
    }

    public static void deleteFromDisk(String key) throws Exception {
        Callable<String> deletingTask = new Callable<String>() {
            @Override
            public String call() throws Exception {
                String fileName = directory + "/"  + key + ".dat";
                File file = new File(fileName);
                file.delete();
                return null;
            }
        };
        Future<String> future = executor.submit(deletingTask);
    }
}
