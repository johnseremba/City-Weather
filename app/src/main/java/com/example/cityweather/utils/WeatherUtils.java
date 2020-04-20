package com.example.cityweather.utils;

public class WeatherUtils {
    public static int getTemperatureInCelsius(Double tempInKelvin) {
        return (int) Math.round(tempInKelvin - 273.15);
    }
}
