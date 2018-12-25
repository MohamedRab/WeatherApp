package com.mohamedrabie.www.weather.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mohamedrabie.www.weather.R;
import com.mohamedrabie.www.weather.RoomDb.DBClient;
import com.mohamedrabie.www.weather.RoomDb.WeatherDB;
import com.mohamedrabie.www.weather.RoomDb.WeatherTable;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by mohamed on 12/3/2018.
 */

public class WeatherBaseAdapter extends BaseAdapter {
    private List<com.mohamedrabie.www.weather.models.model.model.List> weatherList;
    private Context context;


    public WeatherBaseAdapter(Context context,List<com.mohamedrabie.www.weather.models.model.model.List> list){
        this.context = context;
        this.weatherList = list;
    }




    @Override
    public int getCount() {
        return weatherList.size();
    }

    @Override
    public Object getItem(int pos) {
        return weatherList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    private String formatDate(Long dateObject) {

        Date date = new java.util.Date(dateObject*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("LLL dd, yyyy");
        //SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);

        return formattedDate;
    }
    private int backgroundColor(String timeZone){
        //TimeZone tz = TimeZone.getTimeZone("GMT+05:30");
        int Background = 0;

        TimeZone tz = TimeZone.getTimeZone(timeZone);
        Calendar c = Calendar.getInstance(tz);

        /*String time = String.format("%02d" , c.get(Calendar.HOUR_OF_DAY))+":"+
                String.format("%02d" , c.get(Calendar.MINUTE))+":"+
                   String.format("%02d" , c.get(Calendar.SECOND))+":"+
             String.format("%03d" , c.get(Calendar.MILLISECOND));*/

        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            Background= R.drawable.morning_background;

        }else if(timeOfDay >= 12 && timeOfDay < 16){
            Background=R.drawable.afternoon_background;

        }else if(timeOfDay >= 16 && timeOfDay < 21){
            Background=R.drawable.evening_background;

        }else if(timeOfDay >= 21 && timeOfDay < 24){
            Background=R.drawable.night_background;

        }

        return Background;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            view= LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);
        }
        TextView name=(TextView)view.findViewById(R.id.city_name);
        TextView date=(TextView)view.findViewById(R.id.date_text);
        TextView temp=(TextView)view.findViewById(R.id.temp_text);
       TextView desc=(TextView)view.findViewById(R.id.Desc_text);
      ImageView Flag=(ImageView) view.findViewById(R.id.icon);
        LinearLayout itemBackground=(LinearLayout)view.findViewById(R.id.background);


        final com.mohamedrabie.www.weather.models.model.model.List WeatherList= weatherList.get(position);

        name.setText(WeatherList.getName());
      if(position==0){
            itemBackground.setBackgroundResource(backgroundColor("GMT+2"));

        }
        else if(position==1){
            itemBackground.setBackgroundResource(backgroundColor("GMT+1"));

        }
        else if(position==2){
            itemBackground.setBackgroundResource(backgroundColor("GMT"));
        }
        date.setText(formatDate((long) WeatherList.getDt()));
        temp.setText(String.valueOf((int) WeatherList.getMain().getTemp()));
        desc.setText(WeatherList.getWeather().get(0).getDescription());
        Picasso.get().load("http://openweathermap.org/img/w/"+WeatherList.getWeather().get(0).getIcon()+".png").into(Flag);


        class SaveTask extends AsyncTask<Void, Void, Void> {

            int x;
            String m;
            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                WeatherTable task = new WeatherTable();

                task.setName(WeatherList.getName());
                 task.setDesc(WeatherList.getWeather().get(0).getDescription());
                 task.setDate(WeatherList.getDt());
                 task.setTemp( WeatherList.getMain().getTemp());
                 //adding to database
                 WeatherDB DB = DBClient.getInstance(context).getAppDatabase();

              // DB.weatherDAO().deleteAll();
                DB.weatherDAO().insert(task);
                  x = DB.weatherDAO().getNumberOfRows();
            //   m= DB.weatherDAO().getAll().get(4).getName();




                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //Toast.makeText(context,m,Toast.LENGTH_LONG).show();
                Toast.makeText(context,x+"",Toast.LENGTH_LONG).show();

                // Log.v("moahemd",m+"");
            }
        }

        SaveTask sb=new SaveTask();
        sb.execute();

        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, thisSpacecraft.getName(), Toast.LENGTH_SHORT).show();
            }
        });*/

        return view;
    }
}
