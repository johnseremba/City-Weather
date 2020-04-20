package com.example.cityweather.data;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;

import com.example.cityweather.data.api.OnlineDataSourceContract;
import com.example.cityweather.data.api.RequestCallback;
import com.example.cityweather.data.api.model.WeatherItem;
import com.example.cityweather.data.local.LocalDataSourceContract;
import com.example.cityweather.data.local.model.City;
import com.example.cityweather.data.local.model.Forecast;
import com.example.cityweather.data.workers.DatabaseSyncWorker;
import com.example.cityweather.utils.AppExecutors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Repository {
    public static final Object LOCK = new Object();
    private static Repository repository;
    private final LocalDataSourceContract localDataSource;
    private final OnlineDataSourceContract onlineDataSource;
    private final AppExecutors executors;

    public Repository(LocalDataSourceContract localDataSource,
                      OnlineDataSourceContract onlineDataSource,
                      AppExecutors executors) {
        this.localDataSource = localDataSource;
        this.onlineDataSource = onlineDataSource;
        this.executors = executors;
    }

    public static Repository getInstance(LocalDataSourceContract localDataSource,
                                         OnlineDataSourceContract onlineDataSource,
                                         AppExecutors executors) {
        if (repository == null) {
            synchronized (LOCK) {
                repository = new Repository(localDataSource, onlineDataSource, executors);
            }
        }
        return repository;
    }

    public LocalDataSourceContract getLocalDataSource() {
        return localDataSource;
    }

    public void triggerForecastFetch(final City city) {
        executors.getDiskIO().execute(() -> {
            int count = localDataSource.getCityForecastCount(city.getId());
            if (count < 1) fetchWeatherDataForCity(city);
        });
    }

    public void fetchWeatherDataForCity(final City city) {
        onlineDataSource.getWeatherData(
                city.getLatitude(),
                city.getLongitude(),
                new RequestCallback<List<WeatherItem>>() {
                    @Override
                    public void onSuccess(final List<WeatherItem> weatherItems) {
                        executors.getDiskIO().execute(() -> {
                            List<Forecast> forecastList = parseForecastData(city.getId(), weatherItems);
                            localDataSource.insertForecast(forecastList);
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
    }

    private List<Forecast> parseForecastData(int cityId, List<WeatherItem> weatherItems) {
        List<Forecast> forecastList = new ArrayList<>();
        for (WeatherItem item : weatherItems) {
            double temperature = item.getTemperature().temperatureInCelsius();
            String icon = item.getMainIcon();
            Date date = item.getDate();
            forecastList.add(new Forecast(cityId, temperature, date, icon));
        }
        return forecastList;
    }

    public PeriodicWorkRequest createWorkRequest() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();
        // Schedule updates every 12 hours
        // WorkManager.getInstance(context).enqueue(workRequest);
        return new PeriodicWorkRequest.Builder(
                DatabaseSyncWorker.class, 12, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build();
    }
}
