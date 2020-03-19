package com.example.bast.objects;

import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.function.Consumer;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Session {
    private String jwt;
    private Session(String jwt) {
        this.jwt = jwt;
    }

    public static Session doSyncLogin(final URL url, final PrivateKey privKey) throws IOException, AuthFailedException {
        Session session = null;

        try {
            final byte[] challenge;

            final MediaType contentType = MediaType.parse("application/json");
            final Request challengeReq = new Request.Builder().url(url).get().build();

            Log.d("session", "request 1");

            try (Response response = HTTP.doSyncRequest(challengeReq)) {
                Log.d("session", "a");
                final JSONObject c = new JSONObject(response.body().string());
                Log.d("session", "b");
                challenge = Base64.decode(c.getString("challenge"), Base64.DEFAULT);
                Log.d("session", "c");
            }

            Log.d("session", "signature");

            final Signature sig = Signature.getInstance("SHA256withECDSA");
            sig.initSign(privKey);
            sig.update(challenge);
            final byte[] signed = sig.sign();
            final JSONObject jsonSig = new JSONObject()
                    .accumulate("response", Base64.encodeToString(signed, Base64.DEFAULT));
            final RequestBody loginBody = RequestBody.create(contentType, jsonSig.toString());
            final Request loginRequest = new Request.Builder().url(url).post(loginBody).build();

            Log.d("session", "request 2");

            try (Response response = HTTP.doSyncRequest(loginRequest)) {
                Log.d("session", "did response!");
                if (response.code() == 200) {
                    session = new Session(response.body().string());
                }
            }
        } catch (IOException e) {
            Log.d("session", "I Want to die " + e.toString());
        } catch (JSONException e) {
            Log.d("session", "JSON exception: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.d("session", "NoSuchAlgorithm: " + e.getMessage());
        } catch (SignatureException e) {
            Log.d("session", "Signature: " + e.getMessage());
        } catch (InvalidKeyException e) {
            Log.d("session", "Invalid Key: " + e.getMessage());
        } catch (Exception e) {
            throw new AuthFailedException(e.toString());
        } finally {
            if (session == null) {
                throw new AuthFailedException();
            }
        }

        return session;
    }

    public static void doLogin(final URL url, final PrivateKey privateKey, final Consumer<Session> callback) {
        final Handler handler = new Handler();

        Runnable r = () -> {
            try {
                final Session s = doSyncLogin(url, privateKey);
                handler.post(() -> callback.accept(s));
            } catch (IOException e) {
                Log.d("session", e.getMessage());
            } catch (AuthFailedException e) {
                Log.d("session", e.getMessage());
            }
        };

        Thread t = new Thread(r);
        t.start();
    }

    /**
     * @param request An HTTP request to modify to include this Session's JWT.
     * @return Augmented Request
     */
    public Request wrap(Request request) {
        return request.newBuilder().addHeader("jwt", jwt).build();
    }

    public void doRequest(Request request, Consumer<Response> onResponse) {
        HTTP.doRequest(wrap(request), onResponse);
    }

    public Response doSyncRequest(Request request) throws IOException {
        return HTTP.doSyncRequest(wrap(request));
    }
}
