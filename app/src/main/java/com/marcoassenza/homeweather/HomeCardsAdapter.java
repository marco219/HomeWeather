package com.marcoassenza.homeweather;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by marcoassenza on 12/10/2017.
 */

public class HomeCardsAdapter extends RecyclerView.Adapter<HomeCardsAdapter.CardViewHolder>{

    private static List<HomeCard> homeCards;
    private static ClickListener clickListener;

    static String[] stations = {"Chambre Marco","Salon","Extérieur","Bassin"};
    PrettyTime p = new PrettyTime(new Locale("french"));

    public HomeCardsAdapter(List<HomeCard> homeCards) {
        this.homeCards = homeCards;
    }

    public static String getStationId(int position){
        return stations[homeCards.get(position).stationId];
    }


    public static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView stationId;
        TextView lastTemperature;
        TextView lastTemperatureDate;

        CardViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            stationId = (TextView)itemView.findViewById(R.id.room_name);
            lastTemperature = (TextView)itemView.findViewById(R.id.last_temperature);
            lastTemperatureDate = (TextView)itemView.findViewById(R.id.last_temperature_date);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(),view);
        }
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card_view, parent, false);
        CardViewHolder cardViewHolder = new CardViewHolder(v);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {

        Date date = homeCards.get(position).lastTemperatureDate;
        Date now = new Date();
        boolean hostIsDown = false;
        if(((now.getTime()-date.getTime())/60000) > 31) hostIsDown = true;

        if(!hostIsDown){
            holder.stationId.setText(stations[homeCards.get(position).stationId]);
            holder.lastTemperatureDate.setText(p.format(date));
            holder.lastTemperature.setText(homeCards.get(position).lastTemperature + " °C");
        }
        else{
            holder.stationId.setTextColor(Color.RED);
            holder.stationId.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            holder.stationId.setText("La sonde est hors-ligne !");
            holder.lastTemperatureDate.setText(p.format(date));
        }
    }

    @Override
    public int getItemCount() {
        return homeCards.size();
    }

    public interface ClickListener {
        public void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

}