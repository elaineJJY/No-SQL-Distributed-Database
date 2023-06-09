package de.tum.node;

import com.google.protobuf.Empty;
import de.tum.grpc_api.ECSProto;
import de.tum.grpc_api.KVServiceGrpc;
import io.grpc.ManagedChannelBuilder;

import java.util.HashMap;
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
//public class NodeStub {
//
//    private ManagedChannel managedChannel;
//    private KVServiceGrpc.KVServiceBlockingStub blockingStub;
//
//    public NodeStub(String KVHost, int KVPort) {
//        this.managedChannel = ManagedChannelBuilder.forAddress(KVHost, KVPort).usePlaintext().build();
//        this.blockingStub = KVServiceGrpc.newBlockingStub(this.managedChannel);
//    }
//
//    public ManagedChannel getManagedChannel() {
//        return managedChannel;
//    }
//
//    public KVServiceGrpc.KVServiceBlockingStub getBlockingStub() {
//        return blockingStub;
//    }
//}

public class Node {
    private String host;
    static Empty emptyRequest = Empty.newBuilder().build();
    //public io.grpc.stub.AbstractBlockingStub stub;
    private final KVServiceGrpc.KVServiceBlockingStub stub;
    public static final int KV_LISTEN_ECS_PORT = 5200;

    public Node(String host) {
        this.host = host;
        this.stub = KVServiceGrpc.newBlockingStub(ManagedChannelBuilder.forAddress(host, KV_LISTEN_ECS_PORT).usePlaintext().build());
    }

    public long heartbeat() {
        ECSProto.HeartBeatResponse heartBeatResponse = this.stub.heartBeatRPC(emptyRequest);
        return heartBeatResponse.getTimestamp();
    }

    public Range getRange(DataType dataType) {
        return null;
    }

    @Override
    public String toString() {
		ECSProto.ToStringResponse response = this.stub.toStringRPC(emptyRequest);
        return response.getHostPort();
    }

    public boolean isResponsible(String key) throws NullPointerException {
        ECSProto.IsResponsibleRequest request = ECSProto.IsResponsibleRequest.newBuilder().setKey(key).build();
        return this.stub.isResponsibleRPC(request).getIsResponsible();
    }

    public void init() throws Exception {
        this.stub.initRPC(emptyRequest);
    }

    public void recover(Node removedNode) throws Exception {

    }

    public void updateRing(SortedMap<String, Node> ring) {

    }

    public void deleteExpiredData(DataType dataType, Range range) throws Exception {

    }

    public HashMap<String, Object> copy(DataType where, Range range) throws Exception {
        return null;
    }

    public Object get(String key) throws Exception {
        return null;
    }
    public void put(String key, String value) throws Exception {
        ECSProto.PutRequest request = ECSProto.PutRequest.newBuilder().setKey(key).setValue(value).build();
        this.stub.putRPC(request);
    }
    public void delete(String key) throws Exception {
        ECSProto.DeleteRequest request = ECSProto.DeleteRequest.newBuilder().setKey(key).build();
        this.stub.deleteRPC(request);
    }
    public void stop() {}
}