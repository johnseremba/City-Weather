package com.example.cityweather.data.api;

import com.example.cityweather.data.api.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("forecast")
    Call<ApiResponse> getWeatherForecast(@Query("lat") double latitude,
                                         @Query("lon") double longitude,
                                         @Query("appid") String apiKey);
}
