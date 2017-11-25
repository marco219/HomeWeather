package com.marcoassenza.homeweather;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by marcoassenza on 12/10/2017.
 */

public class DetailsCardsAdapter extends RecyclerView.Adapter<DetailsCardsAdapter.CardViewHolder>{

    private static List<DetailsCard> cards;
    //private static ClickListener clickListener;

    public DetailsCardsAdapter(List<DetailsCard> cards) {
        this.cards = cards;
    }


    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView temperature;
        TextView mesureDate;

        CardViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_detail_view);
            temperature = (TextView)itemView.findViewById(R.id.temperature);
            mesureDate = (TextView)itemView.findViewById(R.id.mesure_date);
            //itemView.setOnClickListener(this);
        }


        /*@Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(),view);
        }*/
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_card_view, parent, false);
        CardViewHolder cardViewHolder = new CardViewHolder(v);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        holder.temperature.setText(cards.get(position).temperature + " °C");
        holder.mesureDate.setText(cards.get(position).mesureDate);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    /*public interface ClickListener {
        public void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }
*/
}