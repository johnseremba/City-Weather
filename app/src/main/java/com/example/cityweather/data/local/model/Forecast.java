package com.example.cityweather.data.local.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "forecast",
        foreignKeys = @ForeignKey(
                entity = City.class,
                parentColumns = "city_id",
                childColumns = "city_id",
                onDelete = CASCADE))
public class Forecast implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "forecast_id")
    private int id;

    @ColumnInfo(name = "city_id")
    private int cityId;

    private double temperature;
    private Date datetime;

    @ColumnInfo(name = "icon_url")
    private String iconUrl;

    @Ignore
    public Forecast(int cityId, double temperature, Date datetime, String iconUrl) {
        this.cityId = cityId;
        this.temperature = temperature;
        this.datetime = datetime;
        this.iconUrl = iconUrl;
    }

    public Forecast(int id, int cityId, double temperature, Date datetime, String iconUrl) {
        this.id = id;
        this.cityId = cityId;
        this.temperature = temperature;
        this.datetime = datetime;
        this.iconUrl = iconUrl;
    }

    protected Forecast(Parcel in) {
        id = in.readInt();
        cityId = in.readInt();
        temperature = in.readDouble();
        iconUrl = in.readString();
    }

    public static final Creator<Forecast> CREATOR = new Creator<Forecast>() {
        @Override
        public Forecast createFromParcel(Parcel in) {
            return new Forecast(in);
        }

        @Override
        public Forecast[] newArray(int size) {
            return new Forecast[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "id=" + id +
                ", cityId=" + cityId +
                ", temperature=" + temperature +
                ", datetime=" + datetime +
                ", iconUrl='" + iconUrl + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(cityId);
        dest.writeDouble(temperature);
        dest.writeString(iconUrl);
    }
}
