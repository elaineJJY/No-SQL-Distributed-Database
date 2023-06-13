package de.tum.common;

import de.tum.node.DataType;
import de.tum.node.Node;
import de.tum.node.Range;

import java.util.SortedMap;

import java.io.Serializable;

public class ECSMessage implements Serializable {

    public Command command;
    public SortedMap<String, Node> ring;
    public Node removedNode;
    public Range range;
    public DataType dataType;

    public enum Command {
        INIT, RECOVER, DELETE_EXPIRED_DATA, UPDATE_RING
    }
}
