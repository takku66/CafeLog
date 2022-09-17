package com.cafelog.exception;

public class NotAvailableUserException extends Exception {

    public NotAvailableUserException(){
        super("このユーザーは利用できません。");
    }

    public NotAvailableUserException(String message){
        super(message);
    }
}
