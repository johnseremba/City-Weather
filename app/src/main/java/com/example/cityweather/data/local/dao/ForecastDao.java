package com.example.cityweather.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import com.example.cityweather.data.local.model.Forecast;

import java.util.List;

public interface ForecastDao extends BaseDao<Forecast> {
    @Query("SELECT * FROM forecast WHERE city_id = :cityId")
    LiveData<List<Forecast>> getCityForecast(int cityId);

    @Query("DELETE FROM forecast WHERE city_id = :cityId")
    void deleteForecastByCityId(int cityId);

    @Query("SELECT COUNT(forecast_id) FROM forecast WHERE city_id = :cityId")
    int getCityForecastCount(int cityId);
}
