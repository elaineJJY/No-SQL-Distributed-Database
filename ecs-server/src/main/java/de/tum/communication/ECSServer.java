package de.tum.communication;

import de.tum.common.Help;
import de.tum.node.ConsistentHash;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ExecutorService;
import java.nio.channels.SocketChannel;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.LinkedList;

import de.tum.node.Node;


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
    private static final Logger LOGGER = ServerLogger.INSTANCE.getLogger();
    private final int ecsPort;
    private final String ecsAddress;

    private static ExecutorService executorService;
    public static ServerSocket ecsServerSocket;
    private LinkedList<Socket> closeQueue; // Used when close the queue to transfer data gradually

    public ECSServer(String address, int port) {
        this.ecsPort = port;
        this.ecsAddress = address;
        this.executorService = Executors.newFixedThreadPool(15);
    }

    public int getPort () { return this.ecsPort; }

    public String getAddress() { return this.ecsAddress; }

    public void start(boolean helpUsage) throws Exception {
        if (helpUsage) Help.helpDisplay();
        // Start ECS as a server for registering KVServer
        InetSocketAddress socketAddress = new InetSocketAddress(this.ecsAddress, this.ecsPort);
        ecsServerSocket = new ServerSocket();
        ecsServerSocket.bind(socketAddress);
        System.out.println("ECS is listening on port " + ecsPort);

        Runtime.getRuntime().addShutdownHook(new Thread(ECSServer::shutdown));

        while (true) {
            // accept socket from KVServer
            Socket clientSocket = ecsServerSocket.accept();
//            String message = "ECS starts adding KVServer<" + remoteAddress + ":" + remotePort + "> to ring";
            byte[] buffer = new byte[1024];
            int bytesRead = clientSocket.getInputStream().read(buffer);
            byte[] address = new byte[bytesRead];
            System.arraycopy(buffer, 0, address, 0, bytesRead);

            String addressString = new String(address);
            String remoteAddress = addressString.split(":")[0];
            int remotePort = Integer.parseInt(addressString.split(":")[1]);

//            String remoteAddress = String.valueOf(clientSocket.getInetAddress());
//            int remotePort = clientSocket.getPort();
            LOGGER.info("Accept new KVServer <" + remoteAddress + ":" + remotePort + ">");

            // Start to connect to corresponded KVServer
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(remoteAddress, remotePort));
            socketChannel.configureBlocking(false);
            Node node = new Node(remoteAddress, remotePort, socketChannel);
            ConsistentHash.INSTANCE.addNode(node);
            readKVServer(node);
        }
    }

    // Read message from KVServer (Using NIO)
    private void readKVServer(Node node) throws Exception {
        executorService.execute(() -> {
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            readBuffer.clear(); // clear buffer
            try {
                int len = node.getSocketChannel().read(readBuffer);

                if (len != -1) {
                    node.updateRing(ConsistentHash.INSTANCE.getRing());
                } else {
                    // Connected KVServer is lost
                    node.getSocketChannel().close(); // close channel
                    ConsistentHash.INSTANCE.removeNode(node);
//            key.cancel(); // cancel key
                }
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

//    private void clientHandler(Node node) {
//        executorService.execute(() -> {
//            try {
//
//
//                String message;
//                while ((message = reader.readLine()) != null) {
//                    System.out.println("Received message from client: " + message);
//                    node.updateRing(ConsistentHash.INSTANCE.getRing());
//                    writer.flush();
//                }
//                // Connected KVServer is lost
//                ConsistentHash.INSTANCE.removeNode(node);
//                node.getClientSocket().close();
//            }
//            catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }

    private static void shutdown() {
        // close thread pool
        executorService.shutdown();

        try {
            // 等待所有线程完成任务的最长时间
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ECS shutdowns complete.");
    }
}