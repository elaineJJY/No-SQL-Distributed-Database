package de.tum.common;

public enum StatusCode {
	OK(200, "OK"),
	UPDATED_CONTENT(220, "Updated"),
	PUT_CONTENT(230, "put content"),
	DELETE_CONTENT(240, "delete content"),
	GET_CONTENT(250, "get content"),
	BAD_REQUEST(400, "Bad Request"),
	NOT_FOUND(404, "Not Found"),
	INTERNAL_SERVER_ERROR(500, "Internal Server Error");

	private final int code;
	private final String message;
	StatusCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}
}
