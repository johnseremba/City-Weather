package com.example.cityweather.ui.city.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cityweather.data.Repository;
import com.example.cityweather.data.local.model.City;

import java.util.List;

public class CityListFragmentViewModel extends ViewModel {
    private final Repository repository;
    private final MutableLiveData<List<City>> citiesList = new MutableLiveData<>();

    CityListFragmentViewModel(Repository repository) {
        this.repository = repository;
    }

    public LiveData<List<City>> getCities() {
        return citiesList;
    }

    public void setCitiesList(List<City> cities) {
        citiesList.setValue(cities);
    }

    public LiveData<List<City>> loadAllCities() {
        return repository.getLocalDataSource().getCities();
    }

    public LiveData<List<City>> searchForCity(String searchQuery) {
        return repository.getLocalDataSource().searchCityByName(searchQuery);
    }

    public void deleteCities(List<City> citiesToDelete) {
        repository.getLocalDataSource().deleteCities(citiesToDelete);
    }
}
