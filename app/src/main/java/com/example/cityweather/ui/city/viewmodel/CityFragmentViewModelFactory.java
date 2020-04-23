package com.example.cityweather.ui.city.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.cityweather.data.Repository;
import com.example.cityweather.data.local.model.City;

public class CityFragmentViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final Repository repository;
    private final City city;

    public CityFragmentViewModelFactory(Repository repository, City city) {
        this.repository = repository;
        this.city = city;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CityFragmentViewModel(repository, city);
    }
}
