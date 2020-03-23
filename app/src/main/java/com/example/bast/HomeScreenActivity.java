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
}
