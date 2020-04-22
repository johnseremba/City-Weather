package com.example.cityweather.ui.city;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityweather.R;
import com.example.cityweather.data.local.model.City;
import com.example.cityweather.ui.MainActivity;
import com.example.cityweather.ui.city.adapter.CityListAdapter;
import com.example.cityweather.ui.city.adapter.RecyclerTouchHelper;
import com.example.cityweather.ui.city.viewmodel.CityListFragmentViewModel;
import com.example.cityweather.utils.InjectorUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CityListFragment extends Fragment implements RecyclerTouchHelper.RecyclerItemTouchHelperListener {
    public static final String TAG = CityListFragment.class.getSimpleName();
    private static final String KEY_SEARCH_QUERY = "KEY_SEARCH_QUERY";
    private String mSearchParam;
    private CityListFragmentViewModel mViewMode;
    private CityListFragmentInteractionListener mFragmentInteractionListener;
    private CityListAdapter mCityListAdapter;
    private List<City> mCitiesToDelete = new ArrayList<>();

    public void setFragmentInteractionListener(CityListFragmentInteractionListener listener) {
        this.mFragmentInteractionListener = listener;
    }

    @BindView(R.id.cities_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.txt_no_content)
    TextView mTxtNoContent;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.coordinator_view)
    CoordinatorLayout mCoordinatorLayout;

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
        View view = inflater.inflate(R.layout.fragment_city_list, container, false);
        ButterKnife.bind(this, view);
        mViewMode = ViewModelProviders.of(
                this,
                InjectorUtils.provideCityListFragmentViewModelFactory(requireContext()))
                .get(CityListFragmentViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAppBar();
        initRecyclerView();
        initCitiesObserver();
        if (mSearchParam == null || mSearchParam.isEmpty()) {
            getAllCities();
        } else {
            searchForCity(mSearchParam);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.cities_menu, menu);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setQueryRefinementEnabled(true);
            searchView.setIconifiedByDefault(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Delete cities scheduled to be deleted when the fragment pauses
        if (mCitiesToDelete != null && mCitiesToDelete.size() > 0) {
            mViewMode.deleteCities(mCitiesToDelete);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        // Row is swiped from recycler view
        if (viewHolder instanceof CityListAdapter.ViewHolder) {
            final City deletedCity = mCityListAdapter.getItemAtPosition(position);
            mCitiesToDelete.add(deletedCity);
            mCityListAdapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar snackbar = Snackbar.make(
                    mCoordinatorLayout,
                    deletedCity.getName() + getString(R.string.msg_city_deleted),
                    Snackbar.LENGTH_LONG);

            snackbar.setAction(R.string.btn_undo, v -> {
                mCitiesToDelete.remove(deletedCity);
                mCityListAdapter.restoreItem(deletedCity, position);
            });

            snackbar.setActionTextColor(getResources().getColor(R.color.accent, null));
            snackbar.show();
        }
    }

    private void initAppBar() {
        mToolbar.setTitle(getString(R.string.title_bookmarked_cities));
        ((MainActivity) requireActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((MainActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            setHasOptionsMenu(true);
        }
    }

    private void initRecyclerView() {
        mCityListAdapter = new CityListAdapter(city -> mFragmentInteractionListener.showForecastFragment(city));
        mRecyclerView.setAdapter(mCityListAdapter);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
    }

    private void initCitiesObserver() {
        mViewMode.getCities().observe(getViewLifecycleOwner(), cities -> {
            mTxtNoContent.setVisibility((cities == null || cities.size() == 0) ? View.VISIBLE : View.GONE);
            mCityListAdapter.setCitiesList(cities);
        });
    }

    private void getAllCities() {
        mViewMode.loadAllCities().observe(getViewLifecycleOwner(), cities -> mViewMode.setCitiesList(cities));
    }

    private void searchForCity(String searchParam) {
        mViewMode.searchForCity(searchParam).observe(getViewLifecycleOwner(), cities -> mViewMode.setCitiesList(cities));
    }

    public interface CityListFragmentInteractionListener {
        void showForecastFragment(City city);
    }
}
