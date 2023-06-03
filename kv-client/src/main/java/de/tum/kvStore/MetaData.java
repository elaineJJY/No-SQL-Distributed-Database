package de.tum.kvStore;

import java.util.SortedMap;
import java.util.TreeMap;

public class MetaData {
    private final SortedMap<String, String> hashRing;

    public MetaData() {
        this.hashRing = new TreeMap<>();
    }

    /**
     * init: init the hash ring
     * @param response: response from server: <kr-from>, <kr-to>, <ip:port>; <kr-from>, <kr-to>, <ip:port>;...
     */
    public void init(String response) {
        String[] keyRanges = response.split(";");
        for (String keyRange : keyRanges) {
            String[] kr = keyRange.split(",");
            addNode(kr[0], kr[1], kr[2]);
        }
    }

    public void addNode(String krFrom, String krTo, String address) {
        String serverHash = Integer.toHexString(Integer.parseInt(krTo) + 1);
        hashRing.put(serverHash + 1, address);
    }

    /**
     * getIpByHash: get the ip address of the server that stores the key
     * @param hash: hash value of the key
     * @return ip address of the server: <ip:port>
     */
    public String getIPByHash(String hash) {
        if (hashRing.containsKey(hash)) {
            return hashRing.get(hash);
        } else {
            SortedMap<String, String> tailMap = hashRing.tailMap(hash);
            if (tailMap.isEmpty()) {
                return hashRing.get(hashRing.firstKey());
            }
            return tailMap.get(tailMap.firstKey());
        }
    }
}
