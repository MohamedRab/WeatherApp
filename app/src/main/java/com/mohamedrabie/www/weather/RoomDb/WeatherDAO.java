package com.mohamedrabie.www.weather.RoomDb;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by mohamed on 12/17/2018.
 */
@Dao
public interface WeatherDAO {
    @Query("SELECT * FROM weathertable")
    List<WeatherTable> getAll();

    @Insert
    void insert(WeatherTable weatherTable);

    @Delete
    void delete(WeatherTable task);

    @Update
    void update(WeatherTable task);

    @Query("SELECT COUNT (*) FROM weathertable")
    int getNumberOfRows();

    @Query("Delete  FROM weathertable")
    void deleteAll();
}
