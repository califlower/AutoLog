package com.log.cal.autolog;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Maint_Card_Adapter extends RecyclerView.Adapter<Maint_Card_Adapter.Maint_Card_ViewHolder>
{
    private final List<Maint_Object> maintenance;

    public Maint_Card_Adapter(List<Maint_Object> maintenance)
    {
        this.maintenance=maintenance;
    }

    @Override
    public Maint_Card_Adapter.Maint_Card_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.maint_card,parent,false);
        return new Maint_Card_Adapter.Maint_Card_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Maint_Card_ViewHolder holder, int position)
    {

        Maint_Object b=maintenance.get(position);

        holder.maint_name.setText(b.name);
        holder.maint_upcoming.setText(b.interval);
        holder.maint_cost.setText(b.cost.toString());


    }

    @Override
    public int getItemCount()
    {
        return maintenance.size();
    }

    public class Maint_Card_ViewHolder extends RecyclerView.ViewHolder
    {
        final TextView maint_name;
        final TextView maint_upcoming;
        final TextView maint_cost;

        public Maint_Card_ViewHolder(View v)
        {
            super(v);

            maint_name=(TextView) v.findViewById(R.id.maint_name);
            maint_upcoming=(TextView) v.findViewById(R.id.maint_upcoming);
            maint_cost=(TextView) v.findViewById(R.id.maint_interval_cost);

        }
    }


}
