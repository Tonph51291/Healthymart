package com.example.ass.DTO;

public class Respondata <T>{
    private String message;
  private  int status;
    private T data;

    private  T user;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getUser() {
        return user;
    }

    public void setUser(T user) {
        this.user = user;
    }

    public Respondata(String message, int status, T data, T user) {
        this.message = message;
        this.status = status;
        this.data = data;
        this.user = user;
    }
}
