package com.husyairi.ManaProgressAku.ExceptionHandling;

public class RateLimitExceededException extends RuntimeException{
    public RateLimitExceededException(String message){
        super(message);
    }
}
