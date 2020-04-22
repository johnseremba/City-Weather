package com.example.cityweather.utils;

import android.content.Context;

import com.example.cityweather.data.Repository;
import com.example.cityweather.data.api.OnlineDataSource;
import com.example.cityweather.data.api.WeatherService;
import com.example.cityweather.data.local.AppDatabase;
import com.example.cityweather.data.local.LocalDataSource;
import com.example.cityweather.data.local.model.City;
import com.example.cityweather.ui.city.viewmodel.CityFragmentViewModelFactory;
import com.example.cityweather.ui.city.viewmodel.CityListFragmentViewModelFactory;
import com.example.cityweather.ui.forecast.viewmodel.ForecastFragmentViewModelFactory;
import com.example.cityweather.ui.map.MapFragmentViewModelFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InjectorUtils {
    public static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    public static final String SHARED_PREFS_NAME = "com.example.cityweather.shared_prefs";

    public static Repository provideRepository(Context context) {
        return Repository.getInstance(
                provideLocalDataSource(context),
                provideOnlineDataSource(),
                provideAppExecutors());
    }

    public static Retrofit provideRetrofit() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static WeatherService provideWeatherService() {
        return provideRetrofit().create(WeatherService.class);
    }

    public static OnlineDataSource provideOnlineDataSource() {
        return OnlineDataSource.getInstance(provideWeatherService());
    }

    public static AppExecutors provideAppExecutors() {
        return AppExecutors.getInstance();
    }

    public static AppDatabase provideAppDatabase(Context context) {
        return AppDatabase.getDatabase(context);
    }

    public static LocalDataSource provideLocalDataSource(Context context) {
        AppDatabase database = provideAppDatabase(context);
        return LocalDataSource.getInstance(database.cityDao(), database.forecastDao(), provideAppExecutors());
    }

    public static MapFragmentViewModelFactory provideMapFragmentViewModelFactory(Context context) {
        return new MapFragmentViewModelFactory(provideRepository(context));
    }

    public static CityFragmentViewModelFactory provideCityFragmentViewModelFactory(Context context, City city) {
        return new CityFragmentViewModelFactory(provideRepository(context), city);
    }

    public static CityListFragmentViewModelFactory provideCityListFragmentViewModelFactory(Context context) {
        return new CityListFragmentViewModelFactory(provideRepository(context));
    }

    public static ForecastFragmentViewModelFactory provideForecastFragmentViewModelFactory(Context context, City city) {
        return new ForecastFragmentViewModelFactory(provideRepository(context), city);
    }
}
