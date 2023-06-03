package de.tum.communication;

import io.grpc.Server;
import io.grpc.ServerBuilder;

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

    public ECSServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        Server server = ServerBuilder.forPort(port)
                .addService(new Registry())
                .build()
                .start();
        System.out.println("grpc server start!");
    }

    public void blockUntilShutdown() throws InterruptedException {
//        if (server != null) {
//            server.awaitTermination();
//        }
    }
}
