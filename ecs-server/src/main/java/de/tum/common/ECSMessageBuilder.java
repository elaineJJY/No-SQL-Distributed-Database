package de.tum.common;

import com.alibaba.fastjson.JSON;
import de.tum.communication.ServerLogger;
import de.tum.node.DataType;
import de.tum.node.Node;
import de.tum.node.Range;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.nio.channels.SocketChannel;

public class ECSMessageBuilder {
    private ECSMessage message;

    private ECSMessageBuilder() {
        message = new ECSMessage();
    }

    public static ECSMessageBuilder create() {
        return new ECSMessageBuilder();
    }

    public ECSMessageBuilder command(ECSMessage.Command command) {
        message.command = command;
        return this;
    }

    public ECSMessageBuilder ring(HashMap<String, String> ring) {
        message.ring = ring; // TODO
        return this;
    }

    public ECSMessageBuilder removedNode(Node removedNode) {
        message.removedNode = removedNode;
        return this;
    }

    public ECSMessageBuilder range(Range range) {
        message.range = range;
        return this;
    }

    public ECSMessageBuilder dataType (DataType where){
        message.dataType = where;
        return this;
    }

//    public ByteBuffer buildByteBuffer() throws IOException {
//        String messageString = JSON.toJSONString(message);
//        byte[] byteMessage = messageString.getBytes(StandardCharsets.UTF_8);
//        return ByteBuffer.wrap(byteMessage);
//    }
//
//    public void send(SocketChannel socketChannel) throws IOException {
//        ByteBuffer buffer = buildByteBuffer();
//        socketChannel.write(buffer);
//    }

    public String sendAndRespond(SocketChannel socketChannel) throws Exception {
        String messageString = JSON.toJSONString(this.message);
        byte[] byteMessage = messageString.getBytes(StandardCharsets.UTF_8);

        ByteBuffer buffer = ByteBuffer.allocate(byteMessage.length);
        buffer.put(byteMessage);
        buffer.flip();

        socketChannel.write(buffer);

        //Read
//        ByteBuffer responseBuffer = ByteBuffer.allocate(1024);
//        StringBuilder responseBuilder = new StringBuilder();
//
//        int bytesRead;
//        while ((bytesRead = socketChannel.read(responseBuffer)) > 0) {
//            responseBuffer.flip();
//            byte[] bytes = new byte[bytesRead];
//            responseBuffer.get(bytes);
//            responseBuilder.append(new String(bytes, StandardCharsets.UTF_8));
//
//            if (!responseBuffer.hasRemaining()) {
//                responseBuffer = expandBuffer(responseBuffer);
//            }
//            responseBuffer.clear();
//        }
//
//        String response = responseBuilder.toString().trim();
//        System.out.println("Received response: " + response);
        ByteBuffer readbuffer = ByteBuffer.allocate(1024);
        int bytesRead = 0;
        do {
            bytesRead = socketChannel.read(readbuffer);
        } while (bytesRead == 0);

        // Read the received bytes into a byte[]
        byte[] receivedMessage = new byte[bytesRead];
        readbuffer.flip();
        readbuffer.get(receivedMessage);
        System.out.println("Received response: " + new String(receivedMessage, StandardCharsets.UTF_8).trim());
        return new String(receivedMessage, StandardCharsets.UTF_8);
    }

    private ByteBuffer expandBuffer(ByteBuffer buffer) {
        ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

}
