package com.example.cityweather.ui.map;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.cityweather.data.Repository;

public class MapFragmentViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final Repository repository;

    public MapFragmentViewModelFactory(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MapFragmentViewModel(repository);
    }
}
