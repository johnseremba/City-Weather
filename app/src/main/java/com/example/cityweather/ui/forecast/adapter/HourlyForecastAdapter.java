package com.example.cityweather.ui.forecast.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityweather.R;
import com.example.cityweather.data.local.model.Forecast;
import com.example.cityweather.utils.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder> {
    private List<Forecast> forecastList = new ArrayList<>();

    public HourlyForecastAdapter() {
    }

    @NonNull
    @Override
    public HourlyForecastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hourly_focus, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyForecastAdapter.ViewHolder holder, int position) {
        holder.bind(forecastList.get(position));
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public void setForecastList(List<Forecast> forecastList) {
        this.forecastList = forecastList;
        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_hour)
        TextView txtHour;
        @BindView(R.id.hourly_temperature)
        TextView txtTemperature;
        @BindView(R.id.img_hourly_icon)
        ImageView imgIcon;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Forecast forecast) {
            Resources res = itemView.getResources();
            txtTemperature.setText(String.format(
                    res.getString(R.string.temperature_string),
                    (int) forecast.getTemperature()));
            txtHour.setText(DateUtils.getHours(forecast.getDatetime()));
            Picasso.get().load(forecast.getIconUrl()).into(imgIcon);
        }
    }
}
