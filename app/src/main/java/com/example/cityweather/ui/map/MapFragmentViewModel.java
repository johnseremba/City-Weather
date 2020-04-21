package com.example.cityweather.ui.map;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.work.WorkManager;

import com.example.cityweather.data.Repository;
import com.example.cityweather.data.local.model.City;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.concurrent.Executors;

public class MapFragmentViewModel extends ViewModel {
    private final Repository repository;
    private PlacesClient placesClient;
    private Place userHomeLocation;
    private Place selectedPlace;
    private GoogleMap googleMap;

    MapFragmentViewModel(Repository repository) {
        this.repository = repository;
    }

    void scheduleForecastSyncJob(Context context) {
        WorkManager.getInstance(context).enqueue(repository.createWorkRequest());
    }

    void bookMarkAndGetWeatherInformation(City city) {
        // Check if city is already bookmarked,
        // if not save it in local db and get weather information
        Executors.newSingleThreadExecutor().execute(() -> {
            if (!isCityBookmarked(city)) {
                long id = repository.getLocalDataSource().createCity(city);
                city.setId((int) id);
                repository.triggerForecastFetch(city);
            }
        });
    }

    private boolean isCityBookmarked(City city) {
        int count = repository.getLocalDataSource().getCityCount(city.getLatitude(), city.getLongitude());
        return count > 0;
    }

    PlacesClient getPlacesClient() {
        return placesClient;
    }

    void setPlacesClient(PlacesClient placesClient) {
        this.placesClient = placesClient;
    }

    Place getUserHomeLocation() {
        return userHomeLocation;
    }

    void setUserHomeLocation(Place userHomeLocation) {
        this.userHomeLocation = userHomeLocation;
    }

    Place getSelectedPlace() {
        return selectedPlace;
    }

    void setSelectedPlace(Place selectedPlace) {
        this.selectedPlace = selectedPlace;
    }

    GoogleMap getGoogleMap() {
        return googleMap;
    }

    void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
}
