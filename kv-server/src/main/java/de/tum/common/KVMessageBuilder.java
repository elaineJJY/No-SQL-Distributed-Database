package de.tum.common;

import com.alibaba.fastjson.JSON;
import de.tum.node.DataType;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.channels.SocketChannel;
import de.tum.node.Range;


public class KVMessageBuilder {
    private KVMessage message;

    private KVMessageBuilder() {
        message = new KVMessage();
    }

    public KVMessage build() {
        return message;
    }

    public static KVMessageBuilder create() {
        return new KVMessageBuilder();
    }

    public KVMessageBuilder command(KVMessage.Command command) {
        message.setCommand(command.toString());
        return this;
    }
    public KVMessageBuilder command(String command) {
        message.setCommand(command);
        return this;
    }

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