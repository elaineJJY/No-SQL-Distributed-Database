package de.tum.node;

import com.google.protobuf.Empty;
import de.tum.grpc_api.ECSProto;
import de.tum.grpc_api.KVServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Map;
import java.util.SortedMap;

/**
 * ClassName: NodeProxy
 * Package: de.tum.node
 * Description:
 *
 * @Author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @Create 2023/6/8 12:54
 * @Version 1.0
 */

public class NodeProxy {
    private String host;
    private int rpcPort;
    private int portForClient;
    static Empty emptyRequest = Empty.newBuilder().build();
    private final ManagedChannel managedChannel;
    private final KVServiceGrpc.KVServiceBlockingStub stub;

    public NodeProxy(String host, int rpcPort, int portForClient) {
        this.host = host;
        this.rpcPort = rpcPort;
        this.portForClient = portForClient;
        this.managedChannel = ManagedChannelBuilder.forAddress(host, rpcPort).usePlaintext().build();
        this.stub = KVServiceGrpc.newBlockingStub(managedChannel);
    }

    public String getHost() { return host; }

    public int getRpcPort() { return rpcPort; }
    public int getPortForClient() { return portForClient; }

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
        return host + ":" + portForClient;
    }

    public void init() {
        this.stub.init(emptyRequest);
    }

    public void startKVServer() {
        this.stub.startKVServer(emptyRequest);
    }

    public void recover(NodeProxy removedNodeProxy) {
        ECSProto.RecoverRequest request = ECSProto.RecoverRequest.newBuilder()
                .setNode(ECSProto.NodeMessage.newBuilder()
                        .setHost(removedNodeProxy.getHost())
                        .setRpcPort(removedNodeProxy.getRpcPort())
                        .setPortForClient(removedNodeProxy.getPortForClient())
                        .build())
                .build();
        this.stub.recover(request);
    }

    public void updateRing(SortedMap<String, NodeProxy> ring) {
        ECSProto.UpdateRingRequest.Builder requestBuilder = ECSProto.UpdateRingRequest.newBuilder();
        for (Map.Entry<String, NodeProxy> entry : ring.entrySet()) {
            NodeProxy nodeProxy = entry.getValue();
            ECSProto.NodeMessage.Builder nodeMessageBuilder = ECSProto.NodeMessage.newBuilder()
                    .setHost(nodeProxy.getHost())
                    .setRpcPort(nodeProxy.getRpcPort())
                    .setPortForClient(nodeProxy.getPortForClient());
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
}