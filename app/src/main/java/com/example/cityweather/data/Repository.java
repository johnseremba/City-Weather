package com.example.cityweather.data;

public class Repository {
    public static final Object LOCK = new Object();
    private static Repository instance;

    public Repository() {
    }

    public static Repository getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new Repository();
            }
        }
        return instance;
    }
}
