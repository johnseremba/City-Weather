package com.example.cityweather.ui.city;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.cityweather.R;
import com.example.cityweather.data.local.model.City;
import com.example.cityweather.ui.MainActivity;
import com.example.cityweather.ui.city.viewmodel.CityFragmentViewModel;
import com.example.cityweather.utils.InjectorUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityFragment extends Fragment {
    public static final String TAG = CityFragment.class.getSimpleName();
    private static final String KEY_SELECTED_CITY = "SELECTED_CITY";
    private CityFragmentViewModel mViewModel;
    private City mCity;
    private CityFragmentInteractionListener mListener;
    private boolean isNewEntry = false;
    private boolean updateEntry = false;

    @BindView(R.id.txt_city_desc)
    TextView mTxtCityDesc;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab_add)
    FloatingActionButton mFabAdd;
    @BindView(R.id.btn_show_forecast)
    Button mButtonShowForecast;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city, container, false);
        ButterKnife.bind(this, view);
        City city = getArguments().getParcelable(KEY_SELECTED_CITY);
        updateAppBar(city);
        mViewModel = ViewModelProviders.of(
                this,
                InjectorUtils.provideCityFragmentViewModelFactory(requireContext(), city))
                .get(CityFragmentViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListeners();
        initViewModelObservers();
    }

    private void updateAppBar(City city) {
        mToolbar.setTitle(city.getName());
        ((MainActivity) requireActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((MainActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            setHasOptionsMenu(true);
        }
    }

    public void setFragmentInteractionListener(CityFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    private void initListeners() {
        mFabAdd.setOnClickListener(v -> {
            if (!isNewEntry && !updateEntry) {
                // Mark entry as updatable so that we don't create a new city entry in the db
                updateEntry = true;

                // Toggle fab icon to a save icon when the user starts to edit a city
                updateFabIcon(true);

                // Enable Text input edit text so that a user can update the description
                mTxtCityDesc.setEnabled(true);
            } else {
                // Update city entry
                updateCity(mCity);
            }
        });

        mButtonShowForecast.setOnClickListener(v -> mListener.showForecastFragment(mCity));
    }

    private void initViewModelObservers() {
        mViewModel.getCity().observe(getViewLifecycleOwner(), city -> {
            mCity = city;
            isNewEntry = (city.getDescription() == null || city.getDescription().isEmpty());
            updateFabIcon(isNewEntry);
            updateUI(isNewEntry);
        });
    }

    private void updateUI(boolean isEnabled) {
        mTxtCityDesc.setText(mCity.getDescription());
        mTxtCityDesc.setEnabled(isEnabled);
    }

    private void updateCity(City city) {
        TextInputLayout textInputLayout = (TextInputLayout) mTxtCityDesc.getParent().getParent();
        textInputLayout.setError(null);
        if (mTxtCityDesc.getText() == null || mTxtCityDesc.getText().toString().isEmpty()) {
            textInputLayout.setError(getString(R.string.err_description_required));
            return;
        }
        city.setDescription(mTxtCityDesc.getText().toString());
        mViewModel.updateCity(city);
        Toast.makeText(getContext(), getString(R.string.msg_city_updated), Toast.LENGTH_SHORT).show();

        // reset updatable flag
        updateEntry = false;
    }

    private void updateFabIcon(boolean status) {
        Drawable icon = getResources().getDrawable(
                status ? R.drawable.ic_save_black_24dp : R.drawable.ic_edit_black_24dp, null);
        mFabAdd.setImageDrawable(icon);
    }

    public interface CityFragmentInteractionListener {
        void showForecastFragment(City city);
    }
}
