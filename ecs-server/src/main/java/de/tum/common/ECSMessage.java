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

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public HashMap<String, String> getRing() {
        return ring;
    }

    public void setRing(HashMap<String, String> ring) {
        this.ring = ring;
    }

    public Node getRemovedNode() {
        return removedNode;
    }

    public void setRemovedNode(Node removedNode) {
        this.removedNode = removedNode;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
}