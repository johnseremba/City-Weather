package com.example.cityweather.data.api;

public interface RequestCallback<T> {
    void onSuccess(T result);

    void onError(String errorMessage);
}
