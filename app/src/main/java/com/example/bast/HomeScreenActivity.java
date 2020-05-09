package com.example.bast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bast.list_adapters.SystemsAdapter;

import com.example.bast.objects.Async;
import com.example.bast.objects.HTTP;
import com.example.bast.objects.Session;
import com.example.bast.objects.System;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import okhttp3.Response;

public class HomeScreenActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private final ArrayList<System> systemsList = new ArrayList<System>();
    private final SystemsAdapter adapter = new SystemsAdapter(systemsList, this);
    private RecyclerView rv;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = getIntent().getExtras();
        session = new Session(bundle.getString("jwt"));

        setContentView(R.layout.activity_general_list);
        final TextView activityTitle = findViewById(R.id.activity_title);
        activityTitle.setText("SYSTEMS");

        rv = findViewById(R.id.recycler_view);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        final Button addButton = findViewById(R.id.add_btn);
        final Dialog addDialog = new Dialog(this);

        // Initialize swipe to delete users
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv);

        addButton.setOnClickListener((view) -> {
            addDialog.setContentView(R.layout.connect_system);

            final TextView dialogTitle = addDialog.findViewById(R.id.connect_title);
            final EditText nameEntry = addDialog.findViewById(R.id.system_name);
            final Button systemAddButton = addDialog.findViewById(R.id.add_button);

            systemAddButton.setOnClickListener((v) -> {
                Log.d("system", "Adding System!");

                try {
                    final String systemName = nameEntry.getText().toString();

                    final JSONObject payload = new JSONObject()
                            .accumulate("name", systemName);

                    session.requestAsync(HTTP.post("systems", payload), (response) -> {
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

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // Initialize variables for deleting a system
            int pos = viewHolder.getAdapterPosition();
            System deletedSystem = systemsList.remove(pos);
            int systemId = deletedSystem.getSystemID();
            adapter.notifyItemRemoved(pos);

            Async.task(() -> {
                try {
                    // HTTP delete requests
                    final String file = String.format("systems/"+ systemId);
                    Log.d("system", file);
                    try (final Response response = session.request(HTTP.delete(file))) {
                        if (!response.isSuccessful()) {
                            throw new Exception("Request failed with status " + response.code());
                        }
                    }
                } catch (JSONException e) {
                    Log.d("system", "JSONException " + e.toString());
                } catch (IOException e) {
                    Log.d("system", "IOException " + e.toString());
                } catch (Exception e) {
                    Log.d("system", e.toString());
                }
            });

            // Display a snackbar popup to undo a delete
            Snackbar.make(rv, deletedSystem.getSystemName(), Snackbar.LENGTH_LONG)
                    .setAction("Undo", v -> {
                        systemsList.add(pos, deletedSystem);
                        adapter.notifyItemInserted(pos);
                    }).show();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, rv, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(HomeScreenActivity.this, R.color.delete))
                    .addActionIcon(R.drawable.ic_delete_black_24dp)
                    .create()
                    .decorate();

            super.onChildDraw(c, rv, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public void refreshSystems(Session session) {
        final Handler handler = new Handler();
        Async.task(() -> {
            try (final Response response = session.request(HTTP.get("systems"))) {
                if (response.isSuccessful()) {
                    final String responseBody = response.body().string();
                    Log.d("system", responseBody);
                    systemsList.removeAll(systemsList);
                    Response TotpKey = session.request(HTTP.get("systems"));

                    final JSONArray systems = new JSONArray(responseBody);
                    for (int i = 0; i < systems.length(); i++) {
                        final JSONObject object = systems.getJSONObject(i);
                        final System system = new System(object.getInt("id"),
                                object.getString("name"));
                        systemsList.add(system);
                    }

                    handler.post(() -> adapter.notifyDataSetChanged());
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

    public Session getSession() {
        return session;
    }
}
