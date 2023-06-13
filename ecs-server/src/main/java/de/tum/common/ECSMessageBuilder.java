package de.tum.common;

import com.alibaba.fastjson.JSON;
import de.tum.node.DataType;
import de.tum.node.Node;
import de.tum.node.Range;

import javax.xml.crypto.Data;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.SortedMap;
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

    public ECSMessageBuilder ring(SortedMap<String, Node> ring) {
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

    public ECSMessageBuilder DataType (DataType where){
        message.dataType = where;
        return this;
    }

    public ByteBuffer buildByteBuffer() throws IOException {
        String messageString = JSON.toJSONString(message);
        byte[] byteMessage = messageString.getBytes(StandardCharsets.UTF_8);
        return ByteBuffer.wrap(byteMessage);
    }

    public void send(SocketChannel socketChannel) throws IOException {
        ByteBuffer buffer = buildByteBuffer();
        socketChannel.write(buffer);
    }
}
