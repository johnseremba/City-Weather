package com.example.cityweather.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;

import com.example.cityweather.data.api.OnlineDataSourceContract;
import com.example.cityweather.data.api.RequestCallback;
import com.example.cityweather.data.api.ResponseCode;
import com.example.cityweather.data.local.LocalDataSourceContract;
import com.example.cityweather.data.local.model.City;
import com.example.cityweather.data.local.model.Forecast;
import com.example.cityweather.data.workers.DatabaseSyncWorker;
import com.example.cityweather.utils.AppExecutors;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Repository {
    private static final String TAG = Repository.class.getSimpleName();
    private static final Object LOCK = new Object();
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

    public OnlineDataSourceContract getOnlineDataSource() {
        return onlineDataSource;
    }

    public void triggerForecastFetch(final City city) {
        executors.getDiskIO().execute(() -> {
            int count = localDataSource.getCityForecastCount(city.getId());
            if (count < 1) {
                onlineDataSource.getWeatherData(city, new RequestCallback<List<Forecast>>() {
                    @Override
                    public void onSuccess(@Nullable List<Forecast> result) {
                        // store returned forecast
                        localDataSource.insertForecast(result);
                    }

                    @Override
                    public void onError(@NonNull ResponseCode responseCode, @Nullable String errorMessage) {
                        Log.e(TAG, "onError: " + responseCode + ": " + errorMessage);
                    }
                });
            }
        });
    }

    public PeriodicWorkRequest createWorkRequest() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();
        return new PeriodicWorkRequest.Builder(
                DatabaseSyncWorker.class, 6, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build();
    }
}
