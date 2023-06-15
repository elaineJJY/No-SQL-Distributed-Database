package de.tum.common;

import com.alibaba.fastjson.JSON;
import de.tum.database.IDatabase;
import de.tum.node.DataType;
import de.tum.node.Node;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class KVMessageParser {
    public static KVMessage parseMessageFromString(String messageString) {
        KVMessage message = JSON.parseObject(messageString, KVMessage.class);
        return message;
    }


    public static String processMessage(KVMessage message, Node localNode) throws Exception {
        KVMessage.Command command = message.getCommand();
        String response = "";
        ServerLogger.INSTANCE.getLogger().info("Received Command Type:" + command);
        switch (command) {
            case PUT:
                if (message.getDataType() == DataType.BACKUP) {
                    localNode.putBackup(message.getKey(), message.getValue());
                    response = "updated Backup";
                }
                else {
                    StatusCode code = localNode.put(message.getKey(), message.getValue());
                    CompletableFuture<StatusCode> backupResult = localNode.putDataToBackupNode(message.getKey(), message.getValue());
                    backupResult.thenApply(result -> {
                        System.out.println("Put " + message.getKey() +" to Backup: " + result);
                        return result;
                    });
                    response = code.toString() + " " + message.getKey();
                }
                break;

            case GET:
                String value = localNode.get(message.getKey());
                if(value != null || value.equals("") ) {
                    response = "get_success " + message.getKey() + " " + value;
                }
                else{
                    response = "get_error " + message.getKey();
                }
                break;
            case COPY:
                HashMap<String, String> data = localNode.getDataByRange(message.getDataType(), message.getRange());
                response = JSON.toJSONString(data);
                break;

            case DELETE:
                if (message.getDataType() == DataType.BACKUP) {
                    localNode.deleteBackup(message.getKey());
                }
                else {
                    StatusCode code = localNode.delete(message.getKey());
                    CompletableFuture<StatusCode> backupResult = localNode.deleteDataFromBackupNode(message.getKey());
                    backupResult.thenApply(result -> {
                        System.out.println("Delete " + message.getKey() +" from Backup: " + result);
                        return result;
                    });
                    response = code == StatusCode.OK ? "delete_success" : "delete_error";
                    response = response + " " + message.getKey();
                }
                break;
            default:
                throw new UnsupportedOperationException("Unsupported command: " + command);

        }
        return response + "\n";
    }
}
