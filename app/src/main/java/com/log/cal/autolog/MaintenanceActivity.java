package com.log.cal.autolog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;

import java.util.List;

public class MaintenanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        setupWindowAnimations();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView maint_listview = (RecyclerView) findViewById(R.id.maint_list);

        Bike_Object list_extract = new Bike_Object("Suzuki", "DRZ400sm", 2009, 250000, "miles", null, null);
        List<Maint_Object> maint_list = list_extract.maint_list;


        maint_listview.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);

        manager.setOrientation(LinearLayoutManager.VERTICAL);
        maint_listview.setLayoutManager(manager);
        Maint_Card_Adapter m = new Maint_Card_Adapter(maint_list);
        maint_listview.setAdapter(m);
    }


    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
    }
}
