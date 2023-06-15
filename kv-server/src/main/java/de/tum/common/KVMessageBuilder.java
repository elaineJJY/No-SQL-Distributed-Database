package de.tum.common;

import com.alibaba.fastjson.JSON;
import de.tum.node.DataType;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

import de.tum.node.Range;


public class KVMessageBuilder {
    private final Logger LOGGER = ServerLogger.INSTANCE.getLogger();
    private KVMessage message;
    private SocketChannel socketChannel;

    private KVMessageBuilder() {
        message = new KVMessage();
    }

    public KVMessage build() {
        return message;
    }

    public static KVMessageBuilder create() {
        return new KVMessageBuilder();
    }

    public KVMessageBuilder socketChannel(SocketChannel socketChannel){
        this.socketChannel = socketChannel;
        return this;
    }

    public KVMessageBuilder command(KVMessage.Command command) {
        message.setCommand(command);
        return this;
    }
//    public KVMessageBuilder command(String command) {
//        message.setCommand(command);
//        return this;
//    }

    public KVMessageBuilder key(String key) {
        message.setKey(key);
        return this;
    }

    public KVMessageBuilder value(String value) {
        message.setValue(value);
        return this;
    }

    public KVMessageBuilder dataType(DataType dataType) {
        message.setDataType(dataType);
        return this;
    }

    public KVMessageBuilder range(Range range){
        message.setRange(range);
        return this;
    }

    public KVMessageBuilder statusCode(StatusCode statusCode) {
        message.setStatusCode(statusCode);
        return this;
    }
    public KVMessageBuilder send() throws IOException {
        String messageString = JSON.toJSONString(this.message);
        byte[] msg = messageString.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.wrap(msg);
        int bytesWritten;
        do {
            bytesWritten = socketChannel.write(byteBuffer);
        } while (bytesWritten > 0 && byteBuffer.hasRemaining());
        LOGGER.info("Sent:" + messageString.trim());
        return this;
    }


    public String receive() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = 0;
        do {
            bytesRead = socketChannel.read(buffer);
        } while (bytesRead == 0);

        // Read the received bytes into a byte[]
        byte[] receivedMessage = new byte[bytesRead];
        buffer.flip();
        buffer.get(receivedMessage);
        LOGGER.info( "For Message:" + JSON.toJSONString(this.message) + " Received:" + new String(receivedMessage).trim());
        return new String(receivedMessage);
    }


    public String sendAndRespond(SocketChannel socketChannel) throws Exception {
        String messageString = JSON.toJSONString(this.message);
        byte[] byteMessage = messageString.getBytes(StandardCharsets.UTF_8);

        ByteBuffer buffer = ByteBuffer.allocate(byteMessage.length);
        buffer.put(byteMessage);
        buffer.flip();

        socketChannel.write(buffer);
        
        ByteBuffer responseBuffer = ByteBuffer.allocate(1024); 
        StringBuilder responseBuilder = new StringBuilder();

        int bytesRead;
        while ((bytesRead = socketChannel.read(responseBuffer)) > 0) {
            responseBuffer.flip();
            byte[] bytes = new byte[bytesRead];
            responseBuffer.get(bytes);
            responseBuilder.append(new String(bytes, StandardCharsets.UTF_8));
            
            if (!responseBuffer.hasRemaining()) {
                responseBuffer = expandBuffer(responseBuffer);
            }
            responseBuffer.clear();
        }

        String response = responseBuilder.toString().trim();
        System.out.println("Received response: " + response);

        return response;
    }

    private ByteBuffer expandBuffer(ByteBuffer buffer) {
        ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }


}


//    private String buildMessageString() {
//        StringBuilder builder = new StringBuilder();
//        builder.append(message.getCommand()).append(" ");
//
//        if (message.getKey() != null) {
//            builder.append(message.getKey()).append(" ");
//        }
//
//        if (message.getValue() != null) {
//            builder.append(message.getValue()).append(" ");
//        }
//
//        if (message.getDataType() != null) {
//            builder.append(message.getDataType().toString()).append(" ");
//        }
//
//        builder.append(message.getForwardCount());
//
//        return builder.toString().trim();
//    }

//    public KVMessage send(SocketChannel socketChannel) throws Exception {
//        String messageString = buildMessageString();
//        byte[] byteMessage = messageString.getBytes(StandardCharsets.UTF_8);
//
//        ByteBuffer buffer = ByteBuffer.allocate(byteMessage.length);
//        buffer.put(byteMessage);
//        buffer.flip();
//
//        socketChannel.write(buffer);
//
//        return message;
//    }