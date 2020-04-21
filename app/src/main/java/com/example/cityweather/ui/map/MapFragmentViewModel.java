package com.example.cityweather.ui.map;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.work.WorkManager;

import com.example.cityweather.data.Repository;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;

public class MapFragmentViewModel extends ViewModel {
    private final Repository repository;
    private PlacesClient placesClient;
    private Place userHomeLocation;
    private Place selectedPlace;
    private GoogleMap googleMap;

    MapFragmentViewModel(Repository repository) {
        this.repository = repository;
    }

    public void scheduleForecastSyncJob(Context context) {
        WorkManager.getInstance(context).enqueue(repository.createWorkRequest());
    }

    public PlacesClient getPlacesClient() {
        return placesClient;
    }

    public void setPlacesClient(PlacesClient placesClient) {
        this.placesClient = placesClient;
    }

    public Place getUserHomeLocation() {
        return userHomeLocation;
    }

    public void setUserHomeLocation(Place userHomeLocation) {
        this.userHomeLocation = userHomeLocation;
    }

    public Place getSelectedPlace() {
        return selectedPlace;
    }

    public void setSelectedPlace(Place selectedPlace) {
        this.selectedPlace = selectedPlace;
    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
}
