package de.tum.common;

import de.tum.node.DataType;
import de.tum.node.Node;
import de.tum.node.Range;

import java.util.HashMap;
import java.io.Serializable;

public class ECSMessage implements Serializable {

    public Command command;
    public HashMap<String, String> ring; // key: ip:port, value: hash
    public Node removedNode;
    public Range range;
    public DataType dataType;


    public enum Command {
        INIT, RECOVER, DELETE_EXPIRED_DATA, UPDATE_RING
    }
}