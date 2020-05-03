package com.example.bast;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bast.objects.Async;
import com.example.bast.objects.HTTP;
import com.example.bast.objects.Role;
import com.example.bast.objects.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

public class AddLockActivity extends AppCompatActivity {
    int systemId;
    Session session;
    String codeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // pulling values from previous activity
        Bundle bundle = getIntent().getExtras();
        int systemId = bundle.getInt("systemId");
        String systemName = bundle.getString("systemName");
        String jwt = bundle.getString("jwt");

        final TextView code = findViewById(R.id.code);
        final TextView conuntdownTimer = findViewById(R.id.countdown_timer);

        setContentView(R.layout.activity_connect_lock);

    }

    public void everyFiveMins(Session session) {
        Async.task(() -> {
            String TOTPGet = "systems/" + systemId + "/totp";
            try (final Response response = session.request(HTTP.get(TOTPGet))) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}