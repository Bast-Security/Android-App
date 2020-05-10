package com.example.bast;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Response;

public class AddLockActivity extends AppCompatActivity {
    int systemId;
    Session session;
    String codeValue;
    public int counter = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // pulling values from previous activity
        Bundle bundle = getIntent().getExtras();
        systemId = bundle.getInt("systemId");
        String systemName = bundle.getString("systemName");
        String jwt = bundle.getString("jwt");
        session = new Session(bundle.getString("jwt"));

        setContentView(R.layout.activity_connect_lock);

        final TextView code = findViewById(R.id.code);
        final TextView countdownTimer = findViewById(R.id.timer);

        final Handler handler = new Handler();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                long lastOffset = Long.MAX_VALUE;

                while (true) {
                    final long time = System.currentTimeMillis();
                    final long duration = 30 * 1000;
                    final long offset = time % duration;
                    final long remainingMillis = duration - offset;
                    final int remainingSecs = (int) (remainingMillis / 1000);
                    final String label = String.format("%d seconds", remainingSecs);

                    Log.d("lock", "offset " + offset);

                    handler.post(() -> {
                        countdownTimer.setText(label);
                    });

                    if (offset < lastOffset) {
                        String TOTPGet = "systems/" + systemId + "/totp";
                        Log.d("lock", "Getting TOTP code from path " + TOTPGet);
                        try (final Response response = session.request(HTTP.get(TOTPGet))) {
                            if (response.isSuccessful()) {
                                final String responseBody = response.body().string();
                                Log.d("lock", responseBody);

                                final JSONObject TOTP = new JSONObject(responseBody);
                                final String codeValue = TOTP.getString("code");
                                handler.post(() -> {
                                    code.setText(codeValue);
                                });
                            } else {
                                Log.d("lock", "Bad response from server");
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    lastOffset = offset;
                }
            }
        }, 0, 500);

        Async.task(() -> {

        });



    }
}
