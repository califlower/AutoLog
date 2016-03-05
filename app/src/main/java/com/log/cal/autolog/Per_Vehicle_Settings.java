package com.log.cal.autolog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Per_Vehicle_Settings extends AppCompatActivity
{
    ArrayList<String> PATH;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        setupWindowAnimations();
        
        /************
         * Gets the ids of the various objects so that I can talk to them
        */
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_bike_toolbar);
        EditText make_input= (EditText) findViewById(R.id.make_input);
        EditText model_input= (EditText) findViewById(R.id.model_input);
        EditText year_input= (EditText) findViewById(R.id.year_input);
        EditText mile_input= (EditText) findViewById(R.id.mile_input);
        ImageView bike_image_chooser=(ImageView)findViewById(R.id.bike_image_chooser);
        
        toolbar.setTitle("Edit");
        setSupportActionBar(toolbar);


        /****
         * Get which item to populate all the vehicle info with
        */
        
        Bundle inc=getIntent().getExtras();
        int location=(int) inc.get("location");

        List<Vehicle_Object> bike_array_list;
        /********************
         * handles the saving part
         * retrieves vehicles
         */
         
        Context context=Per_Vehicle_Settings.this;
        final SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.vehicle_array_preferences), Context.MODE_PRIVATE);
        String vehicle_gson_array=sharedPref.getString(getString(R.string.vehicle_key),"empty_key");

        /****
         * Pulls the full array from file storage
        ****/
        
        Gson gson=new Gson();
        Type collectionType = new TypeToken<ArrayList<Vehicle_Object>>(){}.getType();
        bike_array_list= gson.<ArrayList<Vehicle_Object>>fromJson(vehicle_gson_array,collectionType);

        Vehicle_Object b=bike_array_list.get(location);
        
        /****
         * Sets all the inputs to the info of the vehicle that is being edited
        ***/
        
        make_input.setText(b.bike_make);
        model_input.setText(b.bike_model);
        year_input.setText(Integer.toString(b.bike_year));
        mile_input.setText(Integer.toString(b.bike_mileage));

        ImageView i=(ImageView)findViewById(R.id.bike_image_chooser);
        Glide.with(this).load(b.path).centerCrop().into(i);
        PATH=new ArrayList<>();
        PATH.add(b.path);

        /***
         * Declares the radio buttons
         * Declared final so that it can be accessed from an inner class
         ****/
        final RadioButton hours=(RadioButton) findViewById(R.id.radio_hour);
        final RadioButton miles=(RadioButton) findViewById(R.id.radio_miles);

        if (b.maint_type.compareToIgnoreCase("Hours")==0)
        {
            hours.setChecked(true);
            miles.setChecked(false);
        }
        else
        {
            hours.setChecked(false);
            miles.setChecked(true);
        }

        /*************
         * Probably should have used a radiogroup but this part manages
         * clicking one radio button and another one being selected.
         */
        hours.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                miles.setChecked(false);
            }
        });

        miles.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                
                hours.setChecked(false);
            }
        });


        try
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        catch (Exception e)
        {
            Intent back=new Intent(this,MainActivity.class);
            startActivity(back);
        }

        bike_image_chooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FishBun.with(Per_Vehicle_Settings.this).setCamera(true).setActionBarColor(Color.parseColor("#03a9f4"), Color.parseColor("#0288d1")).setPickerCount(1).startAlbum();
            }
        });

    }

    public boolean onSupportNavigateUp()
    {

        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);


        Toolbar toolbar = (Toolbar) findViewById(R.id.add_bike_toolbar);

        imm.hideSoftInputFromWindow(toolbar.getWindowToken(), 0);

        Slide slide=new Slide();
        slide.setDuration(500);
        slide.setSlideEdge(3);
        getWindow().setExitTransition(slide);


        Intent i= new Intent(Per_Vehicle_Settings.this, MainActivity.class);
        ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation(Per_Vehicle_Settings.this,toolbar,"add_toolbar");
        ActivityCompat.startActivity(Per_Vehicle_Settings.this,i, options.toBundle());

        return true;

    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_bike_menu, menu);
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

        if (id==R.id.save_vehicle)
        {
            EditText make_input= (EditText) findViewById(R.id.make_input);
            EditText model_input= (EditText) findViewById(R.id.model_input);
            EditText year_input= (EditText) findViewById(R.id.year_input);
            EditText mile_input= (EditText) findViewById(R.id.mile_input);

            RadioButton hours= (RadioButton) findViewById(R.id.radio_hour);

            String maint_type;

            if (hours.isChecked()==false)
            {
                maint_type="Miles";
            }
            else
            {
                maint_type="Hours";
            }

            if (make_input.getText().toString().trim().length()==0)
            {
                Snackbar.make(findViewById(R.id.root_layout), "MISSING AN INPUT", Snackbar.LENGTH_LONG).show();
                return true;
            }
            else if (model_input.getText().toString().trim().length()==0)
            {
                Snackbar.make(findViewById(R.id.root_layout), "MISSING AN INPUT", Snackbar.LENGTH_LONG).show();
                return true;
            }
            else if (year_input.getText().toString().trim().length()==0)
            {
                Snackbar.make(findViewById(R.id.root_layout), "MISSING AN INPUT", Snackbar.LENGTH_LONG).show();
                return true;
            }
            else if (mile_input.getText().toString().trim().length()==0)
            {
                Snackbar.make(findViewById(R.id.root_layout), "MISSING AN INPUT", Snackbar.LENGTH_LONG).show();
                return true;
            }
            else if (Integer.parseInt(year_input.getText().toString().trim())> Calendar.getInstance().get(Calendar.YEAR)+3 || Integer.parseInt(year_input.getText().toString().trim())<1900)
            {
                Snackbar.make(findViewById(R.id.root_layout), "NON REALISTIC CAR YEAR", Snackbar.LENGTH_LONG).show();
                return true;
            }
            else if (Integer.parseInt(mile_input.getText().toString().trim())<0)
            {
                Snackbar.make(findViewById(R.id.root_layout), "CANNOT HAVE AN HOUR OR ODOMETER READING UNDER 0", Snackbar.LENGTH_LONG).show();
                return true;
            }
            else if (PATH==null || PATH.size()==0)
            {
                Snackbar.make(findViewById(R.id.root_layout), "PLEASE ADD A PICTURE", Snackbar.LENGTH_LONG).show();
                return true;
            }
            else
            {
                Intent back_to_main=new Intent(Per_Vehicle_Settings.this,MainActivity.class);
                Bundle inc=getIntent().getExtras();
                
                back_to_main.putExtra("bike_make",make_input.getText().toString().trim());
                back_to_main.putExtra("bike_model",model_input.getText().toString().trim().toUpperCase());
                back_to_main.putExtra("bike_year",year_input.getText().toString().trim());
                back_to_main.putExtra("bike_mile", mile_input.getText().toString().trim());
                back_to_main.putExtra("maint_type", maint_type);
                back_to_main.putExtra("path", PATH.get(0));
                back_to_main.putExtra("location", (int) inc.get("location"));
                back_to_main.putExtra("id","settings_activity");
                startActivity(back_to_main);
                overridePendingTransition(R.anim.appear,R.anim.close_to_point );
                return true;

            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupWindowAnimations()
    {
        Fade fade = new Fade();
        fade.setDuration(500);
        getWindow().setEnterTransition(fade);
    }


    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK)
                {
                    ImageView i=(ImageView)findViewById(R.id.bike_image_chooser);
                    PATH=imageData.getStringArrayListExtra(Define.INTENT_PATH);
                    Glide.with(this).load(PATH.get(0)).centerCrop().into(i);
                    break;
                }
        }
    }


}

