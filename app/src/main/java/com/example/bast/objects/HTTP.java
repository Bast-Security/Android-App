package com.example.bast.objects;

import android.os.Handler;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.function.Consumer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HTTP {
    public static final String HOST = "10.0.2.2";
    public static final int PORT = 8080;

    public static Request post(String file, JSONObject body) {
        return new Request.Builder()
                .url(url(file))
                .post(jsonBody(body))
                .build();
    }

    public static Request delete(String file) {
        return new Request.Builder()
                .url(url(file))
                .delete()
                .build();
    }

    public static Request put(String file, JSONObject body) {
        return new Request.Builder()
                .url(url(file))
                .put(jsonBody(body))
                .build();
    }

    public static Request get(String file) {
        return new Request.Builder()
                .url(url(file))
                .get()
                .build();
    }

    public static String url(String file) {
        return "https://" + HOST + ":" + PORT + "/" + file;
    }

    public static RequestBody jsonBody(JSONObject payload) {
        final MediaType contentType = MediaType.parse("application/json");
        final RequestBody body = RequestBody.create(contentType, payload.toString());
        return body;
    }

    public static OkHttpClient client() {
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

        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            clientBuilder.sslSocketFactory(sslContext.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            Log.d("http", "could not set ssl context (No Such Algorithm) " + e);
        } catch (KeyManagementException e) {
            Log.d("http", "could not set ssl context (Key Management) " + e);
        }

        HostnameVerifier hostnameVerifier = (hostname, session) -> {
                Log.d("okttp","host verification: "  + hostname);
                return true;
        };

        clientBuilder.hostnameVerifier(hostnameVerifier);

        return clientBuilder.build();
    }

    public static void requestAsync(Request request, Consumer<Response> onResponse) {
        Handler handler = new Handler();

        Runnable r = () -> {
            try {
                Response response = request(request);
                handler.post(() -> onResponse.accept(response));
            } catch (Exception e) {
                Log.d("http", e.toString());
            }
        };

        Thread t = new Thread(r);
        t.start();
    }


    public static Response request(Request request) throws IOException {
        Response r = null;
        OkHttpClient client = HTTP.client();
        return client.newCall(request).execute();
    }
}
