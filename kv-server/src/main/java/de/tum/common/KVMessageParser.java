package de.tum.common;

import com.alibaba.fastjson.JSON;
import de.tum.database.IDatabase;
import de.tum.node.DataType;
import de.tum.node.Node;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class KVMessageParser {
    public static KVMessage parseMessageFromString(String messageString) {
        KVMessage message = JSON.parseObject(messageString, KVMessage.class);
        return message;
    }


    public static String processMessage(KVMessage message, Node localNode) throws Exception {
        KVMessage.Command command = message.getCommand();
        String response = "";
        switch (command) {
            case PUT:
                if (message.getDataType() == DataType.DATA) {
                    StatusCode code = localNode.put(message.getKey(), message.getValue());
                    response = code.toString() + " " + message.getKey();
                }
                else {
                    localNode.putBackup(message.getKey(), message.getValue());
                }
                break;

            case GET:
                String value = localNode.get(message.getKey());
                if(value != "") {
                    response = "get_success " + message.getKey() + " " + value;
                }
                else{
                    response = "get_error " + message.getKey();
                }
                break;
            case COPY:
                localNode.copy(message.getDataType(), message.getRange());
                break;

            case DELETE:
                if (message.getDataType() == DataType.DATA) {
                    StatusCode code = localNode.delete(message.getKey());
                    response = code == StatusCode.OK ? "delete_success" : "delete_error";
                    response = response + " " + message.getKey();
                }
                else {
                    localNode.deleteBackup(message.getKey());
                }
                break;
            default:
                throw new UnsupportedOperationException("Unsupported command: " + command);

        }
        return response + "\n";
    }
}
