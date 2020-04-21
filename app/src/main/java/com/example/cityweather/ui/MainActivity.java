package com.example.cityweather.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.cityweather.R;
import com.example.cityweather.data.local.model.City;
import com.example.cityweather.ui.city.CityFragment;
import com.example.cityweather.ui.city.CityListFragment;
import com.example.cityweather.ui.forecast.ForecastFragment;
import com.example.cityweather.ui.map.MapFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();

        // Handle configuration changes, don't instantiate default fragment
        if (savedInstanceState == null) {
            MapFragment mapFragment = MapFragment.getInstance();
            addFragment(mapFragment, MapFragment.TAG, true);
        }
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof MapFragment) {
            ((MapFragment) fragment).setFragmentInteractionListener(mMapFragmentInteractionListener);
        } else if (fragment instanceof CityFragment) {
            ((CityFragment) fragment).setFragmentInteractionListener(mCityFragmentInteractionListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();
        if (backStackEntryCount == 1) {
            finish();
        } else {
            fragmentManager.popBackStackImmediate();
        }
    }

    private void addFragment(Fragment fragment, String tag, boolean addToBackStack) {
        if (addToBackStack) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment, tag)
                    .addToBackStack(tag)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment, tag)
                    .commit();
        }
    }

    private void toggleProgressBar(boolean showProgress) {
        progressBar.setVisibility(showProgress ? View.VISIBLE : View.GONE);
    }

    private MapFragment.MapFragmentInteractionListener mMapFragmentInteractionListener =
            new MapFragment.MapFragmentInteractionListener() {
                @Override
                public void showDetailsFragment(@NonNull City city) {
                    addFragment(CityFragment.newInstance(city), CityFragment.TAG, true);
                }

                @Override
                public void showCityListFragment(@Nullable String searchQuery) {
                    addFragment(CityListFragment.newInstance(searchQuery), CityListFragment.TAG, true);
                }
            };

    private CityFragment.CityFragmentInteractionListener mCityFragmentInteractionListener =
            city -> {
                addFragment(ForecastFragment.newInstance(city), ForecastFragment.TAG, true);
            };
}
