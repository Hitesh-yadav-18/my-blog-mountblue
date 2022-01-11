package com.company.my.blog.model;

public class BadRequestMessage {
    private String message;

    public BadRequestMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
