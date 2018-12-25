package com.mohamedrabie.www.weather;

import com.mohamedrabie.www.weather.models.model.forecast_model.ForecastResponse;
import com.mohamedrabie.www.weather.models.model.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by mohamed on 12/3/2018.
 */

public interface myWeatherService {
    @GET("group?id=360630,2988507,2643743&units=metric&appid=32dab858a5e96acc3323cf4efc01021a")
    Call<WeatherResponse> getMWeather();


    //Call<List<List>> getMWeather(@Query("id")String ids, @Query("appid")String key);
//forecast.json?key=91d3a1cbfb65473e9cc132128182611&q=paris&days=6
    @GET("forecast.json")
    Call<ForecastResponse> getForecast(@Query("key")String apiKey, @Query("q")String cityName, @Query("days")String dayNumber );


    @GET("forecast.json?key=91d3a1cbfb65473e9cc132128182611&q=Cairo&days=6")
    Call<ForecastResponse> getforecast();
}
