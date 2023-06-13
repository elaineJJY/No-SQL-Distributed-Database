package de.tum.common;

import com.alibaba.fastjson.JSON;
import de.tum.node.DataType;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.channels.SocketChannel;
import de.tum.node.Range;


public class MessageBuilder {
    private KVMessage message;

    private MessageBuilder() {
        message = new KVMessage();
    }

    public static MessageBuilder create() {
        return new MessageBuilder();
    }

    public MessageBuilder command(Command command) {
        message.setCommand(command.toString());
        return this;
    }

    public MessageBuilder key(String key) {
        message.setKey(key);
        return this;
    }

    public MessageBuilder value(String value) {
        message.setValue(value);
        return this;
    }

    public MessageBuilder dataType(DataType dataType) {
        message.setDataType(dataType);
        return this;
    }

    public MessageBuilder range(Range range){
        message.setRange(range);
        return this;
    }

    public MessageBuilder forwardCount(int forwardCount) {
        message.setForwardCount(forwardCount);
        return this;
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


    public enum Command {
        PUT,
        GET,
        COPY,
        DELETE,
    }
}
