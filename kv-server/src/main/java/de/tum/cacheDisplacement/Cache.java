package de.tum.cacheDisplacement;

import de.tum.common.ServerLogger;

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
public interface Cache {
    public static Logger LOGGER = ServerLogger.INSTANCE.getLogger();
    public void put(String key, Object value) throws Exception;
    public Object get(String key) throws Exception ;
    public void delete(String key) throws Exception;
}
