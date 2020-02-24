package com.example.bast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import com.example.bast.list_adapters.RecyclerViewAdapter;
import com.example.bast.objects.System;

import org.spongycastle.jcajce.provider.asymmetric.ec.KeyPairGeneratorSpi;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.util.ArrayList;

public class HomeScreenActivity extends AppCompatActivity {


    // Initialize Discovery Listener and NSD Manager
    private NsdManager.DiscoveryListener discoveryListener;
    private NsdManager nsdManager;

    private static final String TAG = "MainActivity";

    private ArrayList<System> systems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_screen);
        Log.d(TAG, "onCreate: started");

        // adding dummy data
        //TODO: delete dummy data later
        systems.add(new System("System 1", true));
        systems.add(new System("System 2", false));
        systems.add(new System("System 3", true));

        // add the SpongyCastle provider so that we can generate keys
        Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());

        //Creating an instance of the key generator class
        new KeyGenParameterSpec.Builder("keyPair", KeyProperties.PURPOSE_ENCRYPT);
        KeyPairGenerator keyPair = new KeyPairGeneratorSpi.ECDSA();
        KeyPair onCreateKeyPair = keyPair.genKeyPair();
        Key userPublicKey = onCreateKeyPair.getPublic();
        Log.d("keypair", "admin public key: " + userPublicKey);

        // Create an instance of NSD Manager and Discovery Listener
        nsdManager = (NsdManager) this.getSystemService(Context.NSD_SERVICE);
        discoveryListener = new NsdManager.DiscoveryListener() {

            // Failure to start discovery upon start
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.d("networkDiscovery", "On start discovery fail. Error code: " + errorCode);
                nsdManager.stopServiceDiscovery(this);
            }

            // Failure to discover service
            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.d("networkDiscovery", "On stop discovery fail. Error code: " + errorCode);
                nsdManager.stopServiceDiscovery(this);
            }

            // Called when service discovery begins
            @Override
            public void onDiscoveryStarted(String serviceType) {
                Log.d("networkDiscovery", "Discovery started");
            }

            // Discovery stopped
            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.d("networkDiscovery", "Discovery stopped. Service type: " + serviceType);
            }

            // When service found, do something
            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                Log.d("networkDiscovery", "Service discovery success");
                // When the service name is not null
                if (serviceInfo.getServiceName() != null) {
                    String mHost = serviceInfo.getServiceName();
                    if(serviceInfo.getServiceType() != null){
                        String serviceType = serviceInfo.getServiceType();
                        Log.d("networkDiscovery", "Service type: " + serviceType);
                    }

                    Log.d("networkDiscovery", "Service name: " + mHost);
                    systems.add(new System(mHost, false));

                }
                else{
                    Log.d("networkDiscovery", "null host ip address");
                }
            }

            // Network service lost/not available
            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {
                Log.d("networkDiscovery", "Service Lost. " + serviceInfo);
            }
        };

        // What type of service to look for
        nsdManager.discoverServices(
                "_bast._tcp", NsdManager.PROTOCOL_DNS_SD, discoveryListener);

        initRecyclerView();
    }

    /**
     * Initializes the Recycler View and its adapter
     */
    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView rv = findViewById(R.id.rvSystems);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(systems, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

    }

}
