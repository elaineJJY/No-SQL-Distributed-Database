package de.tum.node;

import com.google.protobuf.Empty;
import de.tum.grpc_api.ECSProto;
import de.tum.grpc_api.KVServiceGrpc;
import io.grpc.ManagedChannelBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

/**
 * ClassName: NodeStub
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
    static Empty emptyRequest = Empty.newBuilder().build();
    //public io.grpc.stub.AbstractBlockingStub stub;
    private final KVServiceGrpc.KVServiceBlockingStub stub;

    public Node(String host, int port) {
        this.host = host;
        this.port = port;
        this.stub = KVServiceGrpc.newBlockingStub(ManagedChannelBuilder.forAddress(host, port).usePlaintext().build());
    }

    public String getHost() { return host; }

    public int getPort() { return port; }

    public long heartbeat() {
        ECSProto.HeartBeatResponse heartBeatResponse = this.stub.heartBeat(emptyRequest);
        return heartBeatResponse.getTimestamp();
    }

    public Range getRange(DataType dataType) {
        ECSProto.DataType dataTypeProto;
        if (dataType == DataType.DATA) {
            dataTypeProto = ECSProto.DataType.DATA;
        } else {
            dataTypeProto = ECSProto.DataType.BACKUP;
        }
        ECSProto.GetRangeRequest request = ECSProto.GetRangeRequest.newBuilder().setDataType(dataTypeProto).build();
        ECSProto.GetRangeResponse response = this.stub.getRange(request);
        return new Range(response.getRange().getFrom(), response.getRange().getTo());
    }

    @Override
    public String toString() {
		ECSProto.ToStringResponse response = this.stub.toString(emptyRequest);
        return response.getHostPort();
    }

    public void init() {
        this.stub.init(emptyRequest);
    }

    public void recover(Node removedNode) {
        ECSProto.RecoverRequest request = ECSProto.RecoverRequest.newBuilder()
                .setNode(ECSProto.NodeMessage.newBuilder()
                        .setHost(removedNode.getHost()).setPort(removedNode.getPort())
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
                    .setPort(node.getPort());
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

    public void stop() {}
}