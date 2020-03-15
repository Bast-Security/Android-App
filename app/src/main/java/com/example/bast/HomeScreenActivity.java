package com.example.bast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import com.example.bast.list_adapters.SystemsAdapter;
import com.example.bast.objects.ServiceFinder;
import com.example.bast.objects.System;

import org.spongycastle.jcajce.provider.asymmetric.ec.KeyPairGeneratorSpi;
import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.net.InetAddress;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.util.ArrayList;

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

        // add the SpongyCastle provider so that we can generate keys
        Security.addProvider(new BouncyCastleProvider());

        //Creating an instance of the key generator class
        new KeyGenParameterSpec.Builder("keyPair", KeyProperties.PURPOSE_ENCRYPT);
        KeyPairGenerator keyPair = new KeyPairGeneratorSpi.ECDSA();
        KeyPair onCreateKeyPair = keyPair.genKeyPair();
        publicKey = onCreateKeyPair.getPublic();
        privateKey = onCreateKeyPair.getPrivate();
        Log.d("keypair", "keypair created");

        // Create an instance of NSD Manager and Discovery Listener
        ServiceFinder serviceFinder = new ServiceFinder("_bast_controller._tcp", this, this::serviceFound);
    }

    private void serviceFound(NsdServiceInfo service) {
        systems.add(new System(service.getServiceName(), true));
        adapter.notifyItemInserted(systems.size() - 1);
    }
}
