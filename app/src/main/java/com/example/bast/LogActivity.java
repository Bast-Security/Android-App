package com.example.bast;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.list_adapters.LogAdapter;
import com.example.bast.objects.Async;
import com.example.bast.objects.HTTP;
import com.example.bast.objects.Lock;
import com.example.bast.objects.LogHistory;
import com.example.bast.objects.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Response;

public class LogActivity extends AppCompatActivity {
    private final ArrayList<LogHistory> logHistory = new ArrayList<>();
    private final LogAdapter adapter = new LogAdapter(this, logHistory);
    private RecyclerView rv;
    private Session session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Intialize values for database
        final Bundle bundle = getIntent().getExtras();
        final String jwt = bundle.getString("jwt");
        final String systemName = bundle.getString("systemName");
        final int systemId = bundle.getInt("systemId");
        session = new Session(jwt);
        android.util.Log.d("role",  jwt);

        setContentView(R.layout.activity_log_history);

        rv = (RecyclerView)findViewById(R.id.log_history);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // TODO delete dummy values
        LogHistory one = new LogHistory("Front Door", Calendar.getInstance().getTime().toString());
        LogHistory two = new LogHistory("Bathroom", Calendar.getInstance().getTime().toString());

        logHistory.add(one);
        logHistory.add(two);

    }

}
