package com.mohamedrabie.www.weather.RoomDb;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by mohamed on 12/17/2018.
 */

public class DBClient  {
    private Context mCtx;
    private static DBClient mInstance;
    private WeatherDB weatherDB;

    private DBClient(Context mCtx) {
        this.mCtx = mCtx;

        //creating the app database with Room database builder
        //Weather is the name of the database
        weatherDB = Room.databaseBuilder(mCtx, WeatherDB.class, "Weather").build();
    }

    public static synchronized DBClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DBClient(mCtx);
        }
        return mInstance;
    }
    public WeatherDB getAppDatabase() {
        return weatherDB;
    }


}
