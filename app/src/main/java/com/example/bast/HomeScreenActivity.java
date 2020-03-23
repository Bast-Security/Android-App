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
        Log.d(TAG, "onCreate: started");

        Log.d(TAG, "initRecyclerView: init recyclerview.");
        initRecyclerView();

        final SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        if (!prefs.getBoolean("createdKey", false)) {
            try {
                final KeyPairGenerator kpg = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore");

                final KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder("signer", KeyProperties.PURPOSE_SIGN)
                        .setDigests(KeyProperties.DIGEST_SHA256)
                        .setAlgorithmParameterSpec(new ECGenParameterSpec("P-384"))
                        .build();

                kpg.initialize(spec);
                kpg.generateKeyPair();

                prefs.edit().putBoolean("createdKey", true).apply();

                Log.d("key","Created EC key and Registered");
            } catch (Exception e) {
                Log.d("key", "Failed to create EC key: " + e.getMessage());
            }
        } else {
            Log.d("key", "EC key previously created.");
        }

        if (prefs.getBoolean("createdKey", false)) {
            try {
                final KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
                keyStore.load(null);
                KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry("signer", null);
                final ECPublicKey pubKey = (ECPublicKey) entry.getCertificate().getPublicKey();

                if (!prefs.getBoolean("registered", false)) {
                    try {
                        final JSONObject payload = new JSONObject()
                                .accumulate("X", pubKey.getW().getAffineX())
                                .accumulate("Y", pubKey.getW().getAffineY());

                        final Request register = HTTP.post("register", payload);
                        HTTP.doRequest(register, (response) -> {
                            prefs.edit().putBoolean("registered", true).apply();
                            response.close();
                        });
                    } catch (Exception e) {
                        Log.d("register", e.toString());
                    }
                }

                if (prefs.getBoolean("registered", false)) {
                    Session.doLogin(entry.getPrivateKey(), (session) -> {
                        Log.d("login", "logged in successfully");
                    });
                }
            } catch (Exception e) {
                Log.d("register", "couldn't load keys " + e.toString());
            }
        }
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
