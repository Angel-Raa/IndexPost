package io.github.angel.raa.utils.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Response <T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -3487923168793216732L;
    private transient T data;
    private String message;
    private HttpStatus status;
    private int statusCode;
    private boolean success;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public Response() {
    }

    public Response(T data, String message, HttpStatus status, int statusCode, boolean success) {
        this.data = data;
        this.message = message;
        this.status = status;
        this.statusCode = statusCode;
        this.success = success;
        this.timestamp = LocalDateTime.now();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
