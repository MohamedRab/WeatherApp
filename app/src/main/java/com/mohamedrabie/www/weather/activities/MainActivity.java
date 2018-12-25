package com.mohamedrabie.www.weather.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mohamedrabie.www.weather.R;
import com.mohamedrabie.www.weather.RetrofitClientInstance;
import com.mohamedrabie.www.weather.RoomDb.DBClient;
import com.mohamedrabie.www.weather.RoomDb.WeatherDB;
import com.mohamedrabie.www.weather.RoomDb.WeatherTable;
import com.mohamedrabie.www.weather.adapters.WeatherBaseAdapter;
import com.mohamedrabie.www.weather.adapters.WeatherOfflineAdapter;
import com.mohamedrabie.www.weather.models.model.model.WeatherResponse;
import com.mohamedrabie.www.weather.myWeatherService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity{
   /* private static final String USGS_REQUEST_URL =
            "http://api.openweathermap.org/data/2.5/group?id=360630,2988507,2643743&units=metric&appid=32dab858a5e96acc3323cf4efc01021a";*/
     private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    WeatherBaseAdapter weatherBaseAdapter;
    ListView weatherListView;
    private  List<com.mohamedrabie.www.weather.models.model.model.List> weatherList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            updateData();
            getDataToList();
        } else {
            Toast.makeText(getApplicationContext(),"No Internet Connection \n check the Internet",Toast.LENGTH_LONG)
                    .show();
            getTasks();

        }

        final SwipeRefreshLayout mySwipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               updateData();
               getDataToList();
                //mySwipeRefreshLayout.setRefreshing(true);
                mySwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

     private void populateListView(List<com.mohamedrabie.www.weather.models.model.model.List> list) {
         weatherListView = findViewById(R.id.list);
         weatherBaseAdapter = new WeatherBaseAdapter(this,list);
         weatherListView.setAdapter(weatherBaseAdapter);
         weatherListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 Intent i=new Intent(MainActivity.this,forecastDays_activity.class);

                 if(position==0){
                     i.putExtra("city_name_Key","Cairo");
                     i.putExtra("position",0);
                     startActivity(i);
                 }
                 else if(position==1){
                     i.putExtra("city_name_Key","Paris");
                     i.putExtra("position",1);
                     startActivity(i);
                 }
                 else{
                     i.putExtra("city_name_Key","London");
                     i.putExtra("position",2);
                     startActivity(i);
                 }

             }
         });
     }
    private void getDataToList(){

        myWeatherService myAPIService = RetrofitClientInstance.getRetrofitInstance(BASE_URL).create(myWeatherService.class);

        Call<WeatherResponse> call =
                myAPIService.getMWeather();
        //myAPIService.getMWeather("360630,2988507,2643743","32dab858a5e96acc3323cf4efc01021a");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                weatherList=response.body().getList();
                populateListView(weatherList);

               // Log.v("size",weatherList.get(1).getName());
            }
            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_LONG).show();
                Log.v("01",t.getMessage()+"");
            }
        });

    }

public void updateData(){
     class UpdateData extends AsyncTask<Void, Void, Void> {
         @Override
         protected Void doInBackground(Void... voids) {
             //creating a task
             WeatherTable task = new WeatherTable();
             //Delete  all rows
             WeatherDB DB = DBClient.getInstance(MainActivity.this).getAppDatabase();
              DB.weatherDAO().deleteAll();
             return null;
         }
         @Override
         protected void onPostExecute(Void aVoid) {
             super.onPostExecute(aVoid);

         }
     }
     UpdateData ud=new UpdateData();
     ud.execute();
    }
     private void getTasks() {
         class GetTasks extends AsyncTask<Void, Void, List<WeatherTable>> {

             @Override
             protected List<WeatherTable> doInBackground(Void... voids) {
                 List<WeatherTable> taskList = DBClient
                         .getInstance(getApplicationContext())
                         .getAppDatabase()
                         .weatherDAO()
                         .getAll();
                 return taskList;
             }

             @Override
             protected void onPostExecute(List<WeatherTable> tasks) {
                 super.onPostExecute(tasks);
                 if (tasks.size()==0){
                     Toast.makeText(getApplicationContext(),"No Internet Connection \n check the Internet",Toast.LENGTH_LONG)
                             .show();
                 }

                 WeatherOfflineAdapter offlineAdapter = new WeatherOfflineAdapter(MainActivity.this, tasks);
                 weatherListView = findViewById(R.id.list);
                 weatherListView.setAdapter(offlineAdapter);
             }
         }

         GetTasks gt = new GetTasks();
         gt.execute();
     }


/* private void doYourUpdate() {
         // TODO implement a refresh
         //setRefreshing(false); // Disables the refresh icon
     }
     public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()) {

             case R.id.menu_refresh:
                 // signal SwipeRefreshLayout to start the progress indicator
                 mySwipeRefreshLayout.setRefreshing(true);
                 doYourUpdate();

                 return true;
         }
         return super.onOptionsItemSelected(item);
     }*/

 }


