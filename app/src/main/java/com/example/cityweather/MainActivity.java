package com.example.cityweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvhe = (TextView) findViewById(R.id.text_hello_word);
        String weatherAPIKey = BuildConfig.OPEN_WEATHER_API_KEY;
        tvhe.setText(weatherAPIKey);
    }
}
