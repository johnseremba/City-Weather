package com.example.cityweather.data.local;

import androidx.lifecycle.LiveData;

import com.example.cityweather.data.local.model.City;
import com.example.cityweather.data.local.model.Forecast;

import java.util.List;

public interface LocalDataSourceContract {
    LiveData<City> getCityById(int cityId);

    LiveData<City> getCityByCoordinates(double latitude, double longitude);

    LiveData<List<City>> getCities();

    LiveData<List<City>> searchCityByName(String query);

    List<City> getCitiesList();

    int getCityCount(double latitude, double longitude);

    long createCity(City city);

    void updateCity(City city);

    void deleteForecastByCityId(int cityId);

    void insertForecast(List<Forecast> forecastList);

    void deleteCities(List<City> cities);

    int getCityForecastCount(int cityId);
}