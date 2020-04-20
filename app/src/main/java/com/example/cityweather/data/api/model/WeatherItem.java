package com.example.cityweather.data.api.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class WeatherItem implements Parcelable {
    @SerializedName("main")
    private Temperature temperature;

    @SerializedName("weather")
    private List<Weather> weatherList;

    @SerializedName("dt_txt")
    private Date date;

    public WeatherItem(Temperature temperature, List<Weather> weatherList, Date date) {
        this.temperature = temperature;
        this.weatherList = weatherList;
        this.date = date;
    }

    protected WeatherItem(Parcel in) {
        temperature = in.readParcelable(Temperature.class.getClassLoader());
        weatherList = in.createTypedArrayList(Weather.CREATOR);
    }

    public static final Creator<WeatherItem> CREATOR = new Creator<WeatherItem>() {
        @Override
        public WeatherItem createFromParcel(Parcel in) {
            return new WeatherItem(in);
        }

        @Override
        public WeatherItem[] newArray(int size) {
            return new WeatherItem[size];
        }
    };

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public List<Weather> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Uri getMainIcon() {
        return weatherList.size() > 0
                ? weatherList.get(0).getIcon()
                : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(temperature, flags);
        dest.writeTypedList(weatherList);
    }

    @Override
    public String toString() {
        return "WeatherItem{" +
                "temperature=" + temperature +
                ", weatherList=" + weatherList +
                ", date=" + date +
                '}';
    }
}
