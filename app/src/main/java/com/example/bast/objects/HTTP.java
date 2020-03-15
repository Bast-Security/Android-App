package com.example.bast.objects;

import android.os.Handler;
import android.util.Log;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.function.Consumer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HTTP {
    public static OkHttpClient client() throws Exception {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] cArrr = new X509Certificate[0];
                return cArrr;
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
            }
        }};

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        clientBuilder.sslSocketFactory(sslContext.getSocketFactory());

        HostnameVerifier hostnameVerifier = (hostname, session) -> {
                Log.d("okttp","host verification: "  + hostname);
                return true;
        };

        clientBuilder.hostnameVerifier(hostnameVerifier);

        return clientBuilder.build();
    }

    public static void doRequest(Request request, Consumer<Response> onResponse) throws Exception {
        Handler handler = new Handler();

        OkHttpClient client = HTTP.client();

        Runnable r = () -> {
            try {
                Response res = client.newCall(request).execute();
                handler.post(() -> onResponse.accept(res));
            } catch (Exception e) {
                Log.d("okhttp", e.toString());
            }
        };

        Thread t = new Thread(r);
        t.start();
    }
}
