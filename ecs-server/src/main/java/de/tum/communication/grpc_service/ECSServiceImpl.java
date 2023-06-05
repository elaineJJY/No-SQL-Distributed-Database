package de.tum.communication.grpc_service;

import grpc_api.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;

/**
 * ClassName: ECSServiceImpl
 * Package: de.tum.communication.grpc_service
 * Description: Extended from HelloServiceImpl.java and implements service api
 *
 * @Author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @Create 2023/06/05 16:46
 * @Version 1.0
 */

public class ECSServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
	@Override
	public void hello(grpc_api.ECSProto.HelloRequest request,
					  StreamObserver<grpc_api.ECSProto.HelloResponse> responseObserver) {
		// 1. 接受client的参数
		String name = request.getName();
		// 2. 业务处理
		System.out.println("接受到客户端信息:" + name);
		// 3. 封装响应
		// 3.1 构建响应对象
		grpc_api.ECSProto.HelloResponse.Builder builder = grpc_api.ECSProto.HelloResponse.newBuilder();
		// 3.2 填充数据
		builder.setResult("hello method invoke ok");
		// 3.3 封装响应
		grpc_api.ECSProto.HelloResponse helloResponse = builder.build();
		// 3.4
		responseObserver.onNext(helloResponse); // 处理后的响应通过网络回传给client
		responseObserver.onCompleted(); // 通知client 响应已经结束了，会返回一个标志，client接收到这个标志后，会结束这次rpc调用
	}
}
