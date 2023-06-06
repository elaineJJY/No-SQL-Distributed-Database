package de.tum.communication.grpc_service;

import de.tum.node.ConsistentHash;
import de.tum.node.MD5Hash;
import grpc_api.ECSProto;
import io.grpc.stub.StreamObserver;
import de.tum.node.Node;

/**
 * ClassName: ECSServiceImpl
 * Package: de.tum.communication.grpc_service
 * Description: Extended from HelloServiceImpl.java and implements service api
 *
 * @Author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @Create 2023/06/05 16:46
 * @Version 1.0
 */

public class ECSServiceImpl extends grpc_api.ECSServiceGrpc.ECSServiceImplBase {
	@Override
	public void init(grpc_api.ECSProto.InitRequest request,
					  StreamObserver<grpc_api.ECSProto.InitResponse> responseObserver) {
		// 1. 接受client的参数
		String Host = request.getIpPort().getHost();
		int Port = request.getIpPort().getPort();
		// 2. 业务处理
		System.out.println("ECS receive register request form KVServer:<" + Host + ":" + Port + ">");
		Node node = new Node(Host, Port);
		ConsistentHash.INSTANCE.addNode(node);
		// 3. 封装响应
		// 3.1 构建响应对象
		ECSProto.InitResponse.Builder builder = grpc_api.ECSProto.InitResponse.newBuilder();
		// 3.2 填充数据
		// put the hash value of the node into the response
		ECSProto.NodeMessage nodeMessage = ECSProto.NodeMessage.newBuilder()
				.setHost(Host)
				.setPort(Port)
				.build();

		// 3.3 封装响应
		ECSProto.InitResponse initResponse = builder.putRing(MD5Hash.hash(node.toString()), nodeMessage).build();
		// 3.4
		responseObserver.onNext(initResponse); // 处理后的响应通过网络回传给client
		responseObserver.onCompleted(); // 通知client 响应已经结束了，会返回一个标志，client接收到这个标志后，会结束这次rpc调用
	}
}
