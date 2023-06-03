package de.tum.communication;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import de.tum.communication.grpcService.ECSServiceImpl;

import java.io.IOException;

/**
 * ClassName: ECSServer
 * Package: de.tum.communication
 * Description:
 *
 * @Author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @Create 2023/6/2 23:29
 * @Version 1.0
 */
public class ECSServer {
    private int port;

    public static void ECSServer(String[] args) throws IOException, InterruptedException {
        // 1. 绑定端口
        ServerBuilder serverBuilder = ServerBuilder.forPort(9000);
        // 2. 发布服务
        serverBuilder.addService(new ECSServiceImpl());
        // 3. 创建服务对象
        Server server = serverBuilder.build();
        // 4. 启动服务
        server.start();
        server.awaitTermination();
    }
}
