package com.example.bast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.TextView;

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

    private Dialog addDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_general_list);

        final TextView title = (TextView) findViewById(R.id.activity_title);
        title.setText("SYSTEMS");

        Log.d(TAG, "onCreate: started");

        Log.d(TAG, "initRecyclerView: init recyclerview.");

        initRecyclerView();
    }

    // initializes the view of roles
    private void initRecyclerView() {
        rv = findViewById(R.id.recycler_view);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initButton() {
        Button add_button = (Button) findViewById(R.id.add_btn);
        addDialog = new Dialog(this);

        // Popup add user menu
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectSystem();
            }
        });
    }

    private void connectSystem() {
        addDialog.setContentView(R.layout.connect_system);

        TextView title = (TextView) addDialog.findViewById(R.id.connect_title);
        EditText name = (EditText) addDialog.findViewById(R.id.system_name);

        Button add_role = (Button) addDialog.findViewById(R.id.add_button);
        add_role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Connect to system
            }
        });

        addDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        addDialog.show();
    }
}
