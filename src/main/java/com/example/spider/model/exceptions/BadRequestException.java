package com.example.spider.model.exceptions;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String msg) {
        super(msg);
    }
}
