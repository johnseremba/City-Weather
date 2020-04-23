package com.example.cityweather;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class CityWeatherApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }
}
