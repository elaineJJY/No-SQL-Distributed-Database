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
    public int getPort();
    public String getHost();
    public long heartbeat();
    public String toString();
    public boolean isResponsible(String key);
    public Range getRange(DataType dataType);
    public HashMap<String, String> copy(DataType where, Range range) throws Exception;
    public String get(String key) throws Exception;
    public void put(String key, String value) throws Exception;
    public void putBackup(String key, String value) throws Exception;
    public void delete(String key) throws Exception;
    public boolean hasKey(String key) throws Exception;
    public List<String> executeTransactions(List<String> localCommands, String transactionId) throws Exception;
    public void rollBack(String transactionId) throws Exception;
    public void lock(String key) throws Exception;
    public void unlock(String key) throws Exception;
    public void unlockAll(String transactionId) throws Exception;
//    public void lock(String key) throws Exception;
    // Only called by ECS
    //public void init() throws Exception;
    //public void startKVServer() throws Exception;
    //public void recover(INode removedNode) throws Exception;
    //public void updateRing(SortedMap<String, INode> ring);
    //public void deleteExpiredData(DataType dataType, Range range) throws Exception;
}
