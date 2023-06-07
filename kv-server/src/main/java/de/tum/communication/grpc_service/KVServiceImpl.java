import de.tum.grpc_api.*;
package de.tum.communication.grpc_service;

public class KVServiceImpl extends KVServiceGrpc.KVServiceImplBase {
	@Override
	public void init(com.google.protobuf.Empty request,
		io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {

		google.protobuf.Empty response = google.protobuf.Empty.newBuilder().build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	@Override
	public void updateRing(com.google.protobuf.Empty request,
		io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {

		google.protobuf.Empty response = google.protobuf.Empty.newBuilder().build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}




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
}
