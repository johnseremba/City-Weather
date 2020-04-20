package com.example.cityweather.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.cityweather.data.local.dao.CityDao;
import com.example.cityweather.data.local.dao.ForecastDao;
import com.example.cityweather.data.local.model.City;
import com.example.cityweather.data.local.model.Forecast;

@Database(entities = {City.class, Forecast.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "city_weather_db";
    private static AppDatabase database;

    public static AppDatabase getDatabase(Context context) {
        if (database == null) {
            synchronized (LOCK) {
                database = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
            }
        }
        return database;
    }

    public abstract CityDao cityDao();

    public abstract ForecastDao forecastDao();
}
