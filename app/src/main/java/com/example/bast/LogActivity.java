package com.example.bast;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.list_adapters.LogAdapter;
import com.example.bast.list_adapters.RolesAdapter;
import com.example.bast.objects.Log;
import com.example.bast.objects.Role;
import com.example.bast.objects.Session;

import java.util.ArrayList;
import java.util.Calendar;

public class LogActivity extends AppCompatActivity {
    private final ArrayList<Log> log = new ArrayList<>();
    private final LogAdapter adapter = new LogAdapter(this, log);
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
        Log one = new Log("Front Door", Calendar.getInstance().getTime());
        Log two = new Log("Bathroom", Calendar.getInstance().getTime());

        log.add(one);
        log.add(two);

    }
}
