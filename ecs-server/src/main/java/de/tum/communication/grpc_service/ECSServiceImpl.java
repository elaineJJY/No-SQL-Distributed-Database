package de.tum.communication.grpc_service;

import io.grpc.stub.StreamObserver;

public class ECSServiceImpl {
	// 1. 接受client的参数
	// 2. 业务处理 service+dao 调用对应的业务功能
	// 3. 提供返回值
	@Override
	public void hello(ECSProto.HelloRequest request, StreamObserver<HelloResponse> responseStreamObserver) {
		// 1. 接受client的参数
		String name = request.getName();
		// 2. 业务处理
		System.out.println("接受到客户端信息:" + name);
		// 3. 封装响应
		// 3.1 构建响应对象
		ECSProto.HelloResponse.Builder builder = ECSProto.HelloResponse.newBuilder();
		// 3.2 填充数据
		builder.setResult("hello method invoke ok");
		// 3.3 封装响应
		ECSProto.HelloResponse helloResponse = builder.build();
		// 3.4
		responseStreamObserver.onNext(helloResponse);
		responseStreamObserver.onCompleted();
	}
}
