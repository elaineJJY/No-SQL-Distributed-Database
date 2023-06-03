package de.tum.kvStore;

public interface KVStore {
    byte[] startConnection(String ip, int port) throws Exception;
    boolean stopConnection() throws Exception;
    byte[] put(String key, String value) throws Exception;
    byte[] get(String key) throws Exception;
    byte[] delete(String key) throws Exception;
    boolean isConnected();
}
