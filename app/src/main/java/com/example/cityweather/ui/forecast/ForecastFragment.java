package com.example.cityweather.ui.forecast;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityweather.R;
import com.example.cityweather.data.local.model.City;
import com.example.cityweather.data.local.model.Forecast;
import com.example.cityweather.ui.MainActivity;
import com.example.cityweather.ui.forecast.adapter.DaysAdapter;
import com.example.cityweather.ui.forecast.adapter.HourlyForecastAdapter;
import com.example.cityweather.ui.forecast.viewmodel.ForecastFragmentViewModel;
import com.example.cityweather.utils.DateUtils;
import com.example.cityweather.utils.InjectorUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForecastFragment extends Fragment {
    public static final String TAG = ForecastFragment.class.getSimpleName();
    private static final String KEY_SELECTED_CITY = "KEY_SELECTED_CITY";
    private ForecastFragmentViewModel mViewModel;
    private DaysAdapter mDaysAdapter;
    private HourlyForecastAdapter mHourlyForecastAdapter;
    private ForecastFragmentInteractionListener mInteractionListener;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.weather_icon)
    ImageView mWeatherIcon;
    @BindView(R.id.txt_city_name)
    TextView mTxtCityName;
    @BindView(R.id.txt_date)
    TextView mTxtDate;
    @BindView(R.id.txt_temperature)
    TextView mTxtTemperature;
    @BindView(R.id.txt_more_info)
    TextView mTxtMoreInfo;
    @BindView(R.id.hourly_focus_recycler_view)
    RecyclerView mHourlyForecastRecyclerView;
    @BindView(R.id.days_recycler_view)
    RecyclerView mDaysRecyclerView;

    public ForecastFragment() {
        // Required empty public constructor
    }

    public void setInteractionListener(ForecastFragmentInteractionListener mInteractionListener) {
        this.mInteractionListener = mInteractionListener;
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
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            // If no city is passed into the bundle, close fragment.
            requireActivity().onBackPressed();
            return;
        }
        City city = bundle.getParcelable(KEY_SELECTED_CITY);
        mViewModel = ViewModelProviders.of(
                this,
                InjectorUtils.provideForecastFragmentViewModelFactory(requireContext(), city))
                .get(ForecastFragmentViewModel.class);
        initAppBar(city);
        initListeners();
        initRecyclerViews();
        loadForecastData();
    }

    private void initAppBar(City city) {
        mToolbar.setTitle(city.getName());
        ((MainActivity) requireActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((MainActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            setHasOptionsMenu(true);
        }
    }

    private void initListeners() {
        mTxtMoreInfo.setOnClickListener(v ->
                mInteractionListener.showDetailFragment(mViewModel.getCity()));
    }

    private void initRecyclerViews() {
        mHourlyForecastAdapter = new HourlyForecastAdapter();
        mHourlyForecastRecyclerView.setAdapter(mHourlyForecastAdapter);

        mDaysAdapter = new DaysAdapter(this::loadDateForecast);
        mDaysRecyclerView.setAdapter(mDaysAdapter);
    }

    private void loadForecastData() {
        mViewModel.getForecastData().observe(getViewLifecycleOwner(), forecastList -> {
            if (forecastList == null || forecastList.size() == 0) return;
            mViewModel.groupForecastDataToDays(forecastList);

            Forecast firstEntry = mViewModel.getWeatherData().pollFirstEntry().getValue().get(0);
            updateWeatherUI(firstEntry);

            Date firstDay = mViewModel.getWeatherData().firstKey();
            List<Forecast> forecastItems = mViewModel.getWeatherData().get(firstDay);
            mHourlyForecastAdapter.setForecastList(forecastItems);

            List<Date> dateList = new ArrayList<>();
            for (Map.Entry entry : mViewModel.getWeatherData().entrySet()) {
                dateList.add((Date) entry.getKey());
            }
            mDaysAdapter.setDaysList(dateList);
        });
    }

    private void loadDateForecast(Date date) {
        List<Forecast> forecastList = mViewModel.getWeatherData().get(date);
        mHourlyForecastAdapter.setForecastList(forecastList);
        updateWeatherUI(forecastList.get(0));
    }

    private void updateWeatherUI(Forecast forecast) {
        Resources res = getResources();
        int temp = (int) forecast.getTemperature();
        mTxtCityName.setText(mViewModel.getCity().getName());
        mTxtTemperature.setText(
                String.format(res.getString(R.string.temperature_string), temp));
        mTxtDate.setText(DateUtils.getNormalDate(forecast.getDatetime()));
        Picasso.get()
                .load(forecast.getIconUrl())
                .into(mWeatherIcon);
    }

    public interface ForecastFragmentInteractionListener {
        void showDetailFragment(City city);
    }
}
