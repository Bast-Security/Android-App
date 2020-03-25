package com.example.bast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import com.example.bast.list_adapters.SystemsAdapter;
import com.example.bast.objects.HTTP;
import com.example.bast.objects.Session;
import com.example.bast.objects.System;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.security.Key;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HomeScreenActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView rv;
    private ArrayList<System> systems = new ArrayList<System>();
    private SystemsAdapter adapter = new SystemsAdapter(systems, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_screen);
        Log.d(TAG, "onCreate: started");

        Log.d(TAG, "initRecyclerView: init recyclerview.");
        rv = findViewById(R.id.rvSystems);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
