package de.tum.communication;

import com.google.protobuf.Empty;
import de.tum.grpc_api.ECSProto;
import de.tum.grpc_api.ECServiceGrpc;
import de.tum.grpc_api.KVServiceGrpc;
import de.tum.node.Node;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName: ECSServer
 * Package: de.tum.communication
 * Description:
 *
 * @Author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @Create 2023/6/8 13:37
 * @Version 1.0
 */

public class ECSServer {
    private int port;
    private String address;
    private Server ecsServer;
    private ExecutorService executorService;

    public ECSServer(String address, int port) {
        this.port = port;
        this.address = address;
        this.executorService = Executors.newCachedThreadPool();
    }

    public void start(boolean helpUsage) throws IOException, InterruptedException {
        if (helpUsage) Help.helpDisplay();
        executorService = Executors.newFixedThreadPool(15);
        // Start ECS as an RCP server
        SocketAddress IpPort = new InetSocketAddress(this.address, this.port);
        ecsServer = NettyServerBuilder.forAddress(IpPort).addService(new ECSServiceImpl()).build();
        ecsServer.start();
        System.out.println("Server is listening on port " + port);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("*** Shutting down ECS server since JVM is shutting down ***");
                ECSServer.this.stop();
                System.err.println("*** ECS server shut down ***");
            }
        });

//        ecsServer.awaitTermination();
    }

    public void stop() {
        if (ecsServer != null) {
            ecsServer.shutdown();
        }
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (ecsServer != null) {
            ecsServer.awaitTermination();
        }
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    public class ECSServiceImpl extends ECServiceGrpc.ECServiceImplBase {
        public static final int KV_LISTEN_ECS_PORT = 5200;
        @Override
        public void register(de.tum.grpc_api.ECSProto.RegisterRequest request,
                             io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
            //ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                System.out.println("test register starts");
                String Host = request.getNode().getHost();
                int Port = request.getNode().getPort();
                int rpcPort = request.getRpcPort();
                System.out.println(
                        "ECS receive register request form KVServer: <" + Host + ":" + Port + ">");
                // Build and send the response

                Empty response = Empty.newBuilder().build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();

                System.out.println("start init");


                // new Thread?
//                ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(Host, KV_LISTEN_ECS_PORT).usePlaintext().build();
//                KVServiceGrpc.KVServiceBlockingStub kvServiceStub = KVServiceGrpc.newBlockingStub(managedChannel);
                Node node = new Node(Host, rpcPort);
                try {
                    //ConsistentHash.INSTANCE.addNode(nodeStub);
                    // 心跳
//                    while (nodeStub.heartbeat()) {
//                        Thread.sleep(1000);
//                    }
//                    removeNode(nodeStub);
                    //System.out.println(node.heartbeat());
                    Thread.sleep(20000);
                    System.out.println(node.get("1"));
//                    managedChannel.shutdown();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}