package com.mohamedrabie.www.weather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohamedrabie.www.weather.R;
import com.mohamedrabie.www.weather.models.model.forecast_model.Forecastday;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mohamed on 11/26/2018.
 */

public class forecastAdapter extends BaseAdapter {
    private List<Forecastday> ForecastList;
    private Context context;
public forecastAdapter(Context c,List<Forecastday> list){
    this.context = c;
    this.ForecastList = list;
}

    private String formatDate(Long dateObject) {

// convert seconds to milliseconds
        Date date = new java.util.Date(dateObject*1000L);
// the format of your date
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE");
        String formattedDate = sdf.format(date);

        return formattedDate;
    }

    @Override
    public int getCount() {
        return ForecastList.size();
    }

    @Override
    public Object getItem(int position) {
        return ForecastList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if(view==null)
        {
            view= LayoutInflater.from(context).inflate(R.layout.forecast_list_item,parent,false);
        }
        TextView date=(TextView)view.findViewById(R.id.date);
        TextView max=(TextView)view.findViewById(R.id.max);
        TextView min=(TextView)view.findViewById(R.id.min);
        ImageView Flag=(ImageView) view.findViewById(R.id.ico);

        final Forecastday forelist=ForecastList.get(position);

        date.setText(formatDate((long) forelist.getDateEpoch()));
        max.setText((int)forelist.getDay().getMaxtempC()+"°");
        min.setText((int)forelist.getDay().getMintempC()+"°");
        Picasso.get().load("http:"+forelist.getDay().getCondition().getIcon()).into(Flag);


        return view;
    }
}
