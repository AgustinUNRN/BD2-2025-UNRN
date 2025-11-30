package unrn.model.controller;

import java.time.Instant;

public class ErrorResponse {
    private final String message;
    private final String error;
    private final long timestamp;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
        this.timestamp = Instant.now().toEpochMilli();
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

