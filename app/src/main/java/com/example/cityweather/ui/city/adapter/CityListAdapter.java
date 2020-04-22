package com.example.cityweather.ui.city.adapter;

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityweather.R;
import com.example.cityweather.data.local.model.City;
import com.example.cityweather.ui.base.ItemClickListener;
import com.example.cityweather.utils.WeatherUtils;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {
    private final ItemClickListener<City> itemClickListener;
    private List<City> citiesList = Collections.emptyList();

    public CityListAdapter(ItemClickListener<City> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CityListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityListAdapter.ViewHolder holder, int position) {
        holder.bind(citiesList.get(position));
    }

    @Override
    public int getItemCount() {
        return citiesList != null ? citiesList.size() : 0;
    }

    public void setCitiesList(List<City> citiesList) {
        this.citiesList = citiesList;
        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.city_name)
        TextView cityName;
        @BindView(R.id.city_description)
        TextView cityDesc;
        @BindView(R.id.city_icon)
        ImageView cityIcon;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(City city) {
            cityName.setText(city.getName());
            cityDesc.setText(city.getDescription());
            cityIcon.setColorFilter(
                    ContextCompat.getColor(itemView.getContext(), WeatherUtils.getRandomColor()),
                    PorterDuff.Mode.SRC_IN);
            itemView.setOnClickListener(v -> itemClickListener.onItemClick(city));
        }
    }
}
