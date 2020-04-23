package com.example.cityweather.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.cityweather.data.local.model.City;
import com.example.cityweather.data.local.model.CityWithForecast;

import java.util.List;

@Dao
public interface CityDao extends BaseDao<City> {
    @Query("SELECT * FROM city ORDER BY date_added DESC")
    LiveData<List<City>> getCities();

    @Query("SELECT * FROM city WHERE latitude = :latitude AND longitude = :longitude LIMIT 1")
    LiveData<City> getCityByCoordinates(double latitude, double longitude);

    @Query("SELECT * FROM city WHERE latitude = :latitude AND longitude = :longitude LIMIT 1")
    int getCityCountByCoordinates(double latitude, double longitude);

    @Query("SELECT * FROM city WHERE name LIKE :query")
    LiveData<List<City>> searchCityByName(String query);

    @Transaction
    @Query("SELECT * FROM city WHERE city_id = :cityId")
    LiveData<CityWithForecast> getCityForecast(int cityId);

    @Query("SELECT COUNT(city_id) FROM city")
    int getCitiesCount();

    @Query("SELECT * FROM city")
    List<City> getCitiesList();
}
