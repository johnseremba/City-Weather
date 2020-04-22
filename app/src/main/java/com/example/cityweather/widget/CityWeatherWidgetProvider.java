package com.example.cityweather.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.RemoteViews;

import com.example.cityweather.R;
import com.example.cityweather.data.local.model.City;
import com.example.cityweather.data.local.model.Forecast;
import com.example.cityweather.ui.MainActivity;
import com.example.cityweather.utils.DateUtils;
import com.squareup.picasso.Picasso;

public class CityWeatherWidgetProvider extends AppWidgetProvider {
    public static final String EXTRA_WIDGET_CITY = "com.example.cityweather.widget.EXTRA_WIDGET_CITY";
    public static final String EXTRA_WIDGET_FORECAST = "com.example.cityweather.widget.EXTRA_WIDGET_FORECAST";
    private Forecast mForecast;
    private City mCity;

    public static void sendRefreshBroadcast(Context context, City city, Forecast forecast) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(EXTRA_WIDGET_CITY, city);
        intent.putExtra(EXTRA_WIDGET_FORECAST, forecast);
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            mCity = intent.getParcelableExtra(EXTRA_WIDGET_CITY);
            mForecast = intent.getParcelableExtra(EXTRA_WIDGET_FORECAST);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, CityWeatherWidgetProvider.class);
            onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(componentName));
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            // Instantiate a RemoteViews object for the app widget layout
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);

            // get weather forecast for the last city selected by user
            if (mForecast != null && mCity != null) {
                remoteViews.setTextViewText(R.id.txt_city_name, mCity.getName());
                remoteViews.setTextViewText(R.id.txt_date, DateUtils.getNormalDate(mForecast.getDatetime()));

                Resources res = context.getResources();
                remoteViews.setTextViewText(R.id.txt_temperature,
                        String.format(res.getString(R.string.temperature_string), (int) mForecast.getTemperature()));

                Picasso.get()
                        .load(mForecast.getIconUrl())
                        .into(remoteViews, R.id.weather_icon, appWidgetIds);

                Intent activityIntent = new Intent(context, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);
                remoteViews.setOnClickPendingIntent(R.id.weather_icon, pendingIntent);
            }
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
