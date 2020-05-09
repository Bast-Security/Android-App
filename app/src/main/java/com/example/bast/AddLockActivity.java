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

        new CountDownTimer(30000, 1000){
            public void onTick(long millisUntilFinished){
                final String min = String.valueOf(counter / 60);
                final String sec = String.format("%02d", counter % 60);
                final String time = min + ":" + sec;
                final TextView countdownTimer = findViewById(R.id.timer);
                countdownTimer.setText(time);

                final TextView code = findViewById(R.id.code);
                final Handler handler = new Handler();
                Async.task(() -> {
                    String TOTPGet = "systems/"+ systemId + "/totp";
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
                });

                if(counter != 0){counter--;}
                else{
                    counter = 300;
                }
            }

            @Override
            public void onFinish() { }
        }.start();

        setContentView(R.layout.activity_connect_lock);

    }
}
