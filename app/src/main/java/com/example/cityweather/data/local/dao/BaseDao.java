package com.example.cityweather.data.local.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import java.util.List;

public interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<T> objects);

    @Update
    void update(List<T> objects);

    @Delete
    void delete(List<T> objects);
}
