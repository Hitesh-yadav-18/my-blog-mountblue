package com.company.my.blog.model;

public class RequestMessage {
    private String message;

    public RequestMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
