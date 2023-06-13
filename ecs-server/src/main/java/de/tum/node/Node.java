package de.tum.node;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import de.tum.common.ECSMessage;
import de.tum.common.ECSMessageBuilder;
import de.tum.common.StatusCode;

import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.Date;
import java.util.NoSuchElementException;

/**
 * ClassName: NodeProxy
 * Package: de.tum.node
 * Description:
 *
 * @Author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @Create 2023/6/8 12:54
 * @Version 1.0
 */

public class Node {
    private String host;
    private int port;
    private SocketChannel socketChannel;

    public Node(String host, int port, SocketChannel socketChannel) {
        this.host = host;
        this.port = port;
        this.socketChannel = socketChannel;
    }

    public String getHost() { return this.host; }

    public int getPort() { return this.port; }
    public SocketChannel getSocketChannel() { return this.socketChannel; }

    public long heartbeat() { return new Date().getTime(); }

    @Override
    public String toString() { return host + ":" + port; }

    public boolean init() throws Exception {
        String response = ECSMessageBuilder.create()
                .command(ECSMessage.Command.INIT)
                .sendAndRespond(this.socketChannel);
        StatusCode statusCode = JSON.parseObject(response, new TypeReference<StatusCode>() {});
        return statusCode == StatusCode.OK;
    }

    public void recover(Node removedNode) throws Exception {
        ECSMessageBuilder.create()
                .command(ECSMessage.Command.RECOVER)
                .removedNode(removedNode)
                .sendAndRespond(this.socketChannel);
    }

    public void updateRing(SortedMap<String, Node> ring) throws Exception {
        HashMap<String, String> sentRing = new HashMap<>();
        for (Map.Entry<String, Node> entry : ring.entrySet()) {
            String key = entry.getKey();
            Node node = entry.getValue();
            String host = node.getHost();
            int port = node.getPort();
            sentRing.put(host + ":" + port, key);
        }
        ECSMessageBuilder.create()
                .command(ECSMessage.Command.UPDATE_RING)
                .ring(sentRing)
                .sendAndRespond(this.socketChannel);
    }

    public void deleteExpiredData(DataType dataType, Range range) throws Exception {
        ECSMessageBuilder.create()
                .command(ECSMessage.Command.DELETE_EXPIRED_DATA)
                .dataType(dataType)
                .range(range)
                .sendAndRespond(this.socketChannel);
    }

    /**
     * Get the range of this node in the ring
     * @return String range
     */
    public Range getRange(DataType dataType) {
        Node from = null;
        Node to = null;
        if (dataType == DataType.DATA) {
            from = ConsistentHash.INSTANCE.getPreviousNode(this);
            to = this;
        }
        if (dataType == DataType.BACKUP) {
            from = this;
            to = ConsistentHash.INSTANCE.getNextNode(this);
        }
        if (from == null || to == null) {
            throw new NoSuchElementException("No such node");
        }
        return new Range(ConsistentHash.INSTANCE.getHash(from), ConsistentHash.INSTANCE.getHash(to));
    }

}