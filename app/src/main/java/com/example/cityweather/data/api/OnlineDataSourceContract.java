package com.example.cityweather.data.api;

import com.example.cityweather.data.api.model.WeatherItem;

import java.util.List;

public interface OnlineDataSourceContract {
    void getWeatherData(double latitude, double longitude, RequestCallback<List<WeatherItem>> callback);
}
