package com.example.bast.objects;

import android.os.Handler;
import android.os.NetworkOnMainThreadException;
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

import static com.example.bast.objects.HTTP.get;

public class Session {
    private String jwt;
    private Session(String jwt) {
        this.jwt = jwt;
    }

    public static Session doSyncLogin(final PrivateKey privKey) throws IOException, AuthFailedException {
        Session session = null;

        try {
            final byte[] challenge;

            try (Response response = HTTP.doSyncRequest(HTTP.get("login"))) {
                final JSONObject c = new JSONObject(response.body().string());
                challenge = Base64.decode(c.getString("challenge"), Base64.DEFAULT);
            }

            final Signature sig = Signature.getInstance("SHA256withECDSA");
            sig.initSign(privKey);
            sig.update(challenge);
            final byte[] signed = sig.sign();
            final JSONObject jsonSig = new JSONObject()
                    .accumulate("response", Base64.encodeToString(signed, Base64.DEFAULT));

            try (Response response = HTTP.doSyncRequest(HTTP.post("login", jsonSig))) {
                Log.d("session", "did response!");
                if (response.code() == 200) {
                    session = new Session(response.body().string());
                }
            }
        } catch (JSONException e) {
            Log.d("session", "JSON exception: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.d("session", "NoSuchAlgorithm: " + e.getMessage());
        } catch (SignatureException e) {
            Log.d("session", "Signature: " + e.getMessage());
        } catch (InvalidKeyException e) {
            Log.d("session", "Invalid Key: " + e.getMessage());
        } catch (NetworkOnMainThreadException e) {
            throw new AuthFailedException(e.toString());
        } finally {
            if (session == null) {
                throw new AuthFailedException();
            }
        }

        return session;
    }

    public static void doLogin(final PrivateKey privateKey, final Consumer<Session> callback) {
        final Handler handler = new Handler();

        Async.task(() -> {
            try {
                final Session s = doSyncLogin(privateKey);
                handler.post(() -> callback.accept(s));
            } catch (IOException e) {
                Log.d("session", e.getMessage());
            } catch (AuthFailedException e) {
                Log.d("session", e.getMessage());
            }
        });
    }

    /**
     * @param request An HTTP request to modify to include this Session's JWT.
     * @return Augmented Request
     */
    public Request wrap(Request request) {
        return request.newBuilder()
                .addHeader("Authorization", "Bearer " + jwt)
                .build();
    }

    public void doRequest(Request request, Consumer<Response> onResponse) {
        HTTP.doRequest(wrap(request), onResponse);
    }

    public Response doSyncRequest(Request request) throws IOException {
        return HTTP.doSyncRequest(wrap(request));
    }
}
