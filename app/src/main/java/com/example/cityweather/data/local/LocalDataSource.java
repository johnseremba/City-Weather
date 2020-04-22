package com.example.cityweather.data.local;

import androidx.lifecycle.LiveData;

import com.example.cityweather.data.local.dao.CityDao;
import com.example.cityweather.data.local.dao.ForecastDao;
import com.example.cityweather.data.local.model.City;
import com.example.cityweather.data.local.model.Forecast;
import com.example.cityweather.utils.AppExecutors;

import java.util.List;

public class LocalDataSource implements LocalDataSourceContract {
    private static final Object LOCK = new Object();
    private static LocalDataSource localDataSource;
    private final CityDao cityDao;
    private final ForecastDao forecastDao;
    private final AppExecutors executors;

    public LocalDataSource(CityDao cityDao, ForecastDao forecastDao, AppExecutors executors) {
        this.cityDao = cityDao;
        this.forecastDao = forecastDao;
        this.executors = executors;
    }

    public static LocalDataSource getInstance(CityDao cityDao, ForecastDao forecastDao, AppExecutors executors) {
        if (localDataSource == null) {
            synchronized (LOCK) {
                localDataSource = new LocalDataSource(cityDao, forecastDao, executors);
            }
        }
        return localDataSource;
    }

    @Override
    public LiveData<City> getCityById(int cityId) {
        return cityDao.getCityById(cityId);
    }

    @Override
    public LiveData<City> getCityByCoordinates(double latitude, double longitude) {
        return cityDao.getCityByCoordinates(latitude, longitude);
    }

    @Override
    public LiveData<List<City>> getCities() {
        return cityDao.getCities();
    }

    @Override
    public LiveData<List<City>> searchCityByName(String query) {
        return cityDao.searchCityByName(query);
    }

    @Override
    public long createCity(City city) {
        return cityDao.insert(city);
    }

    @Override
    public void updateCity(City city) {
        executors.getDiskIO().execute(() -> cityDao.update(new City[]{city}));
    }

    @Override
    public void deleteForecastByCityId(int cityId) {
        executors.getDiskIO().execute(() -> forecastDao.deleteForecastByCityId(cityId));
    }

    @Override
    public void insertForecast(List<Forecast> forecastList) {
        executors.getDiskIO().execute(() -> forecastDao.insertObjects(forecastList));
    }

    @Override
    public int getCityForecastCount(int cityId) {
        return forecastDao.getCityForecastCount(cityId);
    }

    @Override
    public List<City> getCitiesList() {
        return cityDao.getCitiesList();
    }

    @Override
    public int getCityCount(double latitude, double longitude) {
        return cityDao.getCityCountByCoordinates(latitude, longitude);
    }

    @Override
    public void deleteCities(List<City> cities) {
        executors.getDiskIO().execute(() -> {
            cityDao.deleteObjects(cities);
        });
    }
}