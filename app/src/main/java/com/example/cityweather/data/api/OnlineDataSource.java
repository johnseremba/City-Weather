package com.example.cityweather.data.api;

import com.example.cityweather.BuildConfig;
import com.example.cityweather.data.api.model.ApiResponse;
import com.example.cityweather.data.api.model.WeatherItem;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnlineDataSource {
    private static final Object LOCK = new Object();
    private static OnlineDataSource instance;
    private final WeatherService weatherService;

    public OnlineDataSource(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public static OnlineDataSource getInstance(WeatherService weatherService) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new OnlineDataSource(weatherService);
            }
        }
        return instance;
    }

    public void getWeatherData(double latitude, double longitude, RequestCallback<List<WeatherItem>> callback) {
        String apiKey = BuildConfig.OPEN_WEATHER_API_KEY;
        weatherService.getWeatherForecast(latitude, longitude, apiKey).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getWeatherItemList());
                } else {
                    callback.onError("No Weather data!");
                }
            }

            @Override
            public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                t.printStackTrace();
                callback.onError(t.getMessage());
            }
        });
    }

    public interface RequestCallback<T> {
        void onSuccess(T result);

        void onError(String errorMessage);
    }
}
