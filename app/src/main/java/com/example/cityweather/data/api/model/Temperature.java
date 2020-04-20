package com.example.cityweather.data.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.cityweather.utils.WeatherUtils;
import com.google.gson.annotations.SerializedName;

public class Temperature implements Parcelable {
    @SerializedName("temp")
    private Double tempInKelvin;

    public Temperature(Double tempInKelvin) {
        this.tempInKelvin = tempInKelvin;
    }

    protected Temperature(Parcel in) {
        if (in.readByte() == 0) {
            tempInKelvin = null;
        } else {
            tempInKelvin = in.readDouble();
        }
    }

    public static final Creator<Temperature> CREATOR = new Creator<Temperature>() {
        @Override
        public Temperature createFromParcel(Parcel in) {
            return new Temperature(in);
        }

        @Override
        public Temperature[] newArray(int size) {
            return new Temperature[size];
        }
    };

    public Double getTempInKelvin() {
        return tempInKelvin;
    }

    public void setTempInKelvin(Double tempInKelvin) {
        this.tempInKelvin = tempInKelvin;
    }

    public double temperatureInCelsius() {
        return WeatherUtils.getTemperatureInCelsius(this.tempInKelvin);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (tempInKelvin == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(tempInKelvin);
        }
    }

    @Override
    public String toString() {
        return "Temperature{" +
                "tempInKelvin=" + tempInKelvin +
                '}';
    }
}
