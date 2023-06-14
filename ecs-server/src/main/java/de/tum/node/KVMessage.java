package de.tum.node;

import de.tum.common.StatusCode;

import java.io.Serializable;

public class KVMessage implements Serializable {

    private Command command;
    private String key;
    private String value;
    private DataType dataType;
    private Range range;
    private StatusCode statusCode;

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(String command) {
        switch(command) {
            case "put": this.command = Command.PUT; break;
            case "get": this.command = Command.GET; break;
            case "delete": this.command = Command.DELETE; break;
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public DataType getDataType() {
        return dataType;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public enum Command {
        PUT,
        GET,
        COPY,
        DELETE,
    }
}
