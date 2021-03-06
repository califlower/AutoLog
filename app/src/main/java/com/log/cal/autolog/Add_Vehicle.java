package com.log.cal.autolog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import java.util.ArrayList;
import java.util.Calendar;

public class Add_Vehicle extends AppCompatActivity
{

    ArrayList<String> PATH;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_bike_toolbar);
        ImageView bike_image_chooser=(ImageView)findViewById(R.id.bike_image_chooser);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        final RadioButton hours=(RadioButton) findViewById(R.id.radio_hour);
        final RadioButton miles=(RadioButton) findViewById(R.id.radio_miles);

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
                FishBun.with(Add_Vehicle.this).setCamera(true).setActionBarColor(Color.parseColor("#03a9f4"), Color.parseColor("#0288d1")).setPickerCount(1).startAlbum();
            }
        });

    }

    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        overridePendingTransition(R.anim.from_left_in,R.anim.to_right_out);
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
            else if (PATH==null ||PATH.size()==0)
            {
                Snackbar.make(findViewById(R.id.root_layout), "PLEASE ADD A PICTURE", Snackbar.LENGTH_LONG).show();
                return true;
            }
            else
            {
                Intent back_to_main=new Intent(Add_Vehicle.this,MainActivity.class);
                back_to_main.putExtra("bike_make",make_input.getText().toString().trim());
                back_to_main.putExtra("bike_model",model_input.getText().toString().trim().toUpperCase());
                back_to_main.putExtra("bike_year",year_input.getText().toString().trim());
                back_to_main.putExtra("bike_mile", mile_input.getText().toString().trim());
                back_to_main.putExtra("maint_type", maint_type);
                back_to_main.putExtra("id", "add_activity");
                back_to_main.putExtra("path", PATH.get(0));
                startActivity(back_to_main);
                overridePendingTransition(R.anim.appear,R.anim.close_to_point );
                return true;

            }

        }

        return super.onOptionsItemSelected(item);
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
                    Glide.with(this).load(imageData.getStringArrayListExtra(Define.INTENT_PATH).get(0)).centerCrop().into(i);
                    break;
                }
        }
    }

}
