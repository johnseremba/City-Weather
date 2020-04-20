package com.example.cityweather.data.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.cityweather.data.Repository;
import com.example.cityweather.data.local.model.City;
import com.example.cityweather.utils.InjectorUtils;

import java.util.List;

public class DatabaseSyncWorker extends Worker {
    private Repository repository;

    public DatabaseSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Refresh weather data for all cities bookmarked by the user
        repository = InjectorUtils.provideRepository(this.getApplicationContext());
        List<City> cities = repository.getLocalDataSource().getCitiesList();

        for (City city : cities) {
            // Delete old weather data
            repository.getLocalDataSource().deleteForecastByCityId(city.getId());

            // Fetch and cache new data
            repository.fetchWeatherDataForCity(city);
        }
        return Result.success();
    }
}
