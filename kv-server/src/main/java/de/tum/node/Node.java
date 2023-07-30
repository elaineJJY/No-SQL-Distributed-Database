package de.tum.node;


import com.google.protobuf.Descriptors;
import com.google.protobuf.Empty;
import de.tum.communication.KVServer;
import de.tum.database.BackupDatabase;
import de.tum.database.IDatabase;
import de.tum.database.MainDatabase;
import de.tum.grpc_api.KVServerProto;
import de.tum.grpc_api.KVServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import org.checkerframework.checker.units.qual.C;

import javax.xml.crypto.Data;
import java.io.Serializable;
import java.util.*;

//public class Node extends KVServiceGrpc.KVServiceImplBase implements Serializable {
public class Node extends KVServiceGrpc.KVServiceImplBase implements Serializable, INode {

	private KVServer server;
	private String host;
	private int port;
	private IDatabase mainDatabase;
	private IDatabase backupDatabase;


	public Node(String host, int port, IDatabase mainDatabase, IDatabase backupDatabase){
		this.host = host;
		this.port = port;
		this.mainDatabase = mainDatabase;
		this.backupDatabase = backupDatabase;
	}

	public String getHost() { return host; }

	public int getPort() { return port; }

	public long heartbeat() { return new Date().getTime(); }

	@Override
	public void heartBeat(com.google.protobuf.Empty request,
						  io.grpc.stub.StreamObserver<de.tum.grpc_api.KVServerProto.HeartBeatResponse> responseObserver) {
		KVServerProto.HeartBeatResponse response = KVServerProto.HeartBeatResponse.newBuilder()
				.setTimestamp(new Date().getTime())
				.build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	/**
	 * Get the range of this node in the ring
	 * @return String range
	 */
	public Range getRange(DataType dataType) {
		INode from = null;
		INode to = null;
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

	@Override
	public void getRange(de.tum.grpc_api.KVServerProto.GetRangeRequest request,
						 io.grpc.stub.StreamObserver<de.tum.grpc_api.KVServerProto.GetRangeResponse> responseObserver) {
		INode from = null;
		INode to = null;

		int dataType = request.getDataType().getNumber();

		if (dataType == DataType.DATA.ordinal()) {
			from = ConsistentHash.INSTANCE.getPreviousNode(this);
			to = this;
		}
		if (dataType == DataType.BACKUP.ordinal()) {
			from = this;
			to = ConsistentHash.INSTANCE.getNextNode(this);
		}
		if (from == null || to == null) {
			throw new NoSuchElementException("No such node");
		}

		KVServerProto.GetRangeResponse response = KVServerProto.GetRangeResponse.newBuilder()
				.setRange(KVServerProto.Range.newBuilder()
						.setFrom(ConsistentHash.INSTANCE.getHash(from))
						.setTo(ConsistentHash.INSTANCE.getHash(to))
						.build())
				.build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}


	/**
	 * Override the equals method, which will be used to compare two nodes according to their toString <ip:port>
	 * @param obj
	 * @return
	 */
	// TODO
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof INode) {
			INode node = (INode) obj;
			return node.toString().equals(this.toString());
		}
		return false;
	}

	/**
	 * Print the node in the format of string <ip:port>, which will be used as the key in the Hash ring
	 * @return <ip:port> string
	 */
	@Override
	public String toString() {
		return host + ":" + port;
	}

	@Override
	public void toString(com.google.protobuf.Empty request,
						 io.grpc.stub.StreamObserver<de.tum.grpc_api.KVServerProto.ToStringResponse> responseObserver) {
		KVServerProto.ToStringResponse response = KVServerProto.ToStringResponse.newBuilder()
				.setHostPort(host + ":" + port)
				.build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	/**
	 * Check if this node is responsible for the given (key, value) pair
	 * @param key
	 * @return true if this node is responsible for the given (key, value) pair
	 */
	// isResponsible, copy, get, put, delete will only be called by other KVServer
	public boolean isResponsible(String key) throws NullPointerException {
		if (ConsistentHash.INSTANCE.getRing().size() == 1) {
			return true;
		}
		String keyHash = MD5Hash.hash(key);	//get hash of key

		String currNodeHash = ConsistentHash.INSTANCE.getHash(this);	//get hash of this node

		SortedMap<String, INode> tailMap = ConsistentHash.INSTANCE.getRing().tailMap(keyHash);
		if (tailMap.isEmpty()) {
			return ConsistentHash.INSTANCE.getRing().firstKey().equals(currNodeHash);
		} else {
			return tailMap.firstKey().equals(currNodeHash);
		}
	}

	@Override
	public void isResponsible(de.tum.grpc_api.KVServerProto.IsResponsibleRequest request,
							  io.grpc.stub.StreamObserver<de.tum.grpc_api.KVServerProto.IsResponsibleResponse> responseObserver) {
		String key = request.getKey();
		boolean result = isResponsible(key);

		KVServerProto.IsResponsibleResponse response = KVServerProto.IsResponsibleResponse.newBuilder()
				.setIsResponsible(result)
				.build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	/**
	 * Get data from another node to this node
	 *
	 * @param where
	 * @param range
	 * @return the data that in this range
	 */
	public HashMap<String, String> copy(DataType where, Range range) throws Exception {
		IDatabase database = where == DataType.DATA ? mainDatabase : backupDatabase;
		return database.getDataByRange(range);
	}

	@Override
	public void copy(de.tum.grpc_api.KVServerProto.CopyRequest request,
					 io.grpc.stub.StreamObserver<de.tum.grpc_api.KVServerProto.CopyResponse> responseObserver) {
		IDatabase database = request.getWhere() == KVServerProto.DataType.DATA ? mainDatabase : backupDatabase;
		KVServerProto.CopyResponse response;
		try {
			Range range = new Range(request.getRange().getFrom(), request.getRange().getTo());
			HashMap<String, String> data = database.getDataByRange(range);
			response = KVServerProto.CopyResponse.newBuilder()
					.putAllData(data)
					.build();
//			response = KVServerProto.CopyResponse.newBuilder()
//					.putAllData(database.getDataByRange(new Range(request.getRange().getFrom(), request.getRange().getTo())))
//					.build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
	public String get(String key) throws Exception {
		return mainDatabase.get(key);
	}

	@Override
	public void get(de.tum.grpc_api.KVServerProto.GetRequest request,
					   io.grpc.stub.StreamObserver<de.tum.grpc_api.KVServerProto.GetResponse> responseObserver) {
		String key = request.getKey();
		KVServerProto.GetResponse response;
		try {
			response = KVServerProto.GetResponse.newBuilder()
					.setValue(mainDatabase.get(key))
					.build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	public void put(String key, String value) throws Exception {
		mainDatabase.put(key, value);
		System.out.println("Put data on database " + this.port + " <" + key + ":" + value + ">");
	}

	@Override
	public void put(de.tum.grpc_api.KVServerProto.PutRequest request,
					   io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
		String key = request.getKey();
		String value = request.getValue();

		try {
			put(key, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Empty response = Empty.newBuilder().build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	public void putBackup(String key, String value) throws Exception {
		backupDatabase.put(key, value);
		System.out.println("Put backup data on database " + this.port + " <" + key + ":" + value + ">");
	}

	@Override
	public void putBackup(de.tum.grpc_api.KVServerProto.PutBackupRequest request,
						  io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
		String key = request.getKey();
		String value = request.getValue();
		try {
			putBackup(key, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Empty response = Empty.newBuilder().build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}


	public void delete(String key) throws Exception {
		mainDatabase.delete(key);
		System.out.println("Delete data on database " + this.port + ": " + key );
	}

	@Override
	public void delete(de.tum.grpc_api.KVServerProto.DeleteRequest request,
						  io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
		String key = request.getKey();
		try {
			delete(key);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Empty response = Empty.newBuilder().build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	public boolean hasKey(String key) throws Exception {
		return mainDatabase.hasKey(key);
	}

	public void hasKey(de.tum.grpc_api.KVServerProto.HasKeyRequest request,
					   io.grpc.stub.StreamObserver<de.tum.grpc_api.KVServerProto.HasKeyResponse> responseObserver) {
		String key = request.getKey();
		KVServerProto.HasKeyResponse response;
		try {
			response = KVServerProto.HasKeyResponse.newBuilder()
					.setHasKey(mainDatabase.hasKey(key))
					.build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	private List<String> executeTransactions(List<String> localCommands, String transactionId) throws Exception {
		return server.executeTransactions(localCommands, transactionId);
	}


	// init, recover, updateRing, deleteExpiredData will only be called by ECS
	public void init() throws Exception {
        // Data transfer
		System.out.println("Start Data Transfer");
		if (ConsistentHash.INSTANCE.getRing().size() != 1) {
			INode nextNode = ConsistentHash.INSTANCE.getNextNode(this);
			INode previousNode = ConsistentHash.INSTANCE.getPreviousNode(this);

			HashMap<String, String> mainData = nextNode.copy(DataType.DATA, getRange(DataType.DATA));
			mainDatabase.saveAllData(mainData);

			HashMap<String, String> backup = previousNode.copy(DataType.BACKUP, getRange(DataType.BACKUP));
			backupDatabase.saveAllData(backup);
//			mainDatabase.saveAllData(nextNode.copy(DataType.DATA, getRange(DataType.DATA)));
//			backupDatabase.saveAllData(previousNode.copy(DataType.BACKUP, getRange(DataType.BACKUP)));
		}
		// Start KVServer
		System.out.println("Start KVServer: " + this.host + ":" + this.port);
	}

	@Override
	public void init(com.google.protobuf.Empty request,
					 io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver){
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Empty response = Empty.newBuilder().build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	public void startKVServer() throws Exception {
		server = new KVServer(this);
		server.start(host, port);
	}

	@Override
	public void startKVServer(com.google.protobuf.Empty request,
							  io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
		try {
			startKVServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Empty response = Empty.newBuilder().build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}


	// TODO: Exception
	public void recover(NodeProxy removedNode) throws Exception {

		String removedHash = ConsistentHash.INSTANCE.getHash(removedNode);
		removedNode.closeRpcChannel();

		// recover data from the removed node
		// If the removed node is the previous node of this node
		INode previousNode = ConsistentHash.INSTANCE.getPreviousNode(this);
		if (MD5Hash.hash(previousNode.getHost() + ":" + previousNode.getPort()).equals(removedHash)) {
			INode newPreviousNode = ConsistentHash.INSTANCE.getPreviousNode(removedNode);
			Range dataRangeOfRemovedNode = new Range(ConsistentHash.INSTANCE.getHash(newPreviousNode), removedHash);
//			try {
//				removedNode.heartbeat(); // check whether the removed node is alive
//				mainDatabase.saveAllData(newPreviousNode.copy(DataType.DATA, dataRangeOfRemovedNode));
//			}
//			catch (Exception e) {
//				System.out.println("Node " + removedNode + " is dead");
//				// recover data from the backup
//				mainDatabase.saveAllData(newPreviousNode.copy(DataType.BACKUP, dataRangeOfRemovedNode));
//			}
			// if work flow goes here, it means the removed node is dead, and we should also close
			// form this node to the removed node
			mainDatabase.saveAllData(newPreviousNode.copy(DataType.BACKUP, dataRangeOfRemovedNode));
		}

		// recover backup from the removed node
		// If the removed node is the next node of this node
		INode nextNode = ConsistentHash.INSTANCE.getNextNode(this);
		if (MD5Hash.hash(nextNode.getHost() + ":" + nextNode.getPort()).equals(removedHash)) {
			INode newNextNode = ConsistentHash.INSTANCE.getNextNode(removedNode);
			Range backupRangeOfRemovedNode = new Range(removedHash, ConsistentHash.INSTANCE.getHash(newNextNode));
//			try {
//				removedNode.heartbeat(); // check whether the removed node is alive
//				backupDatabase.saveAllData(newNextNode.copy(DataType.BACKUP, backupRangeOfRemovedNode));
//			}
//			catch (Exception e) {
//				System.out.println("Node " + removedNode + " is dead");
//				// recover data from the backup
//				backupDatabase.saveAllData(newNextNode.copy(DataType.DATA, backupRangeOfRemovedNode));
//			}
			backupDatabase.saveAllData(newNextNode.copy(DataType.DATA, backupRangeOfRemovedNode));
		}
	}

	@Override
	public void recover(de.tum.grpc_api.KVServerProto.RecoverRequest request,
						io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
		KVServerProto.NodeMessage nodeProto = request.getNode();
		NodeProxy nodeProxy = new NodeProxy(nodeProto.getHost(), nodeProto.getRpcPort(), nodeProto.getPortForClient());
		try {
			recover(nodeProxy);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Empty response = Empty.newBuilder().build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	public void updateRing(SortedMap<String, INode> ring) {
		ConsistentHash.INSTANCE.update(ring);
	}

	@Override
	public void updateRing(de.tum.grpc_api.KVServerProto.UpdateRingRequest request,
						   io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
		Map<String, KVServerProto.NodeMessage> ringNodeMessage = request.getRingMap();
		TreeMap<String, INode> ring = new TreeMap<>();

		// Process each entry in the map
		// TODO: NEEDS TO BE IMPROVED
		for (Map.Entry<String, KVServerProto.NodeMessage> entry : ringNodeMessage.entrySet()) {
			String key = entry.getKey();
			KVServerProto.NodeMessage nodeMessage = entry.getValue();
			String host = nodeMessage.getHost();
			int rpcPort = nodeMessage.getRpcPort();
			int portForClient = nodeMessage.getPortForClient();
			if (key == MD5Hash.hash(this.host + ":" + this.port)) {
				ring.put(key, new Node(host, portForClient, this.mainDatabase, this.backupDatabase));
			}
			ring.put(key, new NodeProxy(host, rpcPort, portForClient));
//			System.out.println("Key: " + key + ", Host: " + host + ", Port: " + this.port);
		}
		System.out.println("Update ring: " + ring);
		updateRing(ring);
		responseObserver.onNext(Empty.newBuilder().build());
		responseObserver.onCompleted();
	}

	public void deleteExpiredData(DataType dataType, Range range) throws Exception {
		IDatabase database = dataType == DataType.DATA ? mainDatabase : backupDatabase;
		database.deleteDataByRange(range);
	}

	@Override
	public void deleteExpiredData(de.tum.grpc_api.KVServerProto.DeleteExpiredDataRequest request,
								  io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
		KVServerProto.DataType dataTypeProto = request.getDataType();
		DataType dataType = dataTypeProto == KVServerProto.DataType.DATA ? DataType.DATA : DataType.BACKUP;
		KVServerProto.Range rangeProto = request.getRange();
		Range range = new Range(rangeProto.getFrom(), rangeProto.getTo());

		try {
			deleteExpiredData(dataType, range);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		Empty response = Empty.newBuilder().build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}
