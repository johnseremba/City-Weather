package com.example.cityweather.data.api;

import com.example.cityweather.data.local.model.City;
import com.example.cityweather.data.local.model.Forecast;

import java.util.List;

public interface OnlineDataSourceContract {
    void getWeatherData(City city, RequestCallback<List<Forecast>> callback);
}
