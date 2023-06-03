package de.tum.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


/**
 * The Client Class manage the generation and usage of sockets
 * @author Jiajin Yi, Weijian Feng, Mingrun Ma
 * @version 1.0
 */
public enum Client {
    INSTANCE;
    private SocketChannel socketChannel;

    /**
     * Establishes a connection with a server at the specified IP address and port number.
     * @param ip the IP address of the server to connect to
     * @param port the port number to use for the connection
     * @throws IOException if there is an error connecting to the server
     */
    public void startConnection(String ip, int port) throws IOException {
        socketChannel = SocketChannel.open(new InetSocketAddress(ip, port));
        socketChannel.configureBlocking(false);
    }

    /**
     * Send message from client to server
     * @param msg the message waiting to be sent
     * @throws Exception if there is an error of outputting stream
     */

    public void send(byte[] msg) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.wrap(msg);
        while (byteBuffer.hasRemaining()) {
            socketChannel.write(byteBuffer);
        }
    }

    /**
     * receive message from server
     * @return receive data
     * @throws Exception if there is error of getting input stream
     */
    public byte[] receive() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = 0;
        do {
            bytesRead = socketChannel.read(buffer);
        } while (bytesRead == 0);

        // Read the received bytes into a byte[]
        byte[] receivedMessage = new byte[bytesRead];
        buffer.flip();
        buffer.get(receivedMessage);
        return receivedMessage;
    }

    /**
     * Stop connection between client and server
     * @throws IOException if there is an error operating socket
     */
    public void stopConnection() throws IOException {
        socketChannel.close();
        System.out.println("stop connection");
    }

    /**
     * Check if the connection between client and server works properly
     * @return false if there is no connection, true if there is connection
     */
    public boolean isConnected() {
        if (socketChannel == null) {
            return false;
        } else {
            return socketChannel.isConnected();
        }
    }
}
