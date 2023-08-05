package de.tum.communication;

import com.google.protobuf.Empty;
import de.tum.grpc_api.ECSProto;
import de.tum.grpc_api.ECServiceGrpc;
import de.tum.grpc_api.KVServiceGrpc;
import de.tum.node.ConsistentHash;
import de.tum.node.NodeProxy;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import org.w3c.dom.Node;

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

    public void start(boolean helpUsage) throws IOException {
        if (helpUsage) Help.helpDisplay();
        executorService = Executors.newFixedThreadPool(15);
        // Start ECS as an RCP server
        SocketAddress IpPort = new InetSocketAddress(this.address, this.port);
        ecsServer = NettyServerBuilder.forAddress(IpPort).addService(new ECSServiceImpl()).build();
        ecsServer.start();
//        System.out.println("ECS is listening on port " + port);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("*** Shutting down ECS server since JVM is shutting down ***");
                ECSServer.this.stop();
                System.err.println("*** ECS server shut down ***");
            }
        });
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
//                System.out.println("test register starts");
                String host = request.getNode().getHost();
                int rpcPort = request.getRpcPort();
                int portForClient = request.getNode().getPortForClient();
//                System.out.println(
//                        "ECS receive register request form KVServer<" + host + ":" + portForClient + ">");

                Empty response = Empty.newBuilder().build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();

                System.out.println("Adding new KVServer<" + host + ":" + portForClient + "> to consistent hash ring");

                // new Thread?
                NodeProxy node = new NodeProxy(host, rpcPort, portForClient);
                try {
//                    System.out.println(node.heartbeat());
                    ConsistentHash.INSTANCE.addNode(node);
//                } catch (io.grpc.StatusRuntimeException e) {
                } catch (Exception e) {
                    // TODO: shutdown when 2 KVServers are running
                    ConsistentHash.INSTANCE.removeNode(node);
                }
            });
        }
    }
}