package com.log.cal.autolog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MaintenanceActivity extends AppCompatActivity
{
    Boolean isMiles;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        setupWindowAnimations();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.maint_toolbar);
        toolbar.setTitle("Maintenance Item List");
        setSupportActionBar(toolbar);
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.maint_fab);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Intent i=this.getIntent();

        /******
         * Determines who started this activity
         * If depending on who started the activity, different things can happen! WOO
         * Mostly just determines what happens to the vehicle list
         *****/

        if (i!=null && i.getExtras()!=null && i.getExtras().get("id")!=null)
        {
            if (i.getExtras().get("id").toString().compareToIgnoreCase("add_activity")==0)
            {
               editList();
            }
            else if (i.getExtras().get("id").toString().compareToIgnoreCase("edit_activity")==0)
            {
                editItemList();
            }
            else
            {
                initList();
            }
        }
        else
        {
            initList();
        }





        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i= new Intent(MaintenanceActivity.this, MaintenanceAddActivity.class);
                Bundle inc=getIntent().getExtras();
                i.putExtra("location",(int) inc.get("location"));
                i.putExtra("isMiles",isMiles);
                ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation(MaintenanceActivity.this, toolbar,"add_toolbar");
                ActivityCompat.startActivity(MaintenanceActivity.this,i, options.toBundle());
            }
        });


    }


    public boolean onSupportNavigateUp()
    {
        final Toolbar t = (Toolbar) findViewById(R.id.maint_toolbar);
        final FloatingActionButton f= (FloatingActionButton) findViewById(R.id.maint_fab);

        Slide slide=new Slide();
        slide.setDuration(500);
        slide.setSlideEdge(3);
        getWindow().setExitTransition(slide);

        Intent i= new Intent(MaintenanceActivity.this, MainActivity.class);

        Pair<View, String> toolbar = Pair.create((View)t, "add_toolbar");
        Pair<View, String> fab = Pair.create((View)f, "fab");

        ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation(MaintenanceActivity.this,fab,toolbar);
        ActivityCompat.startActivity(MaintenanceActivity.this,i, options.toBundle());

        return true;
    }

    private void setupWindowAnimations()
    {
        Fade fade = new Fade();
        fade.setDuration(500);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
    }

    private void initList()
    {
        List<Vehicle_Object> bike_array_list;
        /********************
         * handles the saving part
         * retrieves vehicles
         */
        Context context=this;
        final SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.vehicle_array_preferences), Context.MODE_PRIVATE);
        String vehicle_gson_array=sharedPref.getString(getString(R.string.vehicle_key),"empty_key");

        /******
         * Checks if the saved array is empty or not
         */

        Gson gson=new Gson();
        Type collectionType = new TypeToken<ArrayList<Vehicle_Object>>(){}.getType();
        bike_array_list= gson.<ArrayList<Vehicle_Object>>fromJson(vehicle_gson_array,collectionType);

        Bundle inc=getIntent().getExtras();
        final int location=(int) inc.get("location");

        Vehicle_Object list_extract=bike_array_list.get(location);
        RecyclerView maint_listview = (RecyclerView) findViewById(R.id.maint_list);

        List<Maint_Object> maint_list = list_extract.maint_list;

        Collections.sort(maint_list);

        list_extract.maint_list=maint_list;
        bike_array_list.set(location,list_extract);

        SharedPreferences.Editor editor = sharedPref.edit();
        String insert_preference=gson.toJson(bike_array_list);
        editor.putString(getString(R.string.vehicle_key),insert_preference);
        editor.apply();

        boolean is_miles= list_extract.maint_type.compareToIgnoreCase("Miles")==0? true:false;
        isMiles=is_miles;

        maint_listview.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        maint_listview.setLayoutManager(manager);
        Maint_Card_Adapter m = new Maint_Card_Adapter(maint_list,list_extract.bike_mileage,location,is_miles);
        maint_listview.setAdapter(m);


        final FloatingActionButton maint_fab=(FloatingActionButton)findViewById(R.id.maint_fab);
        maint_listview.addOnScrollListener(new Add_Fab_Behavior()
        {
            @Override
            public void show()
            {
                maint_fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide()
            {
                maint_fab.animate().translationY(maint_fab.getHeight() + 48).setInterpolator(new AccelerateInterpolator(2)).start();
            }
        });


    }

    private void editList()
    {
            List<Vehicle_Object> bike_array_list;
            /********************
             * handles the saving part
             * retrieves vehicles
             */
            Gson gson=new Gson();
            Context context=this;
            Bundle inc=getIntent().getExtras();

            final SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.vehicle_array_preferences), Context.MODE_PRIVATE);
            final int location=(int) inc.get("location");

            RecyclerView maint_listview = (RecyclerView) findViewById(R.id.maint_list);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            Type collectionType = new TypeToken<ArrayList<Vehicle_Object>>(){}.getType();
            String vehicle_gson_array=sharedPref.getString(getString(R.string.vehicle_key),"empty_key");

            /******
             * Checks if the saved array is empty or not
             */

            bike_array_list= gson.<ArrayList<Vehicle_Object>>fromJson(vehicle_gson_array,collectionType);


            Vehicle_Object list_extract=bike_array_list.get(location);

            List<Maint_Object> maint_list = list_extract.maint_list;
            Collections.sort(maint_list);

            Type temp = new TypeToken<Maint_Object>(){}.getType();

            Maint_Object temp_maint=gson.fromJson(inc.get("obj").toString(),temp);

            maint_list.add(temp_maint);
            list_extract.maint_list=maint_list;

            boolean is_miles= list_extract.maint_type.compareToIgnoreCase("Miles")==0? true:false;
            isMiles=is_miles;
            bike_array_list.set((int) inc.get("location"),list_extract);

            Collections.sort(maint_list);

            SharedPreferences.Editor editor = sharedPref.edit();
            String insert_preference=gson.toJson(bike_array_list);
            editor.putString(getString(R.string.vehicle_key),insert_preference);
            editor.apply();



            maint_listview.setHasFixedSize(true);

            manager.setOrientation(LinearLayoutManager.VERTICAL);
            maint_listview.setLayoutManager(manager);
            Maint_Card_Adapter m = new Maint_Card_Adapter(maint_list,list_extract.bike_mileage,location,is_miles);
            maint_listview.setAdapter(m);



            final FloatingActionButton maint_fab=(FloatingActionButton)findViewById(R.id.maint_fab);
            maint_listview.addOnScrollListener(new Add_Fab_Behavior()
            {
                @Override
                public void show()
                {
                    maint_fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                }

            @Override
            public void hide()
            {
                maint_fab.animate().translationY(maint_fab.getHeight() + 48).setInterpolator(new AccelerateInterpolator(2)).start();
            }
            });





    }
    private void editItemList()
    {
        List<Vehicle_Object> bike_array_list;
        /********************
         * handles the saving part
         * retrieves vehicles
         */
        Gson gson=new Gson();
        Context context=this;
        Bundle inc=getIntent().getExtras();

        final SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.vehicle_array_preferences), Context.MODE_PRIVATE);
        final int location=(int) inc.get("location");

        RecyclerView maint_listview = (RecyclerView) findViewById(R.id.maint_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        Type collectionType = new TypeToken<ArrayList<Vehicle_Object>>(){}.getType();
        String vehicle_gson_array=sharedPref.getString(getString(R.string.vehicle_key),"empty_key");

        /******
         * Checks if the saved array is empty or not
         */

        bike_array_list= gson.<ArrayList<Vehicle_Object>>fromJson(vehicle_gson_array,collectionType);


        Vehicle_Object list_extract=bike_array_list.get(location);

        List<Maint_Object> maint_list = list_extract.maint_list;
        Collections.sort(maint_list);

        Type temp = new TypeToken<Maint_Object>(){}.getType();

        Maint_Object temp_maint=gson.fromJson(inc.get("obj").toString(),temp);

        maint_list.set((int)inc.get("maint_loc"),temp_maint);
        list_extract.maint_list=maint_list;


        boolean is_miles= list_extract.maint_type.compareToIgnoreCase("Miles")==0? true:false;
        isMiles=is_miles;

        bike_array_list.set((int) inc.get("location"),list_extract);

        SharedPreferences.Editor editor = sharedPref.edit();
        String insert_preference=gson.toJson(bike_array_list);
        editor.putString(getString(R.string.vehicle_key),insert_preference);
        editor.apply();

        maint_listview.setHasFixedSize(true);

        manager.setOrientation(LinearLayoutManager.VERTICAL);
        maint_listview.setLayoutManager(manager);
        Maint_Card_Adapter m = new Maint_Card_Adapter(maint_list,list_extract.bike_mileage,location,is_miles);
        maint_listview.setAdapter(m);


        final FloatingActionButton maint_fab=(FloatingActionButton)findViewById(R.id.maint_fab);
        maint_listview.addOnScrollListener(new Add_Fab_Behavior()
        {
            @Override
            public void show()
            {
                maint_fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide()
            {
                maint_fab.animate().translationY(maint_fab.getHeight() + 48).setInterpolator(new AccelerateInterpolator(2)).start();
            }
        });

    }

}
