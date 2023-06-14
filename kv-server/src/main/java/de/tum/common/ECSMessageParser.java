package de.tum.common;

import com.alibaba.fastjson.JSON;
import de.tum.node.Node;

public class ECSMessageParser {
    public static ECSMessage parseMessageFromString(String messageString) {
        ECSMessage message = JSON.parseObject(messageString, ECSMessage.class);
        return message;
    }

    public static StatusCode processMessage(ECSMessage message, Node localNode) throws Exception {
        ECSMessage.Command command = message.command;
        ServerLogger.INSTANCE.getLogger().info("Received Message:" + message.toString());
        switch (command) {
            case UPDATE_RING:
                return localNode.updateMetaData(message.ring);

            case DELETE_EXPIRED_DATA:
                return localNode.deleteExpiredData(message.dataType, message.range);

            case INIT:
                return localNode.init();

            case RECOVER:
                return localNode.recover(message.removedNode);

            default:
                throw new UnsupportedOperationException("Unsupported command: " + command);
        }
    }
}
