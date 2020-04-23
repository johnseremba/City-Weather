package com.example.cityweather.data.local.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CityWithForecast {
    @Embedded
    public City city;

    @Relation(
            parentColumn = "city_id",
            entityColumn = "forecast_id"
    )
    public List<Forecast> forecastList;
}
