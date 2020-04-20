package com.example.cityweather.utils;

import com.example.cityweather.data.Repository;
import com.example.cityweather.data.api.OnlineDataSource;
import com.example.cityweather.data.api.WeatherService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InjectorUtils {
    public static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    public Repository provideRepository() {
        return Repository.getInstance();
    }

    public WeatherService provideWeatherService(Retrofit retrofit) {
        return retrofit.create(WeatherService.class);
    }

    public Retrofit provideRetrofit() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public OnlineDataSource provideOnlineDataSource(WeatherService weatherService) {
        return OnlineDataSource.getInstance(weatherService);
    }
}
