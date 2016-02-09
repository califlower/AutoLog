package com.log.cal.autolog;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cal13 on 2/8/2016.
 */
public class Bike_Card_Adapter extends RecyclerView.Adapter<Bike_Card_Adapter.Bike_Card_ViewHolder>
{
    private List<Bike_Object> bikes;

    public Bike_Card_Adapter(List<Bike_Object> bikes)
    {
        this.bikes=bikes;
    }

    @Override
    public Bike_Card_Adapter.Bike_Card_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.bike_card,parent,false);
        return new Bike_Card_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Bike_Card_ViewHolder holder, int position)
    {
        Bike_Object b=bikes.get(position);

        holder.bike_make.setText(b.bike_make);
        holder.bike_model.setText(b.bike_model);
        holder.bike_year.setText(b.bike_year);
    }

    @Override
    public int getItemCount()
    {
        return bikes.size();
    }

    public class Bike_Card_ViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView bike_make;
        protected TextView bike_model;
        protected TextView bike_year;

        public Bike_Card_ViewHolder(View v)
        {
            super(v);

            bike_make=(TextView) v.findViewById(R.id.card_make);
            bike_model=(TextView) v.findViewById(R.id.card_model);
            bike_year=(TextView) v.findViewById(R.id.card_year);
        }
    }
}
