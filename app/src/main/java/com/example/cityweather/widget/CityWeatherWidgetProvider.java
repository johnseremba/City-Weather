package com.example.cityweather.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cityweather.R;
import com.example.cityweather.data.Repository;
import com.example.cityweather.data.api.RequestCallback;
import com.example.cityweather.data.api.ResponseCode;
import com.example.cityweather.data.local.model.Forecast;
import com.example.cityweather.utils.DateUtils;
import com.example.cityweather.utils.InjectorUtils;
import com.squareup.picasso.Picasso;

import static com.example.cityweather.utils.InjectorUtils.KEY_SELECTED_CITY_ID;
import static com.example.cityweather.utils.InjectorUtils.KEY_SELECTED_CITY_NAME;

public class CityWeatherWidgetProvider extends AppWidgetProvider {
    public static final String EXTRA_WIDGET_FORECAST = "com.example.cityweather.widget.EXTRA_WIDGET_FORECAST";
    private Repository mRepository;
    private int mCityId;
    private String mCityName;
    private Forecast mForecast;

    public static void sendRefreshBroadcast(Context context, Forecast forecast) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(EXTRA_WIDGET_FORECAST, forecast);
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            mForecast = intent.getParcelableExtra(EXTRA_WIDGET_FORECAST);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            // Widget update broadcast
            ComponentName componentName = new ComponentName(context, CityWeatherWidgetProvider.class);
            onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(componentName));
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        mRepository = InjectorUtils.provideRepository(context);
        SharedPreferences prefs = InjectorUtils.provideSharedPreferences(context.getApplicationContext());
        mCityName = prefs.getString(KEY_SELECTED_CITY_NAME, "");
        mCityId = prefs.getInt(KEY_SELECTED_CITY_ID, -1);

        for (int appWidgetId : appWidgetIds) {
            // Instantiate a RemoteViews object for the app widget layout
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            if (mForecast == null) {
                // if no forecast data is received via broadcast, load data from the db
                mRepository.getLocalDataSource().getForecastByCityIdAsynchronous(mCityId, new RequestCallback<Forecast>() {
                    @Override
                    public void onSuccess(@Nullable Forecast forecast) {
                        updateRemoteViews(context, forecast, remoteViews, appWidgetId, appWidgetIds, appWidgetManager);
                    }

                    @Override
                    public void onError(@NonNull ResponseCode responseCode, @Nullable String errorMessage) {
                        // If no data in db, then show appropriate msg to user
                        if (responseCode == ResponseCode.NO_DATA) {
                            remoteViews.setTextViewText(R.id.txt_city_name, context.getString(R.string.msg_no_city_weather));
                            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
                        }
                    }
                });
            } else {
                // if forecast data has been received via broadcast, just update the widget
                updateRemoteViews(context, mForecast, remoteViews, appWidgetId, appWidgetIds, appWidgetManager);
            }
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void updateRemoteViews(Context context,
                                   Forecast forecast,
                                   RemoteViews remoteViews,
                                   int appWidgetId,
                                   int[] appWidgetIds,
                                   AppWidgetManager appWidgetManager) {
        Picasso.get()
                .load(forecast.getIconUrl())
                .into(remoteViews, R.id.weather_icon, appWidgetIds);
        remoteViews.setTextViewText(R.id.txt_city_name, mCityName);
        remoteViews.setTextViewText(R.id.txt_date, DateUtils.getNormalDate(forecast.getDatetime()));
        remoteViews.setTextViewText(R.id.txt_temperature,
                String.format(context.getString(R.string.temperature_string), (int) forecast.getTemperature()));
        remoteViews.setTextViewText(R.id.txt_more_info, context.getString(R.string.txt_more_info));
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }
}
