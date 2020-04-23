package com.example.cityweather.ui.city.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.cityweather.data.Repository;

public class CityListFragmentViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final Repository repository;

    public CityListFragmentViewModelFactory(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CityListFragmentViewModel(repository);
    }
}
