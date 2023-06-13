package de.tum.node;

import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Map;
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

    public void init() {
        // ECSMessage
    }

    public void startKVServer() {
        this.stub.startKVServer(emptyRequest);
    }

    public void recover(Node removedNode) {
        ECSProto.RecoverRequest request = ECSProto.RecoverRequest.newBuilder()
                .setNode(ECSProto.NodeMessage.newBuilder()
                        .setHost(removedNode.getHost())
                        .setRpcPort(removedNode.getRpcPort())
                        .setPortForClient(removedNode.getPortForClient())
                        .build())
                .build();
        this.stub.recover(request);
    }

    public void updateRing(SortedMap<String, Node> ring) {
        ECSProto.UpdateRingRequest.Builder requestBuilder = ECSProto.UpdateRingRequest.newBuilder();
        for (Map.Entry<String, Node> entry : ring.entrySet()) {
            Node node = entry.getValue();
            ECSProto.NodeMessage.Builder nodeMessageBuilder = ECSProto.NodeMessage.newBuilder()
                    .setHost(node.getHost())
                    .setRpcPort(node.getRpcPort())
                    .setPortForClient(node.getPortForClient());
            requestBuilder.putRing(entry.getKey(), nodeMessageBuilder.build());
        }

        ECSProto.UpdateRingRequest request = requestBuilder.build();
        this.stub.updateRing(request);
    }

    public void deleteExpiredData(DataType dataType, Range range) {
        ECSProto.DeleteExpiredDataRequest.Builder requestBuilder = ECSProto.DeleteExpiredDataRequest.newBuilder();

        ECSProto.DataType dataTypeProto = dataType == DataType.DATA ? ECSProto.DataType.DATA : ECSProto.DataType.BACKUP;

        requestBuilder.setDataType(dataTypeProto)
                .setRange(ECSProto.Range.newBuilder()
                        .setFrom(range.getFrom())
                        .setTo(range.getTo())
                        .build())
                .build();

        ECSProto.DeleteExpiredDataRequest request = requestBuilder.build();
        this.stub.deleteExpiredData(request);
    }

    public void closeRpcChannel() {
        this.managedChannel.shutdown();
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