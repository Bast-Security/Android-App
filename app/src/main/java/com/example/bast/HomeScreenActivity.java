package com.example.bast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.bast.objects.System;

import java.util.ArrayList;

public class HomeScreenActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayList<System> systems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_screen);
        Log.d(TAG, "onCreate: started");

        systems.add(new System("System 1", true));
        systems.add(new System("System 2", false));
        systems.add(new System("System 3", true));

        initRecyclerView();
    }

    /**
     * Initializes the Recycler View and its adapter
     *
     * This class has no useful logic; it's just a documentation example.
     */
    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView rv = findViewById(R.id.rvSystems);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(systems, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

    }

}
