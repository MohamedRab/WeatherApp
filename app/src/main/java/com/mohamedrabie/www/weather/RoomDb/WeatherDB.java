package com.mohamedrabie.www.weather.RoomDb;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by mohamed on 12/17/2018.
 */
@Database(entities = {WeatherTable.class}, version = 1)
public abstract class WeatherDB extends RoomDatabase {
    public abstract WeatherDAO weatherDAO();
}
