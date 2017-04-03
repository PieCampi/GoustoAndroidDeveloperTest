package com.appersiano.goustoexercise.repository;

public class GoustoResponse<T> {

    public String status;
    public T data;

    public String getStatus() {
        return status;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
