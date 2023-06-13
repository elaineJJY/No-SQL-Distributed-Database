package de.tum.common;

import com.alibaba.fastjson.JSON;
import de.tum.node.Node;

public class ECSMessageParser {
    public static ECSMessage parseMessageFromString(String messageString) {
        ECSMessage message = JSON.parseObject(messageString, ECSMessage.class);
        return message;
    }

    public static void processMessage(ECSMessage message, Node localNode) throws Exception {
        ECSMessage.Command command = message.command;

        switch (command) {
            case UPDATE_RING:
                localNode.updateMetaData(message.ring);
                break;

            case DELETE_EXPIRED_DATA:
                localNode.deleteExpiredData(message.dataType, message.range);
                break;

            case INIT:
                localNode.init();
                break;

            case RECOVER:
                localNode.recover(message.removedNode);
                break;

            default:
                throw new UnsupportedOperationException("Unsupported command: " + command);
        }
    }

}
