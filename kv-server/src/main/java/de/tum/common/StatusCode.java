package de.tum.common;

import java.io.Serializable;

public enum StatusCode implements Serializable {
    NOT_FOUND(404),
    OK(200),
    REDIRECT(307),
    SERVER_ERROR(500),
    put_success(001),
    put_update(002);

    private final int code;

    private StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
