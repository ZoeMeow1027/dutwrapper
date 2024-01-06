package io.dutwrapper.dutwrapper.customrequest2;

import javax.annotation.Nullable;

public class WrapperResponse {
    private @Nullable Integer statusCode;
    private @Nullable String content;
    private @Nullable Exception exception;
    private @Nullable String message;
    private @Nullable String sessionId;

    public WrapperResponse() {
    }

    public WrapperResponse(@Nullable Integer statusCode, @Nullable String content, @Nullable Exception exception,
            @Nullable String message, @Nullable String sessionId) {
        this.statusCode = statusCode;
        this.content = content;
        this.exception = exception;
        this.message = message;
        this.sessionId = sessionId;
    }

    public @Nullable Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public @Nullable String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public @Nullable Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public @Nullable String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public @Nullable String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
