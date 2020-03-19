package com.example.bast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.widget.Button;

import com.example.bast.list_adapters.SystemsAdapter;
import com.example.bast.objects.HTTP;
import com.example.bast.objects.ServiceFinder;
import com.example.bast.objects.System;

import org.spongycastle.jcajce.provider.asymmetric.ec.KeyPairGeneratorSpi;
import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class HomeScreenActivity extends AppCompatActivity {
    // Initialize Discovery Listener and NSD Manager


    private Key privateKey;
    public Key publicKey;

    private static final String TAG = "MainActivity";

    private RecyclerView rv;
    private ArrayList<System> systems = new ArrayList<>();
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

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        if (!prefs.getBoolean("createdKey", false)) {
            try {
                KeyPairGenerator kpg = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore");

                KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder("signer", KeyProperties.PURPOSE_SIGN)
                        .setDigests(KeyProperties.DIGEST_SHA256)
                        .setAlgorithmParameterSpec(new ECGenParameterSpec("P-384"))
                        .build();

                kpg.initialize(spec);
                kpg.generateKeyPair();

                prefs.edit().putBoolean("createdKey", true).apply();

                Log.d("key","Created EC key");
            } catch (Exception e) {
                Log.d("key", "Failed to create EC key: " + e.getMessage());
            }
        } else {
            Log.d("key", "EC key previously created.");
        }

        ServiceFinder serviceFinder = new ServiceFinder("_bast_controller._tcp", this, this::serviceFound);
    }

    private void serviceFound(NsdServiceInfo service) {
        try {
            InetAddress addr = service.getHost();
            int port = service.getPort();
            URL url = new URL("https", addr.getHostAddress(), port, "isOrphan");

            Log.d("networkDiscovery", "GETing " + url.toString());

            Request request = new Request.Builder().url(url).get().build();
            HTTP.doRequest(request, (res) -> {
                boolean isOrphan = res.code() == 200;
                systems.add(new System(service.getServiceName(), service.getHost(), service.getPort(), isOrphan));
                adapter.notifyItemInserted(systems.size() - 1);
            });
        } catch (MalformedURLException e) {
            Log.d("networkDiscovery", e.toString());
        }
    }
}
