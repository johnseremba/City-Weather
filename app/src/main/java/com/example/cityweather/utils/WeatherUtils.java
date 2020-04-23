package com.example.cityweather.utils;

import com.example.cityweather.R;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Random;

public class WeatherUtils {
    public static int getTemperatureInCelsius(Double tempInKelvin) {
        return (int) Math.round(tempInKelvin - 273.15);
    }

    public static int getRandomColor() {
        int[] colors = {
                R.color.primary,
                R.color.accent,
                R.color.green,
                R.color.cyan,
                R.color.light_green
        };
        Random rand = new Random();
        int number = rand.nextInt(5);
        return colors[number];
    }

    public static boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress("8.8.8.8", 53);
            socket.connect(socketAddress, timeoutMs);
            socket.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
