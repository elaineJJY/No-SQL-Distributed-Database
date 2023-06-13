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
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

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
    private int port;
    private String address;
    private Selector selector;
    private ServerSocketChannel ecsServerSocketChannel;
    private ConsistentHash ring;

    public ECSServer(String address, int port) {
        this.port = port;
        this.address = address;
    }

    public void start(boolean helpUsage) throws IOException {
        if (helpUsage) Help.helpDisplay();

        this.ecsServerSocketChannel = ServerSocketChannel.open();
        this.ecsServerSocketChannel.configureBlocking(false);
        ecsServerSocketChannel.bind(new InetSocketAddress(address, port));
        this.selector = Selector.open();

        ecsServerSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        LOGGER.info("ECSServer is listening on port " + port + ", ready to receive data from KVServers");

        while (selector.select() > 0) {
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
            while (selectionKeyIterator.hasNext()) {
                SelectionKey next = selectionKeyIterator.next();
                if (next.isAcceptable()) {
                    accept();
                } else if (next.isReadable()) {
//                  read();
                }
                selectionKeyIterator.remove();
            }
        }
    }

    // accept new server
    private void accept() throws IOException {
        SocketChannel socketChannel = ecsServerSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);

        String message = "Hello Server, it's ECS here.\n";
        send(message, socketChannel);
        // addNode();
        // MetaUpdate HashMap
        LOGGER.info("Accept new server: " + socketChannel.getRemoteAddress());
    }

    private void read(SelectionKey key) throws Exception {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        readBuffer.clear(); // clear buffer
        SocketChannel socketChannel = (SocketChannel) key.channel();
        int len = socketChannel.read(readBuffer);
        if (len != -1) {
            readBuffer.flip(); // reset position
            byte[] bytes = new byte[readBuffer.remaining()]; // 根据缓冲区的数据长度创建字节数组
            readBuffer.get(bytes); // 将缓冲区的数据读到字节数组中
            String request = new String(bytes).trim();
            System.out.println("ECS received: " + request);
            socketChannel.configureBlocking(false);
            //key.interestOps(SelectionKey.OP_READ); // 关心读事件?
            LOGGER.info("Register read event for client: " + socketChannel.getRemoteAddress());

            //String request =  new String(readBuffer.array());
            LOGGER.info("Received request:" + request);
        }
        else {
            socketChannel.close(); // close channel
            key.cancel(); // cancel key
        }
    }

    /**
     * Send message to client
     * @param msg
     * @param socketChannel
     * @throws IOException
     */
    private void send(String msg, SocketChannel socketChannel) throws IOException {
        String newMsg = msg + "\n";
        socketChannel.write(ByteBuffer.wrap(newMsg.getBytes()));
    }

}