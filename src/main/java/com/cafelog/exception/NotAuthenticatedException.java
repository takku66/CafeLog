package com.cafelog.exception;

public class NotAuthenticatedException extends RuntimeException {

    public NotAuthenticatedException(){
        super("認証されていないユーザーです。");
    }

    public NotAuthenticatedException(String message){
        super(message);
    }
}
