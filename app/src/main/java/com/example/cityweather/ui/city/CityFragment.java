package com.example.cityweather.ui.city;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.cityweather.R;
import com.example.cityweather.data.local.model.City;

public class CityFragment extends Fragment {
    public static final String TAG = CityFragment.class.getSimpleName();
    private static final String KEY_SELECTED_CITY = "SELECTED_CITY";
    private City mCity;

    public CityFragment() {
        // Required empty public constructor
    }

    public static CityFragment newInstance(City city) {
        CityFragment fragment = new CityFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SELECTED_CITY, city);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) requireActivity().onBackPressed();

        mCity = getArguments().getParcelable(KEY_SELECTED_CITY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city, container, false);
    }
}
