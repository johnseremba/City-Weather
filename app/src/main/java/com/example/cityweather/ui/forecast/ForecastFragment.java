package com.example.cityweather.ui.forecast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.cityweather.R;
import com.example.cityweather.data.local.model.City;

public class ForecastFragment extends Fragment {
    public static final String TAG = ForecastFragment.class.getSimpleName();
    private static final String KEY_SELECTED_CITY = "KEY_SELECTED_CITY";

    public ForecastFragment() {
        // Required empty public constructor
    }

    public static ForecastFragment newInstance(City city) {
        ForecastFragment fragment = new ForecastFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SELECTED_CITY, city);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forecast, container, false);
    }
}
