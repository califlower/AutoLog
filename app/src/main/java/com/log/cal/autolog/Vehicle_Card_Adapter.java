package com.log.cal.autolog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Vehicle_Card_Adapter extends RecyclerView.Adapter<Vehicle_Card_Adapter.Bike_Card_ViewHolder>
{
    private final List<Vehicle_Object> bikes;

    public Vehicle_Card_Adapter(List<Vehicle_Object> bikes)
    {
        this.bikes=bikes;
    }

    @Override
    public Vehicle_Card_Adapter.Bike_Card_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_card,parent,false);
        return new Bike_Card_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Bike_Card_ViewHolder holder, int position)
    {

        Vehicle_Object b=bikes.get(position);

        holder.bike_make.setText(b.bike_make);
        holder.bike_model.setText(b.bike_model);
        holder.bike_year.setText(String.valueOf(b.bike_year));
        holder.bike_mile.setText(String.valueOf(b.bike_mileage));
        holder.maint_type.setText(b.maint_type);
        Glide.with(holder.bike_image.getContext()).load(b.path).centerCrop().into(holder.bike_image);

        if (b.maint_type.compareToIgnoreCase("Miles")==0)
        {
            holder.update_miles.setText("Update Miles");
        }
        else
        {
            holder.update_miles.setText("Update Hours");
        }


        if (b.maint_list.size()==0)
        {
            holder.upcoming.setText("No Maintenance Items Added");
            holder.upcoming.setTextColor(Color.parseColor("#f44336"));
            holder.est_cost.setText("Not Applicable");
            holder.est_cost.setTextColor(Color.parseColor("#f44336"));
        }
        else
        {
            Collections.sort(b.maint_list);
            Maint_Object m=b.maint_list.get(0);
            holder.upcoming.setText(m.name);
            holder.est_cost.setText(m.cost.toString());
        }



    }

    @Override
    public int getItemCount()
    {
        return bikes.size();
    }

    public class Bike_Card_ViewHolder extends RecyclerView.ViewHolder
    {
        final TextView bike_make;
        final TextView bike_model;
        final TextView bike_year;
        final TextView bike_mile;
        final TextView update_miles;
        final TextView remove_bike;
        final ImageView bike_image;
        final TextView maint_type;
        final ImageButton settings;
        final ImageButton maint_list;

        final TextView upcoming;
        final TextView est_cost;


        public Bike_Card_ViewHolder(View v)
        {
            super(v);

            bike_make=(TextView) v.findViewById(R.id.card_make);
            bike_model=(TextView) v.findViewById(R.id.card_model);
            bike_year=(TextView) v.findViewById(R.id.card_year);
            bike_mile=(TextView) v.findViewById(R.id.card_mileage);
            update_miles=(TextView) v.findViewById(R.id.update_miles);
            remove_bike=(TextView) v.findViewById(R.id.remove_bike);
            bike_image=(ImageView)v.findViewById(R.id.vehicle_image);
            maint_type=(TextView) v.findViewById(R.id.maint_type_text);
            settings=(ImageButton) v.findViewById(R.id.bike_settings);
            maint_list=(ImageButton) v.findViewById(R.id.maintenance_list_button);
            upcoming=(TextView)v.findViewById(R.id.next_maint);
            est_cost=(TextView)v.findViewById(R.id.est_cost_item);


            maint_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent maintenance=new Intent(v.getContext(),MaintenanceActivity.class);
                    maintenance.putExtra("location",getAdapterPosition());
                    v.getContext().startActivity(maintenance);


                }
            });

            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent bike_settings=new Intent(v.getContext(),Per_Vehicle_Settings.class);
                    bike_settings.putExtra("location",getAdapterPosition());
                    v.getContext().startActivity(bike_settings);
                }
            });
            remove_bike.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {


                    /****************
                     * Handles the issue of not removing saved objects
                     */
                    Context context=v.getContext();
                    final SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.vehicle_array_preferences), Context.MODE_PRIVATE);
                    String vehicle_gson_array=sharedPref.getString(context.getString(R.string.vehicle_key),"empty_key");

                    /******
                     * Checks if the saved array is empty or not
                     */

                    if (vehicle_gson_array.compareTo("empty_key")!=0)
                    {
                        Gson gson=new Gson();
                        Type collectionType = new TypeToken<ArrayList<Vehicle_Object>>(){}.getType();
                        ArrayList<Vehicle_Object> temp_list=gson.fromJson(vehicle_gson_array,collectionType);

                        /*******
                         * Removes item from recyclerview and sharedpreferences
                         */
                        temp_list.remove(getAdapterPosition());
                        bikes.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());

                        SharedPreferences.Editor editor = sharedPref.edit();
                        String insert_preference=gson.toJson(temp_list);


                        editor.putString(context.getString(R.string.vehicle_key),insert_preference);

                        editor.apply();


                        /****
                         * applies changes
                         */
                    }

                }
            });

            update_miles.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    final Context context=v.getContext();

                    new MaterialDialog.Builder(v.getContext())
                            .title("Update Mileage")
                            .inputType(InputType.TYPE_CLASS_NUMBER)
                            .inputRange(1,-1)
                            .negativeText("Cancel")
                            .input("E.g. 150000", "", new MaterialDialog.InputCallback()
                            {

                                public void onInput(MaterialDialog dialog, CharSequence input)
                                {
                                    final SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.vehicle_array_preferences), Context.MODE_PRIVATE);
                                    String vehicle_gson_array=sharedPref.getString(context.getString(R.string.vehicle_key),"empty_key");

                                    /******
                                     * Checks if the saved array is empty or not
                                     */

                                    if (vehicle_gson_array.compareTo("empty_key")!=0)
                                    {
                                        Gson gson=new Gson();
                                        Type collectionType = new TypeToken<ArrayList<Vehicle_Object>>(){}.getType();
                                        ArrayList<Vehicle_Object> temp_list=gson.fromJson(vehicle_gson_array,collectionType);

                                        /*******
                                         * Removes item from recyclerview and sharedpreferences
                                         */


                                        Vehicle_Object b=bikes.get(getAdapterPosition());
                                        b.bike_mileage=Integer.parseInt(input.toString());

                                        temp_list.set(getAdapterPosition(),b);
                                        bikes.set(getAdapterPosition(),b);

                                        notifyItemRemoved(getAdapterPosition());

                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        String insert_preference=gson.toJson(temp_list);


                                        editor.putString(context.getString(R.string.vehicle_key),insert_preference);

                                        editor.apply();


                                        /****
                                         * applies changes
                                         */
                                    }
                                }
                            }).show();
                }
            });





        }
    }


}
