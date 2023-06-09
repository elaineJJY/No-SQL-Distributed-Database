package de.tum.communication.grpc_service;

import de.tum.grpc_api.*;
import com.google.protobuf.Empty;

import javax.sound.sampled.Port;


//public class KVServiceImpl extends KVServiceGrpc.KVServiceImplBase {
//	@Override
//	public void init(com.google.protobuf.Empty request,
//		io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
//
//		google.protobuf.Empty response = google.protobuf.Empty.newBuilder().build();
//		responseObserver.onNext(response);
//		responseObserver.onCompleted();
//	}
//
//	@Override
//	public void updateRing(com.google.protobuf.Empty request,
//		io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
//
//		google.protobuf.Empty response = google.protobuf.Empty.newBuilder().build();
//		responseObserver.onNext(response);
//		responseObserver.onCompleted();
//	}
//}

////public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
//public class KVServiceImpl extends KVServiceGrpc.KVServiceImplBase {
//	@Override
//	public void getPort(com.google.protobuf.Empty request,
//						io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
//		System.out.println("test getPort");
//
//		Empty response = Empty.newBuilder().build();
//		responseObserver.onNext(response);
//		responseObserver.onCompleted();
//	}
//
//	@Override
//	public void getHost(com.google.protobuf.Empty request,
//						io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
//		System.out.println("test getHost");
//
//		Empty response = Empty.newBuilder().build();
//		responseObserver.onNext(response);
//		responseObserver.onCompleted();
//	}
//
//	@Override
//	public void heartBeat(com.google.protobuf.Empty request,
//						io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
//		System.out.println("test heartBeat");
//
//		Empty response = Empty.newBuilder().build();
//		responseObserver.onNext(response);
//		responseObserver.onCompleted();
//	}
//
//	@Override
//	public void getRange(com.google.protobuf.Empty request,
//						  io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
//		System.out.println("test getRange");
//
//		Empty response = Empty.newBuilder().build();
//		responseObserver.onNext(response);
//		responseObserver.onCompleted();
//	}
//
//	@Override
//	public void equals(com.google.protobuf.Empty request,
//						  io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
//		System.out.println("test equals");
//
//		Empty response = Empty.newBuilder().build();
//		responseObserver.onNext(response);
//		responseObserver.onCompleted();
//	}
//
//
//	@Override
//	public void initRPC(com.google.protobuf.Empty request,
//		io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
//		System.out.println("test init");
//
//		Empty response = Empty.newBuilder().build();
//		responseObserver.onNext(response);
//		responseObserver.onCompleted();
//	}
//
//	@Override
//	public void updateRing(com.google.protobuf.Empty request,
//		io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
//		System.out.println("test updateRing");
//
//		Empty response = Empty.newBuilder().build();
//		responseObserver.onNext(response);
//		responseObserver.onCompleted();
//	}
//}



//	public void register(ECSProto.RegisterRequest request,
//		StreamObserver<ECSProto.RegisterResponse> responseObserver) {
//		// 1. 接受client的参数
//		String Host = request.getIpPort().getHost();
//		int Port = request.getIpPort().getPort();
//		// 2. 业务处理
//		System.out.println(
//			"ECS receive register request form KVServer:<" + Host + ":" + Port + ">");
//		Node node = new Node(Host, Port); // Stub from KVServer
//		ConsistentHash.INSTANCE.addNode(node);
//		// 3. 封装响应
//		// 3.1 构建响应对象
//		ECSProto.RegisterResponse.Builder builder = ECSProto.RegisterResponse.newBuilder();
//		// 3.2 填充数据
//		// put the hash value of the node into the response
//		ECSProto.NodeMessage nodeMessage = ECSProto.NodeMessage.newBuilder()
//			.setHost(Host)
//			.setPort(Port)
//			.build();
//
//		// 3.3 封装响应
//		ECSProto.RegisterResponse registerResponse = builder.putRing(MD5Hash.hash(node.toString()),
//			nodeMessage).build();
//		responseObserver.onNext(registerResponse); // 处理后的响应通过网络回传给client
//		responseObserver.onCompleted(); // 通知client 响应已经结束了，会返回一个标志，client接收到这个标志后，会结束这次rpc调用
//	}

