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
        return new String(receivedMessage);
    }

    private ByteBuffer expandBuffer(ByteBuffer buffer) {
        ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

}
