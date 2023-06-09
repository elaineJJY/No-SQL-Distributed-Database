package de.tum.node;


import com.google.protobuf.Empty;
import de.tum.communication.KVServer;
import de.tum.database.BackupDatabase;
import de.tum.database.IDatabase;
import de.tum.database.MainDatabase;
import de.tum.grpc_api.KVServerProto;
import de.tum.grpc_api.KVServiceGrpc;
import io.grpc.ManagedChannelBuilder;

import java.io.Serializable;
import java.util.*;

public class Node extends KVServiceGrpc.KVServiceImplBase implements Serializable {

	private KVServer server;
	private String host;
	private int port;
	private IDatabase mainDatabase;
	private IDatabase backupDatabase;
	private final KVServiceGrpc.KVServiceBlockingStub stub;

	public Node(String host, int port, IDatabase mainDatabase, IDatabase backupDatabase){
		this.host = host;
		this.port = port;
		this.mainDatabase = mainDatabase;
		this.backupDatabase = backupDatabase;
		this.stub = KVServiceGrpc.newBlockingStub(ManagedChannelBuilder.forAddress(host, port).usePlaintext().build());
	}

	public int getPort() { return port; }

	public String getHost() { return host; }

	public long heartbeat() { return new Date().getTime(); }

	//TODO: heartBeatRPC
	@Override
	public void heartBeatRPC(com.google.protobuf.Empty request,
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
	public Range getRangeLocal(DataType dataType) {
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

	/**
	 * Override the equals method, which will be used to compare two nodes according to their toString <ip:port>
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Node) {
			Node node = (Node) obj;
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
	public void toStringRPC(com.google.protobuf.Empty request,
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
	public boolean isResponsibleLocal(String key) throws NullPointerException {
		String keyHash = ConsistentHash.INSTANCE.getHash(this);
		Node prevNode = ConsistentHash.INSTANCE.getPreviousNode(this);
		String prevHash = ConsistentHash.INSTANCE.getHash(prevNode);
		return (keyHash.compareTo(key) >= 0 && prevHash.compareTo(key) < 0);
	}

	@Override
	public void isResponsibleRPC(de.tum.grpc_api.KVServerProto.IsResponsibleRequest request,
								 io.grpc.stub.StreamObserver<de.tum.grpc_api.KVServerProto.IsResponsibleResponse> responseObserver) {
		String key = request.getKey();
		boolean result = isResponsibleLocal(key);
		KVServerProto.IsResponsibleResponse response = KVServerProto.IsResponsibleResponse.newBuilder()
				.setIsResponsible(result)
				.build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	public void init() throws Exception {
		if (!ConsistentHash.INSTANCE.getRing().isEmpty()) {
			Node nextNode = ConsistentHash.INSTANCE.getNextNode(this);
			Node previousNode = ConsistentHash.INSTANCE.getPreviousNode(this);
			mainDatabase.saveAllData(nextNode.copy(DataType.DATA, getRangeLocal(DataType.DATA)));
			backupDatabase.saveAllData(previousNode.copy(DataType.BACKUP, getRangeLocal(DataType.BACKUP)));
			server = new KVServer(this);
		}
	}

	@Override
	public void initRPC(com.google.protobuf.Empty request,
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

	public void recover(Node removedNode) throws Exception {

		String removedHash = ConsistentHash.INSTANCE.getHash(removedNode);

		// recover data from the removed node
		if (ConsistentHash.INSTANCE.getPreviousNode(this).equals(removedNode)) {
			Node newPreviousNode = ConsistentHash.INSTANCE.getPreviousNode(removedNode);
			Range dataRangeOfRemovedNode = new Range(ConsistentHash.INSTANCE.getHash(newPreviousNode), removedHash);
			try {
				removedNode.heartbeat(); // check whether the removed node is alive
				mainDatabase.saveAllData(newPreviousNode.copy(DataType.DATA, dataRangeOfRemovedNode));
			}
			catch (Exception e) {
				System.out.println("Node " + removedNode + " is dead");
				// recover data from the backup
				mainDatabase.saveAllData(newPreviousNode.copy(DataType.BACKUP, dataRangeOfRemovedNode));
			}
		}
		// recover backup from the removed node
		if (ConsistentHash.INSTANCE.getNextNode(this).equals(removedNode)) {
			Node newNextNode = ConsistentHash.INSTANCE.getNextNode(removedNode);
			Range backupRangeOfRemovedNode = new Range(removedHash, ConsistentHash.INSTANCE.getHash(newNextNode));
			try {
				removedNode.heartbeat(); // check whether the removed node is alive
				backupDatabase.saveAllData(newNextNode.copy(DataType.BACKUP, backupRangeOfRemovedNode));
			}
			catch (Exception e) {
				System.out.println("Node " + removedNode + " is dead");
				// recover data from the backup
				backupDatabase.saveAllData(newNextNode.copy(DataType.DATA, backupRangeOfRemovedNode));
			}
		}
	}

	public void updateRingLocal(SortedMap<String, Node> ring) {
		ConsistentHash.INSTANCE.update(ring);
	}

	@Override
	public void updateRingRPC(de.tum.grpc_api.KVServerProto.UpdateRingRequest request,
							  io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
		Map<String, KVServerProto.NodeMessage> ringNodeMessage = request.getRingMap();
		TreeMap<String, Node> ring = new TreeMap<>();

//		// Process each entry in the map
//		// TODO: NEEDS TO BE IMPROVED
//		for (Map.Entry<String, KVServerProto.NodeMessage> entry : ringNodeMessage.entrySet()) {
//			String key = entry.getKey();
//			KVServerProto.NodeMessage nodeMessaage = entry.getValue();
//			String host = nodeMessaage.getHost();
//			int port = nodeMessaage.getPort();
//			ring.put(key, new Node(host, port));
//
//			// Do something with the key, host, and port
//			System.out.println("Key: " + key + ", Host: " + host + ", Port: " + port);
//		}
//		System.out.println("Update ring: " + ring);
//		updateRingLocal(ring);
//		responseObserver.onNext(Empty.newBuilder().build());
//		responseObserver.onCompleted();
	}

	public void deleteExpiredData(DataType dataType, Range range) throws Exception {
		IDatabase database = dataType == DataType.DATA ? mainDatabase : backupDatabase;
		database.deleteDataByRange(range);
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

	public String get(String key) throws Exception {
		return mainDatabase.get(key);
	}
	public void put(String key, String value) throws Exception {
		mainDatabase.put(key, value);
	}
	public void delete(String key) throws Exception {
		mainDatabase.delete(key);
	}

	public void getRPC(de.tum.grpc_api.KVServerProto.GetRequest request,
					   io.grpc.stub.StreamObserver<de.tum.grpc_api.KVServerProto.GetResponse> responseObserver) {
		String key = request.getKey();
		KVServerProto.GetResponse response = null;
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
	public void putRPC(de.tum.grpc_api.KVServerProto.PutRequest request,
					   io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
		String key = request.getKey();
		String value = request.getValue();

		try {
			mainDatabase.put(key, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Empty response = Empty.newBuilder().build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
	public void deleteRPC(de.tum.grpc_api.KVServerProto.DeleteRequest request,
						  io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
		String key = request.getKey();
		try {
			mainDatabase.delete(key);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Empty response = Empty.newBuilder().build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}
