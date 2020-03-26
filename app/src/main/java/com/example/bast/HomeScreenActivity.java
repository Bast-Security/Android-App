package com.example.bast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bast.list_adapters.SystemsAdapter;

import com.example.bast.objects.Async;
import com.example.bast.objects.HTTP;
import com.example.bast.objects.Session;
import com.example.bast.objects.System;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Response;

public class HomeScreenActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private final ArrayList<System> systemsList = new ArrayList<System>();
    private final SystemsAdapter adapter = new SystemsAdapter(systemsList, this);
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = getIntent().getExtras();
        final Session session = new Session(bundle.getString("jwt"));
        Log.d("session", "JWT: " + session.jwt);

        setContentView(R.layout.activity_general_list);
        final TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        activityTitle.setText("SYSTEMS");

        rv = findViewById(R.id.recycler_view);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        final Button addButton = (Button) findViewById(R.id.add_btn);
        final Dialog addDialog = new Dialog(this);

        addButton.setOnClickListener((view) -> {
            addDialog.setContentView(R.layout.connect_system);

            final TextView dialogTitle = (TextView) addDialog.findViewById(R.id.connect_title);
            final EditText nameEntry = (EditText) addDialog.findViewById(R.id.system_name);
            final Button systemAddButton = (Button) addDialog.findViewById(R.id.add_button);

            systemAddButton.setOnClickListener((v) -> {
                Log.d("system", "Adding System!");

                try {
                    final String systemName = nameEntry.getText().toString();

                    final JSONObject payload = new JSONObject()
                            .accumulate("name", systemName);

                    session.requestAync(HTTP.post("addSystem", payload), (response) -> {
                        if (response.code() != 200) {
                            Toast.makeText(this, "Failed to Add System", Toast.LENGTH_SHORT).show();
                        } else {
                            addDialog.dismiss();
                        }

                        response.close();
                        refreshSystems(session);
                    });
                } catch (Exception e) {
                    Log.d("system", e.toString());
                    Toast.makeText(this, "Failed to Add System", Toast.LENGTH_SHORT).show();
                }
            });

            addDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            addDialog.show();
        });

        refreshSystems(session);
    }

    public void refreshSystems(Session session) {
        Async.task(() -> {
            try (final Response response = session.request(HTTP.get("listSystems"))) {
                if (response.isSuccessful()) {
                    final String responseBody = response.body().string();
                    Log.d("system", responseBody);
                    systemsList.removeAll(systemsList);

                    final JSONArray systems = new JSONArray(responseBody);
                    for (int i = 0; i < systems.length(); i++) {
                        final JSONObject object = systems.getJSONObject(i);
                        final System system = new System(object.getInt("id"), object.getString("name"));
                        systemsList.add(system);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("system", "Bad response from server");
                }
            } catch (IOException e) {
                Log.d("system", "Failed to connect to host " + e.toString());
            } catch (JSONException e) {
                Log.d("system", "Failed to parse JSON string " + e.toString());
            }
        });
    }
}
