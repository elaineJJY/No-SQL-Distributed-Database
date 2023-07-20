package de.tum.server.cacheDisplacement;

import java.io.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Map.Entry;
import java.util.concurrent.Future;

/**
 * ClassName: CacheManager used to change Cache management method (e.g. LRU, LFU, FIFO)
 * Package: de.tum.server.cacheDisplacement
 * Description:
 *
 * @Author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @Create 2023/5/13 10:46
 * @Version 1.0
 */
public class CacheManager {
    private static String directory = "src/main/java/de/tum/server/database/data/";
    static ExecutorService executor = Executors.newFixedThreadPool(1);

    public static void setDirectory(String directory) {
        CacheManager.directory = directory;
    }

    /**
     * Persist the key-value pair to the disk
     * @param key, value
     * @throws IOException
     */
    public static void storeToDisk(String key, Object value) throws IOException {
        Callable<Entry<String, String>> writingTask = new Callable<Entry<String, String>>() {
            @Override
            public Entry<String, String> call() throws Exception {
                String fileName = directory + "/"  + key + ".dat";
                checkFileExistence(fileName);
                FileOutputStream fos = new FileOutputStream(fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(value);
                oos.close();
                fos.close();
                return null;
            }
        };
        Future<Entry<String, String>> future = executor.submit(writingTask);
    }

    /**
     * Read the key-value pair from the disk
     * @param key
     * @return Object
     * @throws Exception
     */
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

    /**
     * Delete the key-value pair from the disk
     * @param key
     * @throws Exception
     */
    public static void deleteFromDisk(String key) throws Exception {
        Callable<String> deletingTask = new Callable<String>() {
            @Override
            public String call() throws Exception {
                String fileName = directory + "/"  + key + ".dat";
                if (!new File(fileName).exists()) {
                    return null;
                }
                File file = new File(fileName);
                file.delete();
                return null;
            }
        };
        Future<String> future = executor.submit(deletingTask);
    }

    // check if the file exists, if not, create it
    private static void checkFileExistence(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            if (file.getParentFile() != null){
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
    }
}
