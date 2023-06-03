package de.tum.node;

import org.w3c.dom.Node;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * ClassName: ConsistentHash
 * Package: de.tum.node
 * Description:
 *
 * @Author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @Create 2023/6/2 23:37
 * @Version 1.0
 */
public class ConsistentHash {
    private SortedMap<Long, Long> ring;

    public ConsistentHash() {
        ring = new TreeMap<>();
        nodes = new ArrayList<>();
    }

    public Node getNode(String key) {
        if (ring.isEmpty()) {
            return null;
        }

        long hash = hash(key);
        SortedMap<Long, Long> tailMap = ring.tailMap(hash);
        long nodeHash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
        return ring.get(nodeHash);
    }

    private void updateRing() {
        ring.clear();
        for (Node node : nodes) {
            long nodeHash = hash(node.getIdentifier());
            ring.put(nodeHash, node);
        }
    }

    private long hash(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            byte[] digest = md.digest();
            // MD5 return 16 Bytea
            return ((long) (digest[3] & 0xFF) << 24) |
                    ((long) (digest[2] & 0xFF) << 16) |
                    ((long) (digest[1] & 0xFF) << 8) |
                    (digest[0] & 0xFF);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found.");
        }
    }
}
