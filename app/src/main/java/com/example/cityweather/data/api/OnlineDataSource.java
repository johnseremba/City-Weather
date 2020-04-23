package com.example.cityweather.data.api;

import com.example.cityweather.BuildConfig;
import com.example.cityweather.data.api.model.ApiResponse;
import com.example.cityweather.data.api.model.WeatherItem;
import com.example.cityweather.data.local.model.City;
import com.example.cityweather.data.local.model.Forecast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnlineDataSource implements OnlineDataSourceContract {
    private static final Object LOCK = new Object();
    private static OnlineDataSource onlineDataSource;
    private final WeatherService weatherService;

    public OnlineDataSource(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public static OnlineDataSource getInstance(WeatherService weatherService) {
        if (onlineDataSource == null) {
            synchronized (LOCK) {
                onlineDataSource = new OnlineDataSource(weatherService);
            }
        }
        return onlineDataSource;
    }

    @Override
    public void getWeatherData(City city, RequestCallback<List<Forecast>> callback) {
        String apiKey = BuildConfig.OPEN_WEATHER_API_KEY;
        weatherService.getWeatherForecast(city.getLatitude(), city.getLongitude(), apiKey).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Forecast> forecastList = parseForecastData(city.getId(), response.body().getWeatherItemList());
                    callback.onSuccess(forecastList);
                } else {
                    callback.onError(ResponseCode.NO_DATA, "No Weather data!");
                }
            }

            @Override
            public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                t.printStackTrace();
                callback.onError(ResponseCode.UNKNOWN_EXCEPTION, t.getMessage());
            }
        });
    }

    private List<Forecast> parseForecastData(int cityId, List<WeatherItem> weatherItems) {
        List<Forecast> forecastList = new ArrayList<>();
        for (WeatherItem item : weatherItems) {
            double temperature = item.getTemperature().getTemperatureInCelsius();
            String icon = item.getMainIcon();
            Date date = item.getDate();
            forecastList.add(new Forecast(cityId, temperature, date, icon));
        }
        return forecastList;
    }
}
