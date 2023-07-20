package de.tum.cacheDisplacement;

import de.tum.common.ServerLogger;

import de.tum.database.PersistentStorage;
import java.util.logging.Logger;

/**
 * ClassName: CacheDisplacementInterface
 * Package: de.tum.server.cacheDisplacement
 * Description:
 *
 * @Author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @Create 2023/5/13 10:45
 * @Version 1.0
 */
public abstract class Cache {
    private PersistentStorage persistentStorage;
    private static Logger LOGGER = ServerLogger.INSTANCE.getLogger();

    public Cache(String DEFAULT_DIR) {
        this.persistentStorage = new PersistentStorage(DEFAULT_DIR);
    }

    public void setDirectory(String directory) {
        this.persistentStorage.setDirectory(directory);
    }

    public Logger getLOGGER() {
        return LOGGER;
    }

    public PersistentStorage getPersistentStorage() {
        return persistentStorage;
    }

    public abstract void put(String key, String value) throws Exception;
    public abstract String get(String key) throws Exception ;
    public abstract void delete(String key) throws Exception;
}
