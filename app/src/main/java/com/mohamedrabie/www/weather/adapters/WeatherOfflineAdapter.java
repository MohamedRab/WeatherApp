package com.mohamedrabie.www.weather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mohamedrabie.www.weather.R;
import com.mohamedrabie.www.weather.RoomDb.WeatherTable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by mohamed on 12/20/2018.
 */

public class WeatherOfflineAdapter extends BaseAdapter {
    private Context c;
    private java.util.List<WeatherTable> offlineList;

    public WeatherOfflineAdapter(Context c,java.util.List<WeatherTable> offlineList){
        this.c=c;
        this.offlineList=offlineList;

    }
    @Override
    public int getCount() {
        return offlineList.size();
    }

    @Override
    public Object getItem(int position) {
        return offlineList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
    public View getView(int position, View view, ViewGroup parent) {
        if(view==null)
        {
            view= LayoutInflater.from(c).inflate(R.layout.list_item,parent,false);
        }
        TextView name=(TextView)view.findViewById(R.id.city_name);
        TextView date=(TextView)view.findViewById(R.id.date_text);
        TextView temp=(TextView)view.findViewById(R.id.temp_text);
        TextView desc=(TextView)view.findViewById(R.id.Desc_text);
        ImageView Flag=(ImageView) view.findViewById(R.id.icon);
        LinearLayout itemBackground=(LinearLayout)view.findViewById(R.id.background);

        WeatherTable offList=offlineList.get(position);
        name.setText(offList.getName());
        if(position==0){
            itemBackground.setBackgroundResource(backgroundColor("GMT+2"));

        }
        else if(position==1){
            itemBackground.setBackgroundResource(backgroundColor("GMT+1"));

        }
        else if(position==2){
            itemBackground.setBackgroundResource(backgroundColor("GMT"));
        }
        date.setText(formatDate((long) offList.getDate()));
        temp.setText(String.valueOf((int) offList.getTemp()));
        desc.setText(offList.getDesc());
       // Picasso.get().load("http://openweathermap.org/img/w/"+WeatherList.getWeather().get(0).getIcon()+".png").into(Flag);

        return view;
    }
}
