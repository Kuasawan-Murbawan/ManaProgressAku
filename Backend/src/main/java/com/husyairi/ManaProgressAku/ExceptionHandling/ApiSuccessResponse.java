package com.husyairi.ManaProgressAku.ExceptionHandling;

public class ApiSuccessResponse<T> {
    private String status;
    private String message;
    private T data;

    public ApiSuccessResponse() {}

    public ApiSuccessResponse(String message, T data) {
        this.status = "SUCCESS";
        this.message = message;
        this.data = data;
    }

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}

