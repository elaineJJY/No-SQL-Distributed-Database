package de.tum.communication;

import de.tum.common.ECSMessageBuilder;
import de.tum.common.Help;
import de.tum.node.ConsistentHash;

import java.io.OutputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.nio.channels.SocketChannel;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.LinkedList;

import de.tum.node.Node;

import static java.lang.Thread.sleep;


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
    private LinkedList<Node> closeQueue = new LinkedList<>(); // Used when close the queue to transfer data gradually
    private HashMap<String, Node> nodeMap = new HashMap<>();// Store all exist nodes: <ip:port, Node>
    private HashMap<Node, Socket> nodeSocketMap = new HashMap<>(); // store all exist nodes: <Node, Socket>

    public ECSServer(String address, int port) {
        this.ecsPort = port;
        this.ecsAddress = address;
        this.executorService = Executors.newFixedThreadPool(15);
    }

    public int getEcsPort () { return this.ecsPort; }

    public String getEcsAddress() { return this.ecsAddress; }

    /**
     * Start ECS server
     * @param helpUsage
     * @throws Exception
     */
    public void start(boolean helpUsage) throws Exception {
        if (helpUsage) Help.helpDisplay();
        // Start ECS as a server for registering KVServer
        InetSocketAddress socketAddress = new InetSocketAddress(this.ecsAddress, this.ecsPort);
        ecsServerSocket = new ServerSocket();
        ecsServerSocket.bind(socketAddress);
        System.out.println("ECS is listening on port " + ecsPort);
        handleRemoveRequest();

        Runtime.getRuntime().addShutdownHook(new Thread(ECSServer::shutdown));

        while (true) {
            // accept socket from KVServer, block if no connection comes in
            Socket clientSocket = ecsServerSocket.accept();
            byte[] buffer = new byte[1024];
            int bytesRead = clientSocket.getInputStream().read(buffer);
            byte[] address = new byte[bytesRead];
            System.arraycopy(buffer, 0, address, 0, bytesRead);

            String addressString = new String(address);
            String remoteAddress = addressString.split(":")[0];
            int remotePort = Integer.parseInt(addressString.split(":")[1]);

            LOGGER.info("Accept new KVServer <" + remoteAddress + ":" + remotePort + ">");

            Thread.sleep(1000);

            // Connect to KVServer through NIO SocketChannel
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(remoteAddress, remotePort));

            while (!socketChannel.finishConnect()) {
                System.out.println("Connecting to KVServer...");
                sleep(1000);
            }
            ECSMessageBuilder.create().receive(socketChannel); // Test

            Node node = new Node(remoteAddress, remotePort, socketChannel);
            ConsistentHash.INSTANCE.addNode(node);
            nodeMap.put(remoteAddress + ":" + remotePort, node);
            nodeSocketMap.put(node, clientSocket);
            readKVServer(clientSocket); // read shutdown message from KVServer
        }
    }

    /**
     * Handle remove request from KVServer with a new thread
     * @throws Exception
     */
    private void readKVServer(Socket clientSocket) throws Exception {
        executorService.execute(() -> {
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            readBuffer.clear(); // clear buffer

            try {
                // read shutdown message from KVServer
                byte[] buffer = new byte[1024];
                int bytesRead = clientSocket.getInputStream().read(buffer);
                if (bytesRead != -1) {
                    byte[] message = new byte[bytesRead];
                    System.arraycopy(buffer, 0, message, 0, bytesRead);
                    String addressOfRemoveNode = new String(message);
                    System.out.println("Remove Node:" + addressOfRemoveNode);
                    // add node to close queue, handle remove request will be executed in another thread
                    closeQueue.add(nodeMap.get(addressOfRemoveNode));
                }
                Thread.currentThread().interrupt();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }


    /**
     * Shutdown hook called when ECS is terminated
     */
    private static void shutdown() {
        // close thread pool
        executorService.shutdown();

        try {
            // Longest time to wait for all threads to complete tasks
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ECS shutdowns complete.");
    }

    /**
     * Thread specifically for closing KVServer properly
     */
    private void handleRemoveRequest() {
        executorService.execute(() -> {
            while (true) {
                try {
                    if (closeQueue != null && !closeQueue.isEmpty()) {
                        Node node = closeQueue.poll();
                        Socket clientSocket = nodeSocketMap.get(node);
                        ConsistentHash.INSTANCE.removeNode(node);

                        OutputStream outputStream = clientSocket.getOutputStream();
                        ByteBuffer byteWriteBuffer = ByteBuffer.wrap("REMOVE_NODE_SUCCESS".getBytes());
                        outputStream.write(byteWriteBuffer.array());
                        outputStream.flush();

                        node.getSocketChannel().close();
                        clientSocket.close();
                    }
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}