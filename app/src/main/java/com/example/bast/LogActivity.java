package com.example.bast;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.list_adapters.LogAdapter;
import com.example.bast.list_adapters.RolesAdapter;
import com.example.bast.objects.Log;
import com.example.bast.objects.Role;
import com.example.bast.objects.Session;

import java.util.ArrayList;

public class LogActivity extends AppCompatActivity {
    private final ArrayList<Log> log = new ArrayList<Log>();
    private final LogAdapter adapter = new LogAdapter(this, log);
    private ListView rv;
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

        setContentView(R.layout.activity_log_history);
    }
}
