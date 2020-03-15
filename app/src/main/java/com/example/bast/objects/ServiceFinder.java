package com.example.bast.objects;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Handler;
import android.util.Log;

import java.net.InetAddress;
import java.util.function.Consumer;

public class ServiceFinder {
    private NsdManager.DiscoveryListener discoveryListener;
    private NsdManager.ResolveListener resolveListener;
    private NsdManager nsdManager;
    Context context;
    Handler handler;

    public ServiceFinder(String serviceType, Context context, Consumer<NsdServiceInfo> onResolved) {
        this.context = context;
        this.handler = new Handler();

        this.nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);

        this.resolveListener = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.d("networkDiscovery", "Resolve failed with error: " + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                String serviceName = serviceInfo.getServiceName();
                InetAddress host = serviceInfo.getHost();
                Log.d("networkDiscovery", "Found host '" + serviceName + "' at " + host);
                handler.post(() -> onResolved.accept(serviceInfo));
            }
        };

        this.discoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.d("networkDiscovery", "On start discovery fail. Error code: " + errorCode);
                nsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.d("networkDiscovery", "On stop discovery fail. Error code: " + errorCode);
                nsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onDiscoveryStarted(String serviceType) {
                Log.d("networkDiscovery", "Discovery started");
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.d("networkDiscovery", "Discovery stopped. Service type: " + serviceType);
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                Log.d("networkDiscovery", "Service discovery success");
                nsdManager.resolveService(serviceInfo, resolveListener);
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {
                Log.d("networkDiscovery", "Service Lost. " + serviceInfo);
            }
        };

        nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
    }
}
