package com.mohamedrabie.www.weather.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mohamedrabie.www.weather.R;
import com.mohamedrabie.www.weather.adapters.forecastAdapter;
import com.mohamedrabie.www.weather.models.model.forecast_model.ForecastResponse;
import com.mohamedrabie.www.weather.models.model.forecast_model.Forecastday;
import com.mohamedrabie.www.weather.myWeatherService;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class forecastDays_activity extends AppCompatActivity
{

    private String USGS_REQUEST_URL ;
    private TextView city;
    private TextView temp;
    private TextView current_date;
    private TextView desc;
    private ImageView current_icon;
    String city_name;
    ListView forecastListView;
    forecastAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_days_activity);
        Bundle b=getIntent().getExtras();

        city_name=b.getString("city_name_Key");


        /*USGS_REQUEST_URL =
                "http://api.apixu.com/v1/forecast.json?key=91d3a1cbfb65473e9cc132128182611&q="+city_name+"&days=6";*/
    String BASE_URL =
                "http://api.apixu.com/v1/";

        city=(TextView)findViewById(R.id.cityName);
        city.setText(city_name);

        temp=(TextView)findViewById(R.id.temp_text);
        current_date=(TextView)findViewById(R.id.date_text);
        desc=(TextView)findViewById(R.id.Desc_text);
        current_icon=(ImageView)findViewById(R.id.cIcon);

        loadcityName();
        getListData();



    }

    private String formatDate(Long dateObject) {
// convert seconds to milliseconds
        Date date = new java.util.Date(dateObject*1000L);
// the format of your date
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("LLL dd, yyyy");
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public void loadcityName(){
        Retrofit retrofit=new Retrofit.Builder().baseUrl("http://api.apixu.com/v1/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        myWeatherService newJson=retrofit.create(myWeatherService.class);
        newJson.getForecast("91d3a1cbfb65473e9cc132128182611",city_name,"7").enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                temp.setText(String.valueOf((int)response.body().getCurrent().getTempC()));
                current_date.setText(formatDate((long) response.body().getCurrent().getLastUpdatedEpoch()));
                desc.setText(response.body().getCurrent().getCondition().getText());
                Picasso.get().load("http:"+response.body().getCurrent().getCondition().getIcon()).into(current_icon);
            }
            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {

            }
        });

}
    private void populateListView(java.util.List<Forecastday> list) {

        forecastListView = (ListView) findViewById(R.id.forecast_list);
        adapter = new forecastAdapter(this,list);
        forecastListView.setAdapter(adapter);

    }
    private void getListData(){

        Retrofit retrofit=new Retrofit.Builder().baseUrl("http://api.apixu.com/v1/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        myWeatherService newJson=retrofit.create(myWeatherService.class);
        newJson.getForecast("91d3a1cbfb65473e9cc132128182611",city_name,"7").enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                java.util.List<Forecastday> forecastList=response.body().getForecast().getForecastday();
                populateListView(forecastList);
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {

            }
        });


    }
}
