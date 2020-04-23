package com.example.cityweather.ui.forecast.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.cityweather.data.Repository;
import com.example.cityweather.data.local.model.City;
import com.example.cityweather.data.local.model.Forecast;
import com.example.cityweather.utils.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ForecastFragmentViewModel extends ViewModel {
    private final Repository repository;
    private LiveData<List<Forecast>> allCityForecast;
    private City city;

    // Tree map keeps the keys sorted
    private TreeMap<Date, List<Forecast>> forecastTreeMap = new TreeMap<>();

    ForecastFragmentViewModel(Repository repository, City city) {
        this.repository = repository;
        this.city = city;
        allCityForecast = repository.getLocalDataSource().getCityForecast(city.getId());
    }

    public City getCity() {
        return city;
    }

    public LiveData<List<Forecast>> getForecastData() {
        return allCityForecast;
    }

    public void groupForecastDataToDays(List<Forecast> forecastList) {
        forecastTreeMap.clear();
        for (Forecast forecast : forecastList) {
            Date shortDate = DateUtils.getShortDate(forecast.getDatetime());
            if (forecastTreeMap.containsKey(shortDate)) {
                forecastTreeMap.get(shortDate).add(forecast);
            } else {
                forecastTreeMap.put(shortDate, new ArrayList<Forecast>());
                forecastTreeMap.get(shortDate).add(forecast);
            }
        }
        // Sort ArrayList entries
        for (Map.Entry element : forecastTreeMap.entrySet()) {
            List<Forecast> forecastItem = (List<Forecast>) element.getValue();
            Collections.sort(forecastItem, weatherItemComparator);
        }
    }

    public TreeMap<Date, List<Forecast>> getWeatherData() {
        return forecastTreeMap;
    }

    private Comparator<Forecast> weatherItemComparator =
            (item1, item2) -> item1.getDatetime().compareTo(item2.getDatetime());
}
