package com.example.bast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
import java.util.ArrayList;


public class HomeScreen extends AppCompatActivity {

    // Initialize Discovery Listener and NSD Manager
    private NsdManager.DiscoveryListener discoveryListener;
    private NsdManager nsdManager;
    public static ArrayList<String> services = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);


        RecyclerView rvServices = (RecyclerView) findViewById(R.id.rvSystems);
        serviceListAdapter adapter = new serviceListAdapter(services);
        //adding in dummy data to our service list
        //TODO: remove this later when we get the correct ip address
        services.add("dummy data 1");
        services.add("dummy data 2");
        services.add("dummy data 3");


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
                        services.add("dummy data" + serviceType);
                    }

                    Log.d("networkDiscovery", "Service name: " + mHost);

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

        // add the SpongyCastle provider so that we can generate keys
        Security.addProvider(new org.spongycastle.jce.provider.BouncyCastleProvider());

        //Creating an instance of the key generator class
        new KeyGenParameterSpec.Builder("keyPair", KeyProperties.PURPOSE_ENCRYPT);
        KeyPairGenerator keyPair = new KeyPairGeneratorSpi.ECDSA();
        KeyPair onCreateKeyPair = keyPair.genKeyPair();
        Log.d("keypair", "keypair" + onCreateKeyPair);
    }

    public void joinSystem(View view){
        Log.d("userAction", "Join system clicked");

        //TODO: find out what I need to set host address to
        //ip address of fabio's computer
        String ipAddress = "http://192.168.1.99:8080/addUser";
        //gives the object
        final TextView textView = findViewById(R.id.username);

        final String userName = textView.getText().toString();
        Log.d("userAction","Username: " + userName);

        //sending data over
        String payload = "{\"name\": \"" + userName + "\"}";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST,ipAddress,
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
