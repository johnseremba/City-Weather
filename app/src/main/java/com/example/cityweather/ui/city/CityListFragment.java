package com.example.cityweather.ui.city;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cityweather.R;

public class CityListFragment extends Fragment {
    public static final String TAG = CityListFragment.class.getSimpleName();
    private static final String KEY_SEARCH_QUERY = "KEY_SEARCH_QUERY";
    private String mSearchParam;

    public CityListFragment() {
        // Required empty public constructor
    }

    public static CityListFragment newInstance(@Nullable String searchQuery) {
        CityListFragment fragment = new CityListFragment();
        Bundle args = new Bundle();
        args.putString(KEY_SEARCH_QUERY, searchQuery);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           mSearchParam = getArguments().getString(KEY_SEARCH_QUERY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city_list, container, false);
    }
}
