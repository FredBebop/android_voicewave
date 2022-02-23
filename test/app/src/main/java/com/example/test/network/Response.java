package com.example.test.network;

public class Response<T> {
    private boolean success;
    private T data ;

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}