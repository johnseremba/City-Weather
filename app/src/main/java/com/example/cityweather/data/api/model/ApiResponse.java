package com.example.cityweather.data.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse implements Parcelable {
    @SerializedName("list")
    private List<WeatherItem> weatherItemList;

    public ApiResponse(List<WeatherItem> weatherItemList) {
        this.weatherItemList = weatherItemList;
    }

    protected ApiResponse(Parcel in) {
        weatherItemList = in.createTypedArrayList(WeatherItem.CREATOR);
    }

    public static final Creator<ApiResponse> CREATOR = new Creator<ApiResponse>() {
        @Override
        public ApiResponse createFromParcel(Parcel in) {
            return new ApiResponse(in);
        }

        @Override
        public ApiResponse[] newArray(int size) {
            return new ApiResponse[size];
        }
    };

    public List<WeatherItem> getWeatherItemList() {
        return weatherItemList;
    }

    public void setWeatherItemList(List<WeatherItem> weatherItemList) {
        this.weatherItemList = weatherItemList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(weatherItemList);
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "weatherItemList=" + weatherItemList +
                '}';
    }
}
