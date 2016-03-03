package com.log.cal.autolog;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Maintenance_Edit_Activity extends AppCompatActivity
{

    Maint_Object m;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_edit);
        setupWindowAnimations();
        Toolbar toolbar = (Toolbar) findViewById(R.id.maint_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        Intent i=this.getIntent();
        Gson g=new Gson();
        Type t= new TypeToken<Maint_Object>(){}.getType();

        m= g.fromJson(i.getExtras().get("obj").toString(),t);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        EditText name= (EditText)findViewById(R.id.maint_name_input);
        EditText interval= (EditText)findViewById(R.id.maint_interval_input);
        EditText cost= (EditText)findViewById(R.id.maint_cost_input);
        final LinearLayout llpast=(LinearLayout)findViewById(R.id.llpast);

        name.setText(m.name);

        String a= String.valueOf(m.interval);
        interval.setText(a);
        cost.setText(m.cost.toString());


        for (int r=0; r<m.history.size();r++)
        {
            final int current=r;
            RelativeLayout view_Add=new RelativeLayout(this);
            TextView text=new TextView(this);
            ImageButton del=new ImageButton(this);

            RelativeLayout.LayoutParams add_params=new RelativeLayout.LayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            RelativeLayout.LayoutParams text_params=new RelativeLayout.LayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            RelativeLayout.LayoutParams del_params=new RelativeLayout.LayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            view_Add.setLayoutParams(add_params);

            text_params.addRule(RelativeLayout.ALIGN_PARENT_START);
            text_params.topMargin=50;
            text_params.setMarginStart(30);
            text.setLayoutParams(text_params);
            text.setTextSize(15);

            del.setBackgroundResource(R.drawable.ic_close_black_24dp);
            del_params.topMargin=50;
            del_params.setMarginEnd(30);
            del_params.addRule(RelativeLayout.RIGHT_OF,text.getId());
            del_params.addRule(RelativeLayout.ALIGN_PARENT_END,text.getId());
            del.setLayoutParams(del_params);


            view_Add.addView(text);
            view_Add.addView(del);








            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

            Date d=m.history.get(r).date;
            String s=sdf.format(d);
            text.setText("Done at "+ m.history.get(r).miles+ " on " + s);

            text.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));

            del.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    m.history.remove(current);
                    llpast.removeViewAt(current);
                }
            });

            llpast.addView(view_Add);


        }







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


            if (name.getText().toString().trim().length()==0 || cost.getText().toString().trim().length()==0||interval.getText().toString().trim().length()==0)
            {
                Snackbar.make(findViewById(R.id.root_layout), "MISSING AN INPUT", Snackbar.LENGTH_LONG).show();
                return true;
            }
            else
            {

                Intent back_to_main=new Intent(Maintenance_Edit_Activity.this,MaintenanceActivity.class);
                Gson g=new Gson();
                Bundle inc=getIntent().getExtras();
                final int location=(int) inc.get("location");
                Intent i=this.getIntent();
                Type t= new TypeToken<Maint_Object>(){}.getType();


                Maint_Object obj=new Maint_Object(name.getText().toString().trim(),Double.parseDouble(cost.getText().toString().trim()),Integer.parseInt(interval.getText().toString().trim()),m.history);

                back_to_main.putExtra("obj",g.toJson(obj));
                back_to_main.putExtra("id", "edit_activity");
                back_to_main.putExtra("location",location);
                back_to_main.putExtra("maint_loc", (int) inc.get("maint_loc"));

                ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation(Maintenance_Edit_Activity.this);
                ActivityCompat.startActivity(Maintenance_Edit_Activity.this,back_to_main, options.toBundle());

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

        Intent i= new Intent(Maintenance_Edit_Activity.this, MaintenanceActivity.class);
        Bundle inc=getIntent().getExtras();
        i.putExtra("location",(int) inc.get("location"));
        ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation(Maintenance_Edit_Activity.this,toolbar,"add_toolbar");
        ActivityCompat.startActivity(Maintenance_Edit_Activity.this,i, options.toBundle());
        return true;
    }



}
