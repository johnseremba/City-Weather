package com.example.cityweather;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import com.example.cityweather.data.local.model.City;
import com.example.cityweather.ui.MainActivity;
import com.example.cityweather.ui.city.CityFragment;
import com.example.cityweather.utils.InjectorUtils;

import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(JUnit4.class)
@LargeTest
public class CityWeatherTest {
    private IdlingResource idlingResource;
    ActivityScenario<MainActivity> activityScenario;

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.ACCESS_COARSE_LOCATION");

    @Before
    public void setUp() {
        idlingResource = InjectorUtils.provideTestingIdlingResource();
        activityScenario = ActivityScenario.launch(MainActivity.class);
        activityScenario.onActivity(activity -> IdlingRegistry.getInstance().register(idlingResource));
    }

    @After
    public void tearDown() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void testMapFragmentDisplayingCorrectly() {
        onView(withId(R.id.autocomplete_fragment))
                .check(matches(isDisplayed()));

        onView(withId(R.id.map))
                .check(matches(isDisplayed()));

        onView(withId(R.id.fab_focus_map))
                .check(matches(isDisplayed()));

        onView(withId(R.id.fab_cities_list))
                .check(matches(isDisplayed()));

        onView(withId(R.id.coordinator_view))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testCityDetailFragment() {
        City city = getFakeCity();
        activityScenario.onActivity(activity -> {
            CityFragment cityFragment = CityFragment.newInstance(city);
            activity.addFragment(cityFragment, CityFragment.TAG, true);
        });

        // Check that the forecast button is displayed and has text
        onView(withId(R.id.btn_show_forecast))
                .check(matches(isDisplayed()))
                .check(matches(not(withText(""))));

        // Check that the description text view is displayed
        onView(withId(R.id.txt_city_desc))
                .check(matches(isDisplayed()));

        // Check that saving FAB is displayed
        onView(withId(R.id.fab_add))
                .check(matches(isDisplayed()));
    }

    @NotNull
    private City getFakeCity() {
        return new City(
                1,
                new Date(),
                "Fake City",
                "Fake Address",
                1.01,
                1.02,
                "Fake Description");
    }
}
