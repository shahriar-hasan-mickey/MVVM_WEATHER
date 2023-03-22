package com.example.mvvm_weather.common;

public interface RequestCompleteListener <T>{
    void onRequestSuccess(T data);
    void onRequestFailed(String errorMessage);
}
