package de.tum.communication;

import de.tum.common.Help;
import de.tum.grpc_api.ECSProto;
import de.tum.grpc_api.ECServiceGrpc;
import de.tum.grpc_api.KVServiceGrpc;
import de.tum.node.ConsistentHash;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
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
    private final int port;
    private final String address;
    private Selector selector;
    private ServerSocketChannel ecsServerSocketChannel;
    private LinkedList<SocketChannel>

    public ECSServer(String address, int port) {
        this.port = port;
        this.address = address;
    }

    public int getPort () { return this.port; }

    public String getAddress() { return this.address; }

    public void start(boolean helpUsage) throws IOException, Exception {
        if (helpUsage) Help.helpDisplay();

        this.ecsServerSocketChannel = ServerSocketChannel.open();
        this.ecsServerSocketChannel.configureBlocking(false);
        ecsServerSocketChannel.bind(new InetSocketAddress(address, port));
        this.selector = Selector.open();

        ecsServerSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        LOGGER.info("ECSServer is listening on port " + port);

        while (selector.select() > 0) {
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
            while (selectionKeyIterator.hasNext()) {
                SelectionKey next = selectionKeyIterator.next();
                if (next.isAcceptable()) {
                    accept();
                } else if (next.isReadable()) {
                  read(next);
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

        String remoteAddress = String.valueOf(socketChannel.getRemoteAddress());
        int remotePort = socketChannel.socket().getPort();

        String message = "ECS starts adding KVServer<" + remoteAddress + ":" + remotePort + "> to ring";

        send(message, socketChannel);
        ConsistentHash.INSTANCE.addNode(node);
        ConsistentHash.INSTANCE.updateRingForAll();
        LOGGER.info("Accept new server: " + socketChannel.getRemoteAddress());
    }

    private void read(SelectionKey key) throws Exception {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        readBuffer.clear(); // clear buffer
        SocketChannel socketChannel = (SocketChannel) key.channel();
        int len = socketChannel.read(readBuffer);
        if (len != -1) {
            ConsistentHash.INSTANCE.updateRing(node);

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
            // Connected KVServer is lost
            socketChannel.close(); // close channel
            // TODO: how to know which node?
            ConsistentHash.INSTANCE.removeNode(node);
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