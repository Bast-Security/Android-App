package com.example.bast;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.spongycastle.jcajce.provider.asymmetric.ec.KeyPairGeneratorSpi;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;


public class HomeScreen extends AppCompatActivity{

    // Initialize Discovery Listener and NSD Manager
    private NsdManager.DiscoveryListener discoveryListener;
    private NsdManager nsdManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create an instance of NSD Manager and Discovery Listener
        nsdManager = (NsdManager) this.getSystemService(Context.NSD_SERVICE);
        discoveryListener = new NsdManager.DiscoveryListener() {

            // Failure to start discovery upon start
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.d("discovery", "On Start Discovery Fail. Error Code: " + errorCode);
                nsdManager.stopServiceDiscovery(this);
            }

            // Failure to discover service
            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.d("discovery", "On Stop Discovery Fail. Error Code: " + errorCode);
                nsdManager.stopServiceDiscovery(this);
            }

            // Called when service discovery begins
            @Override
            public void onDiscoveryStarted(String serviceType) {
                Log.d("discovery", "Discovery Started");
            }

            // Discovery stopped
            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.d("discovery", "Discovery Stopped. Service Type: " + serviceType);
            }

            // When service found, do something
            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                Log.d("discovery", "Service Discovery Success");
                // When the service name is not null
                if (serviceInfo.getServiceName() != null) {
                    String mHost = serviceInfo.getServiceName();
                    Log.d("hostName", "hostName: " + mHost);
                }
                else{
                    Log.d("hostName", "null host name");
                }
            }

            // Network service lost/not available
            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {
                Log.d("discovery", "On Service Lost. " + serviceInfo);
            }
        };

        // What type of service to look for
        nsdManager.discoverServices(
                "_bast._tcp", NsdManager.PROTOCOL_DNS_SD, discoveryListener);

        // add the SpongyCastle provider so that we can generate keys
        Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());

        //Creating an instance of the key generator class
        new KeyGenParameterSpec.Builder("keyPair", KeyProperties.PURPOSE_ENCRYPT);
        KeyPairGenerator keyPair = new KeyPairGeneratorSpi.ECDSA();
        KeyPair onCreateKeyPair = keyPair.genKeyPair();
        Log.d("keyPair", "keypair" + onCreateKeyPair);
    }

    public void joinSystem(View view){
        Log.d("clicked", "userClick");
        //ip address of fabio's computer
        String ipAdress = "http://192.168.1.99:8080/addUser";
        //gives the object
        final TextView textView = (TextView) findViewById(R.id.username);

        final String userName = textView.getText().toString();
        Log.d("userName",userName);

        //sending data over
        String payload = "{\"name\": \"" + userName + "\"}";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST,ipAdress,
                new Response.Listener<String>(){
            @Override
                    public void onResponse(String response){}
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){}
        });

        queue.add(request);
    }

}
