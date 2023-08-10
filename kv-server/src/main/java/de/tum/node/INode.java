package de.tum.node;

import de.tum.database.IDatabase;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;

/**
 * ClassName: INode
 * Package: de.tum.node
 * Description:
 *
 * @Author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @Create 2023/6/10 15:27
 * @Version 1.0
 */
public interface INode {
    int getPort();
    String getHost();
    long heartbeat();
    String toString();
    boolean isResponsible(String key);
    Range getRange(DataType dataType);
    HashMap<String, String> copy(DataType where, Range range) throws Exception;
    String get(String key, String transactionId) throws Exception;
    void put(String key, String value, String transactionID) throws Exception;
    void putBackup(String key, String value) throws Exception;
    void delete(String key, String transactionId) throws Exception;
    boolean hasKey(String key) throws Exception;
    List<String> executeTransactions(List<String> localCommands, String transactionId) throws Exception;
    void rollBack(String transactionId) throws Exception;
    void lock(String key, String transactionId) throws Exception;
    void unlock(String key, String transactionId) throws Exception;
    void unlockAll(String transactionId) throws Exception;
}
