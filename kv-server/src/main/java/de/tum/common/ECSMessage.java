package de.tum.common;

import de.tum.node.DataType;
import de.tum.node.Node;
import de.tum.node.Range;

import java.io.Serializable;
import java.util.HashMap;

public class ECSMessage implements Serializable {

    public Command command;
    public HashMap<String, String> ring; // key: ip:port, value: hash
    public String removedNodeAddress;
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

    public String getRemovedNodeAddress() {
        return removedNodeAddress;
    }

    public void setRemovedNodeAd(String removedNodeAddress) {
        this.removedNodeAddress = removedNodeAddress;
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
