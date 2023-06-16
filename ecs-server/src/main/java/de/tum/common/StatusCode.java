package de.tum.common;

public enum StatusCode {
    NOT_FOUND(404),
    OK(200),
    REDIRECT(307),
    SERVER_ERROR(500);

    private final int code;

    private StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
