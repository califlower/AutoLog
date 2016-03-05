package com.log.cal.autolog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.InputCallback;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.log.cal.autolog.R.id;
import com.log.cal.autolog.R.layout;
import com.log.cal.autolog.R.string;
import com.log.cal.autolog.Vehicle_Card_Adapter.Bike_Card_ViewHolder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Vehicle_Card_Adapter extends Adapter<Bike_Card_ViewHolder>
{
    private final List<Vehicle_Object> bikes;

    public Vehicle_Card_Adapter(List<Vehicle_Object> bikes)
    {
        this.bikes=bikes;
    }

    @Override
    public Bike_Card_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(layout.vehicle_card,parent,false);
        return new Vehicle_Card_Adapter.Bike_Card_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Vehicle_Card_Adapter.Bike_Card_ViewHolder holder, int position)
    {

        Vehicle_Object b= this.bikes.get(position);

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
            holder.upcoming.setTextColor(Color.parseColor("#e57373"));
            holder.est_cost.setText("Not Applicable");
            holder.est_cost.setTextColor(Color.parseColor("#e57373"));
        }
        else
        {
            Collections.sort(b.maint_list);
            Maint_Object m=b.maint_list.get(0);
            holder.upcoming.setText(m.name);
            holder.est_cost.setText('$'+ m.cost.toString());
        }


    }

    @Override
    public int getItemCount()
    {
        return this.bikes.size();
    }

    public class Bike_Card_ViewHolder extends ViewHolder
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

            this.bike_make =(TextView) v.findViewById(id.card_make);
            this.bike_model =(TextView) v.findViewById(id.card_model);
            this.bike_year =(TextView) v.findViewById(id.card_year);
            this.bike_mile =(TextView) v.findViewById(id.card_mileage);
            this.update_miles =(TextView) v.findViewById(id.update_miles);
            this.remove_bike =(TextView) v.findViewById(id.remove_bike);
            this.bike_image =(ImageView)v.findViewById(id.vehicle_image);
            this.maint_type =(TextView) v.findViewById(id.maint_type_text);
            this.settings =(ImageButton) v.findViewById(id.bike_settings);
            this.maint_list =(ImageButton) v.findViewById(id.maintenance_list_button);
            this.upcoming =(TextView)v.findViewById(id.next_maint);
            this.est_cost =(TextView)v.findViewById(id.est_cost_item);


            this.maint_list.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    //Kinda hacky, totes works//

                    AppCompatActivity a= (AppCompatActivity) v.getContext();

                    Toolbar t=(Toolbar) a.findViewById(id.toolbar);
                    FloatingActionButton f= (FloatingActionButton) a.findViewById(id.fab);

                    Intent maintenance=new Intent(v.getContext(),MaintenanceActivity.class);
                    maintenance.putExtra("location", Vehicle_Card_Adapter.Bike_Card_ViewHolder.this.getAdapterPosition());

                    Pair<View, String> toolbar = Pair.create((View)t, "add_toolbar");
                    Pair<View, String> fab = Pair.create((View)f, "fab");

                    ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) v.getContext(),toolbar,fab);
                    ActivityCompat.startActivity((Activity) v.getContext(),maintenance, options.toBundle());


                }
            });




            OnClickListener setting_click=new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity a = (AppCompatActivity) v.getContext();

                    Toolbar t = (Toolbar) a.findViewById(id.toolbar);
                    RelativeLayout r = (RelativeLayout) a.findViewById(id.card_expand_vehicle);

                    Intent bike_settings = new Intent(v.getContext(), Per_Vehicle_Settings.class);

                    bike_settings.putExtra("location", Vehicle_Card_Adapter.Bike_Card_ViewHolder.this.getAdapterPosition());

                    Pair<View, String> toolbar = Pair.create((View) t, "add_toolbar");
                    Pair<View, String> lay = Pair.create((View) r, "card_expand_vehicle");

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) v.getContext(), toolbar, lay);
                    ActivityCompat.startActivity((Activity) v.getContext(), bike_settings, options.toBundle());
                }
            };


            this.settings.setOnClickListener(setting_click);
            this.bike_image.setOnClickListener(setting_click);



            this.remove_bike.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {


                    /****************
                     * Handles the issue of not removing saved objects
                     */
                    Context context=v.getContext();
                    SharedPreferences sharedPref = context.getSharedPreferences(context.getString(string.vehicle_array_preferences), Context.MODE_PRIVATE);
                    String vehicle_gson_array=sharedPref.getString(context.getString(string.vehicle_key),"empty_key");

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
                        temp_list.remove(Vehicle_Card_Adapter.Bike_Card_ViewHolder.this.getAdapterPosition());
                        Vehicle_Card_Adapter.this.bikes.remove(Vehicle_Card_Adapter.Bike_Card_ViewHolder.this.getAdapterPosition());
                        Vehicle_Card_Adapter.this.notifyItemRemoved(Vehicle_Card_Adapter.Bike_Card_ViewHolder.this.getAdapterPosition());

                        Editor editor = sharedPref.edit();
                        String insert_preference=gson.toJson(temp_list);


                        editor.putString(context.getString(string.vehicle_key),insert_preference);

                        editor.apply();


                        /****
                         * applies changes
                         */
                    }

                }
            });

            this.update_miles.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    final Context context=v.getContext();

                    new Builder(v.getContext())
                            .title("Update Mileage")
                            .inputType(InputType.TYPE_CLASS_NUMBER)
                            .inputRange(1,-1)
                            .negativeText("Cancel")
                            .input("E.g. 150000", "", new InputCallback()
                            {

                                @Override
                                public void onInput(MaterialDialog dialog, CharSequence input)
                                {
                                    SharedPreferences sharedPref = context.getSharedPreferences(context.getString(string.vehicle_array_preferences), Context.MODE_PRIVATE);
                                    String vehicle_gson_array=sharedPref.getString(context.getString(string.vehicle_key),"empty_key");

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


                                        Vehicle_Object b= Vehicle_Card_Adapter.this.bikes.get(Vehicle_Card_Adapter.Bike_Card_ViewHolder.this.getAdapterPosition());
                                        b.bike_mileage=Integer.parseInt(input.toString());

                                        temp_list.set(Vehicle_Card_Adapter.Bike_Card_ViewHolder.this.getAdapterPosition(),b);
                                        Vehicle_Card_Adapter.this.bikes.set(Vehicle_Card_Adapter.Bike_Card_ViewHolder.this.getAdapterPosition(),b);

                                        Vehicle_Card_Adapter.this.notifyItemRemoved(Vehicle_Card_Adapter.Bike_Card_ViewHolder.this.getAdapterPosition());

                                        Editor editor = sharedPref.edit();
                                        String insert_preference=gson.toJson(temp_list);


                                        editor.putString(context.getString(string.vehicle_key),insert_preference);

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
