package com.log.cal.autolog;

import android.content.Intent;
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
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MaintenanceAddActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_add);
        setupWindowAnimations();
        Toolbar toolbar = (Toolbar) findViewById(R.id.maint_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);





    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_bike_menu, menu);
        return true;
    }

    private void setupWindowAnimations()
    {
        Fade fade = new Fade();
        fade.setDuration(500);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.save_vehicle)
        {
            EditText name= (EditText)findViewById(R.id.maint_name_input);
            EditText interval= (EditText)findViewById(R.id.maint_interval_input);
            EditText cost= (EditText)findViewById(R.id.maint_cost_input);
            EditText last_done=(EditText)findViewById(R.id.maint_last_done_input);

            if (name.getText().toString().trim().length()==0 || cost.getText().toString().trim().length()==0||interval.getText().toString().trim().length()==0 || last_done.getText().toString().trim().length()==0)
            {
                Snackbar.make(findViewById(R.id.root_layout), "MISSING AN INPUT", Snackbar.LENGTH_LONG).show();
                return true;
            }
            else
            {
                Intent back_to_main=new Intent(MaintenanceAddActivity.this,MaintenanceActivity.class);

                Gson g=new Gson();

                List<Date> dates_done=new ArrayList<>();
                List<Integer>miles_done=new ArrayList<>();

                int temp=Integer.parseInt(String.valueOf(last_done.getText()));
                miles_done.add(temp);

                Maint_Object obj=new Maint_Object(name.getText().toString().trim(),Double.parseDouble(cost.getText().toString().trim()),Integer.parseInt(interval.getText().toString().trim()),dates_done,miles_done);


                Bundle inc=getIntent().getExtras();
                final int location=(int) inc.get("location");

                back_to_main.putExtra("obj",g.toJson(obj));
                back_to_main.putExtra("id", "add_activity");
                back_to_main.putExtra("location",location);

                ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation(MaintenanceAddActivity.this);
                ActivityCompat.startActivity(MaintenanceAddActivity.this,back_to_main, options.toBundle());

            }
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onSupportNavigateUp()
    {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.maint_toolbar);
        Slide slide=new Slide();
        slide.setDuration(500);
        slide.setSlideEdge(3);
        getWindow().setExitTransition(slide);

        Intent i= new Intent(MaintenanceAddActivity.this, MaintenanceActivity.class);
        Bundle inc=getIntent().getExtras();
        i.putExtra("location",(int) inc.get("location"));
        ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation(MaintenanceAddActivity.this,toolbar,"add_toolbar");
        ActivityCompat.startActivity(MaintenanceAddActivity.this,i, options.toBundle());
        return true;
    }



}
