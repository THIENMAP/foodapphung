package com.example.foodapp.model;

import java.util.List;

public class HoadonModel {
    boolean success;
    String message;
    List<Hoadon> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Hoadon> getResult() {
        return result;
    }

    public void setResult(List<Hoadon> result) {
        this.result = result;
    }
}
