package com.example.cityweather.ui.forecast.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityweather.R;
import com.example.cityweather.ui.base.ItemClickListener;
import com.example.cityweather.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder> {
    private ItemClickListener<Date> itemClickListener;
    private List<Date> daysList = new ArrayList<>();

    public DaysAdapter(ItemClickListener<Date> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public DaysAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DaysAdapter.ViewHolder holder, int position) {
        holder.bind(daysList.get(position));
    }

    @Override
    public int getItemCount() {
        return daysList.size();
    }

    public void setDaysList(List<Date> daysList) {
        this.daysList = daysList;
        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_day_icon)
        ImageView imgDayIcon;
        @BindView(R.id.txt_day_title)
        TextView txtDayTitle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Date date) {
            String dayLabel = DateUtils.getDayString(date);
            txtDayTitle.setText(dayLabel);
            itemView.setOnClickListener(v -> itemClickListener.onItemClick(date));
        }
    }
}
