package com.example.cityweather.data.api.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Weather implements Parcelable {
    @SerializedName("icon")
    private String icon;

    public Weather(String icon) {
        this.icon = icon;
    }

    protected Weather(Parcel in) {
        icon = in.readString();
    }

    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Uri getIcon() {
        String iconUri = String.format("https://openweathermap.org/img/wn/%s@2x.png", this.icon);
        return Uri.parse(iconUri);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(icon);
    }

    @Override
    public String toString() {
        return "Weather{" +
                "icon='" + icon + '\'' +
                '}';
    }
}
