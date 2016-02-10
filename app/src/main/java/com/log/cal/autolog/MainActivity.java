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
        /*
            Holds all the objects
         */
        List<Bike_Object> bike_array_list;
        //Bike_Object Test_Bike=new Bike_Object("Suzuki", "DRZ400SM", 2006, 20000);
        //bike_array_list.add(Test_Bike);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            Type collectionType = new TypeToken<ArrayList<Bike_Object>>(){}.getType();
            ArrayList<Bike_Object> temp_list=gson.fromJson(vehicle_gson_array,collectionType);
            bike_array_list=temp_list;
        }


        Bundle inc=getIntent().getExtras();


        if (inc!=null && inc.get("bike_year")!=null && inc.get("bike_make")!=null && inc.get("bike_model")!=null)
        {

            Gson gson=new Gson();

            Bike_Object new_bike=new Bike_Object(inc.get("bike_make").toString(),inc.get("bike_model").toString(),Integer.parseInt(inc.get("bike_year").toString()),Integer.parseInt(inc.get("bike_mile").toString()));
            bike_array_list.add(new_bike);

            SharedPreferences.Editor editor = sharedPref.edit();

            String insert_preference=gson.toJson(bike_array_list);

            editor.putString(getString(R.string.vehicle_key),insert_preference);

            editor.apply();

        }



        //bike_array_list.add(incoming_bike);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*Handles Recylerview mess */
        /*----------------------------------*/

            RecyclerView bike_list=(RecyclerView) findViewById(R.id.bike_list);
            bike_list.setHasFixedSize(true);
            LinearLayoutManager manager=new LinearLayoutManager(this);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            bike_list.setLayoutManager(manager);

            Bike_Card_Adapter b=new Bike_Card_Adapter(bike_array_list);
            bike_list.setAdapter(b);





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, add_bike.class);
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

    /***
     *Hides the overflow menu from view in the main activity
     */
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        return false;
    }
}
