package com.example.cityweather.data.local.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import java.util.List;

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertObjects(List<T> objects);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(T... objects);

    @Update
    void updateObjects(List<T> objects);

    @Update
    void update(T... objects);

    @Delete
    void deleteObjects(List<T> objects);

    @Delete
    void delete(T... objects);
}
