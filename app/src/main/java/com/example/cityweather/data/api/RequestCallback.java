package com.example.cityweather.data.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface RequestCallback<T> {
    void onSuccess(@Nullable T result);

    void onError(@NonNull ResponseCode responseCode, @Nullable String errorMessage);
}
