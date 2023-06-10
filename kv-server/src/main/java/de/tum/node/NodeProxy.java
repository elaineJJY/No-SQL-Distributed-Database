package de.tum.node;

import com.google.protobuf.Empty;
import de.tum.grpc_api.KVServerProto;
import de.tum.grpc_api.KVServiceGrpc;
import io.grpc.ManagedChannelBuilder;

import java.util.Date;
import java.util.HashMap;

/**
 * ClassName: NodeProxy
 * Package: de.tum.node
 * Description:
 *
 * @Author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @Create 2023/6/10 15:27
 * @Version 1.0
 */
public class NodeProxy implements INode {
    private String host;
    private int port;
    private final KVServiceGrpc.KVServiceBlockingStub stub;
    static Empty emptyRequest = Empty.newBuilder().build();

    public NodeProxy(String host, int port) {
        this.host = host;
        this.port = port;
        this.stub = KVServiceGrpc.newBlockingStub(ManagedChannelBuilder.forAddress(host, port).usePlaintext().build());
    }

    public String getHost() { return host; }

    public int getPort() { return port; }

    public long heartbeat() {
        KVServerProto.HeartBeatResponse heartBeatResponse = this.stub.heartBeat(emptyRequest);
        return heartBeatResponse.getTimestamp();
    }

    public Range getRange(DataType dataType) {
        KVServerProto.DataType dataTypeProto;
        if (dataType == DataType.DATA) {
            dataTypeProto = KVServerProto.DataType.DATA;
        } else {
            dataTypeProto = KVServerProto.DataType.BACKUP;
        }
        KVServerProto.GetRangeRequest request = KVServerProto.GetRangeRequest.newBuilder().setDataType(dataTypeProto).build();
        KVServerProto.GetRangeResponse response = this.stub.getRange(request);
        return new Range(response.getRange().getFrom(), response.getRange().getTo());
    }

    @Override
    public String toString() {
        KVServerProto.ToStringResponse response = this.stub.toString(emptyRequest);
        return response.getHostPort();
    }

    public boolean isResponsible(String key) throws NullPointerException {
        KVServerProto.IsResponsibleRequest request = KVServerProto.IsResponsibleRequest.newBuilder().setKey(key).build();
        return this.stub.isResponsible(request).getIsResponsible();
    }

    public HashMap<String, String> copy(DataType where, Range range) throws Exception {
        KVServerProto.DataType whereProto;
        if (where == DataType.DATA) {
            whereProto = KVServerProto.DataType.DATA;
        } else {
            whereProto = KVServerProto.DataType.BACKUP;
        }
        KVServerProto.Range rangeProto = KVServerProto.Range.newBuilder()
                .setFrom(range.getFrom()).setTo(range.getTo()).build();

        KVServerProto.CopyRequest request = KVServerProto.CopyRequest.newBuilder()
                .setWhere(whereProto).setRange(rangeProto).build();
        //TODO: Check if cast into HashMap<String, String> is safe
        return (HashMap<String, String>) this.stub.copy(request).getData();
    }

    public String get(String key) throws Exception {
        KVServerProto.GetRequest request = KVServerProto.GetRequest.newBuilder().setKey(key).build();
        KVServerProto.GetResponse response = this.stub.get(request);
        return response.getValue();
    }

    public void put(String key, String value) throws Exception {
        KVServerProto.PutRequest request = KVServerProto.PutRequest.newBuilder().setKey(key).setValue(value).build();
        this.stub.put(request);
    }

    public void delete(String key) throws Exception {
        KVServerProto.DeleteRequest request = KVServerProto.DeleteRequest.newBuilder().setKey(key).build();
        this.stub.delete(request);
    }
}
