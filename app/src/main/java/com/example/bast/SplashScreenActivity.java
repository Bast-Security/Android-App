package com.example.bast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bast.objects.Async;
import com.example.bast.objects.HTTP;
import com.example.bast.objects.Session;

import org.json.JSONObject;

import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;

import okhttp3.Request;
import okhttp3.Response;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(final Bundle savedStateInstance) {
        super.onCreate(savedStateInstance);
        setContentView(R.layout.splash_screen);

        Log.d("splash", "AM I EVEN REAL");

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

                Async.task(() -> {
                    if (!prefs.getBoolean("registered", false)) {
                        Log.d("register", "Registering account");
                        try {
                            final JSONObject payload = new JSONObject()
                                    .accumulate("X", pubKey.getW().getAffineX())
                                    .accumulate("Y", pubKey.getW().getAffineY());

                            Log.d("register", "Sending register POST");
                            final Request register = HTTP.post("register", payload);
                            try (final Response response = HTTP.doSyncRequest(register)) {
                                if (response.code() == 200) {
                                    final int id = new JSONObject(response.body().string()).getInt("id");
                                    prefs.edit().putInt("id", id).apply();
                                    prefs.edit().putBoolean("registered", true).apply();
                                }
                            }
                        } catch (Exception e) {
                            Log.d("register", "Failed to register " + e.toString());
                        }
                    }

                    if (prefs.getBoolean("registered", false)) {
                        Log.d("login", "attempting to log in");
                        try {
                            final Session session = Session.login(entry.getPrivateKey(), prefs.getInt("id", -1));
                            Log.d("login", "logged in successfully");

                            final Intent intent = new Intent(this, HomeScreenActivity.class);
                            this.startActivity(intent);
                        } catch (Exception e) {
                            Log.d("login", "Failed to login " + e.toString());
                        }
                    }
                });
            } catch (Exception e) {
                Log.d("register", "couldn't load keys " + e.toString());
            }
        }
    }
}
