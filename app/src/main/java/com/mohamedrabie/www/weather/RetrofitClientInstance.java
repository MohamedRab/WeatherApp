package com.mohamedrabie.www.weather;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mohamed on 12/3/2018.
 */

public  class RetrofitClientInstance {

    private static Retrofit retrofit;
    //private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    public static Retrofit getRetrofitInstance(String BASE_URL) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
