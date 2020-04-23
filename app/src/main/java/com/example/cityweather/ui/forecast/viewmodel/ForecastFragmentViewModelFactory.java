package com.example.cityweather.ui.forecast.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.cityweather.data.Repository;
import com.example.cityweather.data.local.model.City;

public class ForecastFragmentViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final Repository repository;
    private final City city;

    public ForecastFragmentViewModelFactory(Repository repository, City city) {
        this.repository = repository;
        this.city = city;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ForecastFragmentViewModel(repository, city);
    }
}
