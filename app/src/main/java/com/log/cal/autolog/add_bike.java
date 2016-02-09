package com.log.cal.autolog;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Calendar;

public class add_bike extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bike);
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_bike_toolbar);
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
                TextView mile_header= (TextView) findViewById(R.id.mile_header);
                mile_header.setText("Odometer Hour Reading");
            }
        });

        miles.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                hours.setChecked(false);
                TextView mile_header= (TextView) findViewById(R.id.mile_header);
                mile_header.setText("Odometer Reading");



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
                Snackbar.make(findViewById(R.id.root_layout), "CANNOT HAVE AN HOUR OR ODEMETER READING UNDER 0", Snackbar.LENGTH_LONG).show();
                return true;
            }
            else
            {
                Intent back_to_main=new Intent(add_bike.this,MainActivity.class);
                back_to_main.putExtra("bike_make",make_input.getText().toString().trim());
                back_to_main.putExtra("bike_model",model_input.getText().toString().trim().toUpperCase());
                back_to_main.putExtra("bike_year",year_input.getText().toString().trim());
                back_to_main.putExtra("bike_mile", mile_input.getText().toString().trim());
                startActivity(back_to_main);
                overridePendingTransition(R.anim.appear,R.anim.close_to_point );
                return true;

            }




        }

        return super.onOptionsItemSelected(item);
    }

}
