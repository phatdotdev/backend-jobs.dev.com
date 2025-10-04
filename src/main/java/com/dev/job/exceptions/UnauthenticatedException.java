package com.dev.job.exceptions;

public class UnauthenticatedException extends RuntimeException{
    public UnauthenticatedException(String message){
        super(message);
    }
}
