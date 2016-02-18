package com.log.cal.autolog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        
        final FloatingActionButton add_fab=(FloatingActionButton) findViewById(R.id.fab);
        List<Vehicle_Object> bike_array_list;
        RecyclerView bike_list=(RecyclerView) findViewById(R.id.bike_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        
        Intent i=this.getIntent();

        /******
         * Determines who started this activity
         * If depending on who started the activity, different things can happen! WOO
         * Mostly just determines what happens to the vehicle list
        *****/
        
        if (i!=null && i.getExtras()!=null && i.getExtras().get("id")!=null)
        {
            if (i.getExtras().get("id").toString().compareToIgnoreCase("settings_activity")==0)
            {
                bike_array_list=initializeEditList();
            }
            else
            {
                bike_array_list=initializeList();
            }
        }
        else
        {
            bike_array_list=initializeList();
        }


        /*****
         * Handles the recyclerview disaster
         * Like, why is there so much code to implement a funky listview?
         * Actually its not that bad, once it works it's pretty dope
         **********/

        bike_list.setHasFixedSize(true);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        bike_list.setLayoutManager(manager);
        Vehicle_Card_Adapter b=new Vehicle_Card_Adapter(bike_array_list);
        bike_list.setAdapter(b);

        /****
         * Sets a listener that hides the fab when scrolling
        *****/
        
        bike_list.addOnScrollListener(new Add_Fab_Behavior()
        {
            @Override
            public void show()
            {
                add_fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide()
            {
                add_fab.animate().translationY(add_fab.getHeight() + 48).setInterpolator(new AccelerateInterpolator(2)).start();
            }
        });


        add_fab.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, Add_Vehicle.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up_incoming,R.anim.slide_up_outgoing);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<Vehicle_Object> initializeList()
    {
        List<Vehicle_Object> bike_array_list;
        /********************
         * handles the saving part
         * retrieves vehicles
         */
        Context context=MainActivity.this;
        final SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.vehicle_array_preferences), Context.MODE_PRIVATE);
        String vehicle_gson_array=sharedPref.getString(getString(R.string.vehicle_key),"empty_key");

        /******
         * Checks if the saved array is empty or not
         */

        if (vehicle_gson_array.compareTo("empty_key")==0)
        {
            bike_array_list=new ArrayList<>();
        }
        else
        {
            Gson gson=new Gson();
            Type collectionType = new TypeToken<ArrayList<Vehicle_Object>>(){}.getType();
            bike_array_list= gson.<ArrayList<Vehicle_Object>>fromJson(vehicle_gson_array,collectionType);
        }


        Bundle inc=getIntent().getExtras();


        if (inc!=null && inc.get("bike_year")!=null && inc.get("bike_make")!=null && inc.get("bike_model")!=null)
        {

            Gson gson=new Gson();

            List<Maint_Object> maint_list=new ArrayList<>();
            Vehicle_Object new_bike=new Vehicle_Object
                    (
                            inc.get("bike_make").toString(),
                            inc.get("bike_model").toString(),
                            Integer.parseInt(inc.get("bike_year").toString()),
                            Integer.parseInt(inc.get("bike_mile").toString()),
                            inc.get("maint_type").toString(),
                            inc.get("path").toString(),maint_list
                    );
            bike_array_list.add(new_bike);

            SharedPreferences.Editor editor = sharedPref.edit();

            String insert_preference=gson.toJson(bike_array_list);

            editor.putString(getString(R.string.vehicle_key),insert_preference);

            editor.apply();



        }
        return bike_array_list;
    }

    private List<Vehicle_Object> initializeEditList()
    {
        List<Vehicle_Object> bike_array_list;
        /********************
         * handles the saving part
         * retrieves vehicles
         */
        Context context=MainActivity.this;
        final SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.vehicle_array_preferences), Context.MODE_PRIVATE);
        String vehicle_gson_array=sharedPref.getString(getString(R.string.vehicle_key),"empty_key");

        /******
         * Checks if the saved array is empty or not
         */

        if (vehicle_gson_array.compareTo("empty_key")==0)
        {
            bike_array_list=new ArrayList<>();
        }
        else
        {
            Gson gson=new Gson();
            Type collectionType = new TypeToken<ArrayList<Vehicle_Object>>(){}.getType();
            bike_array_list= gson.<ArrayList<Vehicle_Object>>fromJson(vehicle_gson_array,collectionType);
        }


        Bundle inc=getIntent().getExtras();


        if (inc!=null && inc.get("bike_year")!=null && inc.get("bike_make")!=null && inc.get("bike_model")!=null && inc.get("location")!=null && inc.get("path")!=null)
        {

            Gson gson=new Gson();

            Vehicle_Object new_bike=new Vehicle_Object
                    (
                            inc.get("bike_make").toString(),
                            inc.get("bike_model").toString(),
                            Integer.parseInt(inc.get("bike_year").toString()),
                            Integer.parseInt(inc.get("bike_mile").toString()),
                            inc.get("maint_type").toString(),
                            inc.get("path").toString(),bike_array_list.get((int) inc.get("location")).maint_list
                    );
            bike_array_list.set((int) inc.get("location"),new_bike);

            SharedPreferences.Editor editor = sharedPref.edit();

            String insert_preference=gson.toJson(bike_array_list);

            editor.putString(getString(R.string.vehicle_key),insert_preference);

            editor.apply();



        }
        return bike_array_list;

    }

    /***
     *Hides the overflow menu from view in the main activity
     */
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        return false;
    }
}
