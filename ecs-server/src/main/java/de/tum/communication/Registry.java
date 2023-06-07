package de.tum.communication;
import io.grpc.BindableService;
import io.grpc.ServerMethodDefinition;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.StreamObserver;
import jdk.jshell.spi.ExecutionControl;

import java.util.HashMap;

/**
 * ClassName: Registry
 * Package: de.tum.service
 * Description:
 *
 * @Author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @Create 2023/5/28 11:35
 * @Version 1.0
 */
public class Registry implements BindableService {  // extends TestGRPCGrpc.TestGRPCImplBase
    @Override
    public ServerServiceDefinition bindService(){
        return null;
    }
//    @Override
//    public void getVersion(TestProto.GetVersionRequest request, StreamObserver<TestProto.GetVersionResponse> responseObserver) {
//        System.out.println("请求信息" + request);
//        TestProto.GetVersionResponse reply = TestProto.GetVersionResponse.newBuilder()
//                .setResponse(
//                        TestProto.BaseResponse.newBuilder()
//                                .setMessage("success").setCode(0))
//                .setVersion(
//                        TestProto.Version.newBuilder()
//                                .setSoftwareVersion("1.0.0")
//                                .setLastCompileTime(System.currentTimeMillis()))
//                .build();
//        responseObserver.onNext(reply);
//        responseObserver.onCompleted();
//        System.out.println("发送版本信息完成！");
//    }

}
