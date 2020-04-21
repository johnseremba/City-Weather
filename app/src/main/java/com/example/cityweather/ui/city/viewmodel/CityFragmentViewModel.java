package com.example.cityweather.ui.city.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.cityweather.data.Repository;
import com.example.cityweather.data.local.model.City;

public class CityFragmentViewModel extends ViewModel {
    private final Repository repository;
    private LiveData<City> city;

    CityFragmentViewModel(Repository repository, City city) {
        this.repository = repository;
        // load city from database
        this.city = this.repository.getLocalDataSource()
                .getCityByCoordinates(city.getLatitude(), city.getLongitude());
    }

    public LiveData<City> getCity() {
        return city;
    }

    public void updateCity(City city) {
        repository.getLocalDataSource().updateCity(city);
    }
}
