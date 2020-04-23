package com.example.cityweather.ui.map;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.example.cityweather.BuildConfig;
import com.example.cityweather.R;
import com.example.cityweather.data.local.model.City;
import com.example.cityweather.utils.InjectorUtils;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    public static final String TAG = MapFragment.class.getSimpleName();
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private static final String FIRST_TIME_ACCESS = "FIRST_TIME_ACCESS";
    private BottomSheetBehavior<ConstraintLayout> mBottomSheetBehavior;
    private MapFragmentInteractionListener mListener;
    private MapFragmentViewModel mViewModel;
    private List<Place.Field> mPlaceFields = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG);

    @BindView(R.id.txt_city_name)
    TextView mTxtCityName;
    @BindView(R.id.txt_city_desc)
    TextView mTxtCityDesc;
    @BindView(R.id.fab_focus_map)
    FloatingActionButton mFabFocusMap;
    @BindView(R.id.fab_cities_list)
    FloatingActionButton mFabCitiesList;
    @BindView(R.id.txt_bookmark_text)
    TextView mTxtBookmark;
    @BindView(R.id.bottom_sheet)
    ConstraintLayout mBottomSheet;

    private CountingIdlingResource idlingResource = InjectorUtils.provideIdlingResource();

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment getInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // @ForTesting: Starting idling resource when loading map
        if (idlingResource != null) {
            idlingResource.increment();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        mViewModel = ViewModelProviders.of(
                requireActivity(),
                InjectorUtils.provideMapFragmentViewModelFactory(requireContext().getApplicationContext()))
                .get(MapFragmentViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkInitialAccessAndScheduleJob();
        initEventListeners();
        initAutoCompleteWidget();
        iniUi();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mViewModel.setGoogleMap(googleMap);
        if (idlingResource != null) {
            idlingResource.decrement();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(requireContext(), getString(R.string.msg_permission_required), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void iniUi() {
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        if (mViewModel.getGoogleMap() == null) {
            // Initialize map and get current location in case it is not yet initialized
            initMapAndPlaces();
            getCurrentLocation();
        } else {
            // Update interface with either the user's current location or selected place
            if (mViewModel.getSelectedPlace() != null) {
                updateCurrentLocation(mViewModel.getSelectedPlace());
            } else {
                updateCurrentLocation(mViewModel.getUserHomeLocation());
            }
        }
    }

    private void checkInitialAccessAndScheduleJob() {
        SharedPreferences prefs = InjectorUtils.provideSharedPreferences(requireContext().getApplicationContext());
        boolean initialAccess = prefs.getBoolean(FIRST_TIME_ACCESS, false);
        if (initialAccess) return;
        prefs.edit().putBoolean(FIRST_TIME_ACCESS, true).apply();
        mViewModel.scheduleForecastSyncJob(requireContext().getApplicationContext());
    }

    private void initEventListeners() {
        mFabFocusMap.setOnClickListener(v -> updateCameraPosition(mViewModel.getUserHomeLocation()));
        mFabCitiesList.setOnClickListener(v -> showBookmarkedCities(null));
        mTxtBookmark.setOnClickListener(v -> bookMarkCity());
    }

    private void bookMarkCity() {
        Place selectedPlace = mViewModel.getSelectedPlace();
        if (selectedPlace == null || selectedPlace.getLatLng() == null) {
            Toast.makeText(getContext(), getString(R.string.msg_select_city), Toast.LENGTH_SHORT).show();
            return;
        }
        City city = new City(
                new Date(),
                selectedPlace.getName(),
                selectedPlace.getAddress(),
                selectedPlace.getLatLng().latitude,
                selectedPlace.getLatLng().longitude);

        // Fetch Weather information for selected city
        mViewModel.bookMarkAndGetWeatherInformation(city);
        // Show Fragment to enter description for city
        mListener.showDetailsFragment(city);
    }

    private void initMapAndPlaces() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        if (!Places.isInitialized()) {
            Places.initialize(requireActivity().getApplicationContext(), BuildConfig.GOOGLE_MAPS_API_KEY);
            mViewModel.setPlacesClient(Places.createClient(requireContext().getApplicationContext()));
        }
    }

    private void getCurrentLocation() {
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(mPlaceFields).build();
        if (isLocationPermissionGranted()) {
            Task<FindCurrentPlaceResponse> placeResponse = mViewModel.getPlacesClient().findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FindCurrentPlaceResponse response = task.getResult();
                    if (response != null && response.getPlaceLikelihoods().size() > 0) {
                        PlaceLikelihood placeLikelihood = response.getPlaceLikelihoods().get(0);
                        Place place = placeLikelihood.getPlace();
                        mViewModel.setUserHomeLocation(place);
                        updateCurrentLocation(place);
                    }
                } else {
                    Toast.makeText(requireContext(), R.string.err_failed_to_get_location, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            getLocationPermission();
        }
    }

    private void getLocationPermission() {
        if (!isLocationPermissionGranted()) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(
                        requireActivity(),
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        },
                        REQUEST_LOCATION_PERMISSION
                );
            }
        }
    }

    private void initAutoCompleteWidget() {
        AutocompleteSupportFragment autocompleteFragment =
                (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(mPlaceFields);
        autocompleteFragment.setTypeFilter(TypeFilter.CITIES);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                mViewModel.setSelectedPlace(place);
                updateBottomSheet(place);
                updateCameraPosition(place);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e(TAG, "onError: " + status);
                Toast.makeText(requireContext(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isLocationPermissionGranted() {
        return ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void updateCurrentLocation(@NonNull Place place) {
        mViewModel.setSelectedPlace(place);
        updateBottomSheet(place);
        updateCameraPosition(place);
    }

    private void updateBottomSheet(Place place) {
        if (place == null) return;
        mTxtCityName.setText(place.getName());
        mTxtCityDesc.setText(place.getAddress());
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void updateCameraPosition(Place place) {
        if (place == null || place.getLatLng() == null) {
            Toast.makeText(getContext(), getString(R.string.msg_wrong_location), Toast.LENGTH_SHORT).show();
            return;
        }

        mViewModel.getGoogleMap().clear();
        mViewModel.getGoogleMap().addMarker(new MarkerOptions()
                .position(place.getLatLng())
                .title(place.getAddress()));
        mViewModel.getGoogleMap().animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
    }

    private void showBookmarkedCities(@Nullable String query) {
        mListener.showCityListFragment(query);
    }

    public void setFragmentInteractionListener(@NonNull MapFragmentInteractionListener listener) {
        mListener = listener;
    }

    public interface MapFragmentInteractionListener {
        void showDetailsFragment(@NonNull City city);

        void showCityListFragment(@Nullable String searchQuery);
    }
}
