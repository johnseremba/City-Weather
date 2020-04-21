package com.example.cityweather.data.workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.cityweather.data.Repository;
import com.example.cityweather.data.api.RequestCallback;
import com.example.cityweather.data.api.ResponseCode;
import com.example.cityweather.data.local.model.City;
import com.example.cityweather.data.local.model.Forecast;
import com.example.cityweather.utils.InjectorUtils;

import java.util.List;

public class DatabaseSyncWorker extends Worker {
    private static final String TAG = DatabaseSyncWorker.class.getSimpleName();
    private Repository repository;

    public DatabaseSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        repository = InjectorUtils.provideRepository(this.getApplicationContext());
        List<City> cities = repository.getLocalDataSource().getCitiesList();

        // Refresh weather data for all cities bookmarked by the user
        for (final City city : cities) {
            repository.getOnlineDataSource().getWeatherData(city, new RequestCallback<List<Forecast>>() {
                @Override
                public void onSuccess(@Nullable List<Forecast> forecastList) {
                    if (forecastList != null) {
                        // Update database with new forecast data
                        repository.getLocalDataSource().deleteForecastByCityId(city.getId());
                        repository.getLocalDataSource().insertForecast(forecastList);
                    }
                }

                @Override
                public void onError(@NonNull ResponseCode responseCode, @Nullable String errorMessage) {
                    Log.e(TAG, responseCode + ": " + errorMessage);
                }
            });
        }
        return Result.success();
    }
}
