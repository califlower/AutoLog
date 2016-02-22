package com.log.cal.autolog;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Maint_Card_Adapter extends RecyclerView.Adapter<Maint_Card_Adapter.Maint_Card_ViewHolder>
{
    private final List<Maint_Object> maintenance;
    private final int odometer;
    private final int bike_location;

    public Maint_Card_Adapter(List<Maint_Object> maintenance, int odometer, int bike_location)
    {
        this.maintenance=maintenance;
        this.odometer=odometer;
        this.bike_location=bike_location;
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

        if (b.miles_done.size()!=0)
        {
            int t=b.interval+b.miles_done.get(0)-odometer;

            if(t<0)
            {
                holder.maint_upcoming.setText(String.valueOf(Math.abs(t))+" Miles Late");
            }
            else
            {
                holder.maint_upcoming.setText(String.valueOf(t)+" Miles Left");
            }

        }


        //last_done+interval - current miles

        holder.maint_cost.setText("$"+b.cost.toString()+" Estimated Cost");

        for (int i=0; i<b.miles_done.size();i++)
        {
            TextView t=new TextView(holder.llpast.getContext());

            try
            {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy)");

                Date d=b.dates_done.get(i);
                String s=sdf.format(d);
                t.setText("Done at"+ b.miles_done.get(i).toString()+ " on " + b);

                t.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                holder.llpast.addView(t);
            }
            catch (Exception e)
            {
                t.setText("Done at "+ b.miles_done.get(i).toString()+" on -- -- --");
                t.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                holder.llpast.addView(t);
            }

        }
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
        final ImageButton close;
        final LinearLayout llpast;
        final ImageButton add;


        public Maint_Card_ViewHolder(View v)
        {
            super(v);

            maint_name=(TextView) v.findViewById(R.id.maint_name);
            maint_upcoming=(TextView) v.findViewById(R.id.maint_upcoming);
            maint_cost=(TextView) v.findViewById(R.id.maint_interval_cost);
            close=(ImageButton)v.findViewById(R.id.close);
            llpast=(LinearLayout)v.findViewById(R.id.llpast);
            add=(ImageButton) v.findViewById(R.id.add_date);


            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(v.getContext())
                            .title("Update Mileage")
                            .inputType(InputType.TYPE_DATETIME_VARIATION_DATE)
                            .inputRange(1,-1)
                            .negativeText("Cancel").show();


                }
            });


            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /****************
                     * Handles the issue of not removing saved objects
                     */
                    Context context=v.getContext();
                    SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.vehicle_array_preferences), Context.MODE_PRIVATE);
                    String vehicle_gson_array=sharedPref.getString(context.getString(R.string.vehicle_key),"empty_key");

                    /******
                     * Checks if the saved array is empty or not
                     */

                    if (vehicle_gson_array.compareTo("empty_key")!=0)
                    {
                        Gson gson=new Gson();
                        Type collectionType = new TypeToken<ArrayList<Vehicle_Object>>(){}.getType();
                        ArrayList<Vehicle_Object> temp_vehicle_list=gson.fromJson(vehicle_gson_array,collectionType);
                        Vehicle_Object obj=temp_vehicle_list.get(bike_location);
                        List<Maint_Object> temp_list=obj.maint_list;


                        /*******
                         * Removes item from recyclerview and sharedpreferences
                         */
                        temp_list.remove(Maint_Card_Adapter.Maint_Card_ViewHolder.this.getAdapterPosition());
                        obj.maint_list=temp_list;
                        temp_vehicle_list.set(bike_location,obj);

                        Maint_Card_Adapter.this.maintenance.remove(Maint_Card_Adapter.Maint_Card_ViewHolder.this.getAdapterPosition());
                        Maint_Card_Adapter.this.notifyItemRemoved(Maint_Card_Adapter.Maint_Card_ViewHolder.this.getAdapterPosition());

                        SharedPreferences.Editor editor = sharedPref.edit();
                        String insert_preference=gson.toJson(temp_vehicle_list);

                        editor.putString(context.getString(R.string.vehicle_key),insert_preference);

                        editor.apply();


                        /****
                         * applies changes
                         */
                    }
                }
            });
        }
    }


}
