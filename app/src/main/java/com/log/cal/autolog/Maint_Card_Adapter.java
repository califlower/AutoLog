package com.log.cal.autolog;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Maint_Card_Adapter extends RecyclerView.Adapter<Maint_Card_Adapter.Maint_Card_ViewHolder>
{
    private final List<Maint_Object> maintenance;
    private final int odometer;
    private final int bike_location;
    private final boolean maint_type;

    public Maint_Card_Adapter(List<Maint_Object> maintenance, int odometer, int bike_location,boolean maint_type)
    {
        this.maintenance=maintenance;
        this.odometer=odometer;
        this.bike_location=bike_location;
        this.maint_type=maint_type;
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

        if (b.history.size()!=0)
        {
            int t=b.interval+b.history.get(0).miles-odometer;

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
        holder.llpast.removeAllViewsInLayout();

        for (int i=0; i<b.history.size();i++)
        {
            TextView t=new TextView(holder.llpast.getContext());
            LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            p.setMargins(10,10,10,10);
            t.setLayoutParams(p);


            try
            {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

                Date d=b.history.get(i).date;
                String s=sdf.format(d);
                t.setText("Done at "+ b.history.get(i).miles+ " on " + s);

                t.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));

                holder.llpast.addView(t);
            }
            catch (Exception e)
            {
                t.setText("Done at "+ b.history.get(i).miles+" on -- -- --");
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
                public void onClick(final View v)
                {
                   new MaterialDialog.Builder(v.getContext())
                            .title("Add a Mileage and Date")
                            .customView(R.layout.date_time_view,true)
                            .positiveText("Add History")
                            .negativeText("Cancel")
                           .alwaysCallInputCallback()
                           .onPositive(new MaterialDialog.SingleButtonCallback()
                           {
                               @Override
                               public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
                               {


                                   EditText in=(EditText) dialog.getCustomView().findViewById(R.id.history_mile);
                                   DatePicker date=(DatePicker) dialog.getCustomView().findViewById(R.id.history_date);

                                   if (in.getText().toString().trim().length()==0)
                                   {

                                       Snackbar.make(v, "Missing Input Detected", Snackbar.LENGTH_LONG).show();
                                   }
                                   else if (Integer.parseInt(in.getText().toString())>odometer)
                                   {
                                       Snackbar.make(v, "Mileage Entered Greater Than Odometer", Snackbar.LENGTH_LONG).show();

                                   }
                                   else
                                   {
                                       Date to_add = new Date(date.getCalendarView().getDate());
                                       int to_add_int = Integer.parseInt(in.getText().toString());
                                       Maint_History_Obj x = new Maint_History_Obj(to_add, to_add_int);

                                       maintenance.get(getAdapterPosition()).history.add(x);

                                       Collections.sort(maintenance.get(getAdapterPosition()).history, Collections.<Maint_History_Obj>reverseOrder());

                                       Context context = dialog.getContext();
                                       SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.vehicle_array_preferences), Context.MODE_PRIVATE);
                                       String vehicle_gson_array = sharedPref.getString(context.getString(R.string.vehicle_key), "empty_key");

                                       /******
                                        * Checks if the saved array is empty or not
                                        */

                                       if (vehicle_gson_array.compareTo("empty_key") != 0) {
                                           Gson gson = new Gson();
                                           Type collectionType = new TypeToken<ArrayList<Vehicle_Object>>() {
                                           }.getType();
                                           ArrayList<Vehicle_Object> temp_list = gson.fromJson(vehicle_gson_array, collectionType);

                                           /*******
                                            * Removes item from recyclerview and sharedpreferences
                                            */
                                           temp_list.get(bike_location).maint_list = maintenance;
                                           Maint_Card_Adapter.this.notifyDataSetChanged();


                                           SharedPreferences.Editor editor = sharedPref.edit();
                                           String insert_preference = gson.toJson(temp_list);


                                           editor.putString(context.getString(R.string.vehicle_key), insert_preference);

                                           editor.apply();


                                           /****
                                            * applies changes
                                            */
                                       }
                                   }


                               }
                           })
                            .show();
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
