package de.tum.node;

import com.google.protobuf.Empty;
import de.tum.grpc_api.KVServerProto;
import de.tum.grpc_api.KVServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private int rpcPort;
    private int portForClient;
    private final ManagedChannel managedChannel;
    private final KVServiceGrpc.KVServiceBlockingStub stub;
    static Empty emptyRequest = Empty.newBuilder().build();

    public NodeProxy(String host, int rpcPort, int portForClient) {
        this.host = host;
        this.rpcPort = rpcPort;
        this.portForClient = portForClient;
        this.managedChannel = ManagedChannelBuilder.forAddress(host, rpcPort).usePlaintext().build();
        this.stub = KVServiceGrpc.newBlockingStub(managedChannel);
    }

    public String getHost() { return host; }
    public int getRpcPort() { return rpcPort; }
    public int getPort() { return portForClient; }

    public long heartbeat() {
        KVServerProto.HeartBeatResponse response = this.stub.heartBeat(emptyRequest);
        return response.getTimestamp();
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

//    @Override
//    public String toString() {
//        KVServerProto.ToStringResponse response = this.stub.toString(emptyRequest);
//        return response.getHostPort();
//    }
    public String toString() {
        return this.host + ":" + this.portForClient;
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
        Map<String, String> returnMap = this.stub.copy(request).getDataMap();
        HashMap<String, String> returnHashMap = new HashMap<>();
        for (Map.Entry<String, String> entry : returnMap.entrySet()) {
            returnHashMap.put(entry.getKey(), entry.getValue());
        }
        return returnHashMap;
    }

    public String get(String key, String transactionId) throws Exception {
        KVServerProto.GetRequest request = KVServerProto.GetRequest.newBuilder()
            .setKey(key).setTransactionId(transactionId).build();
        KVServerProto.GetResponse response = this.stub.get(request);
        return response.getValue();
    }

    public void put(String key, String value, String transactionId) throws Exception {
        KVServerProto.PutRequest request = KVServerProto.PutRequest.newBuilder().setKey(key)
            .setValue(value).setTransactionId(transactionId).build();
        this.stub.put(request);
    }

    public void putBackup(String key, String value) throws Exception {
        KVServerProto.PutBackupRequest request = KVServerProto.PutBackupRequest.newBuilder().setKey(key).setValue(value).build();
        this.stub.putBackup(request);
    }

    public void delete(String key, String transactionId) throws Exception {
        KVServerProto.DeleteRequest request = KVServerProto.DeleteRequest.newBuilder()
            .setKey(key).setTransactionId(transactionId).build();
        this.stub.delete(request);
    }

    public boolean hasKey(String key) throws Exception {
        KVServerProto.HasKeyRequest request = KVServerProto.HasKeyRequest.newBuilder().setKey(key).build();
        return this.stub.hasKey(request).getHasKey();
    }

    public List<String> executeTransactions(List<String> localCommands, String transactionId) {
        KVServerProto.ExecuteTransactionsRequest request = KVServerProto.ExecuteTransactionsRequest.newBuilder()
                .addAllLocalCommands(localCommands).setTransactionId(transactionId).build();
        KVServerProto.ExecuteTransactionsResponse response = this.stub.executeTransactions(request);
        return response.getResultsList();
    }

    public void rollBack(String transactionId) {
        KVServerProto.RollbackRequest request = KVServerProto.RollbackRequest.newBuilder().setTransactionId(transactionId).build();
        this.stub.rollBack(request);
    }
    public void lock(String key, String transactionId) {}

    public void unlock(String key, String transactionId) {}

    public void unlockAll(String transactionId) {
        KVServerProto.unlockAllRequest request = KVServerProto.unlockAllRequest.newBuilder().setTransactionId(transactionId).build();
        this.stub.unlockAll(request);
    }

    public void closeRpcChannel() {
        this.managedChannel.shutdown();
    }
}
