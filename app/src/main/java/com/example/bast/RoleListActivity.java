package com.example.bast;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.list_adapters.RolesAdapter;
import com.example.bast.objects.Async;
import com.example.bast.objects.HTTP;
import com.example.bast.objects.Lock;
import com.example.bast.objects.Role;
import com.example.bast.objects.Session;
import com.example.bast.objects.System;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RoleListActivity extends AppCompatActivity {
    private static final String TAG = "RoleListActivity";

    private final ArrayList<Role> rolesList = new ArrayList<Role>();
    private final RolesAdapter adapter = new RolesAdapter(rolesList, this);
    private RecyclerView rv;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = getIntent().getExtras();
        final String jwt = bundle.getString("jwt");
        final String systemName = bundle.getString("systemName");
        final int systemId = bundle.getInt("systemId");
        final Session session = new Session(jwt);

        setContentView(R.layout.activity_general_list);
        final TextView activityTitle = (TextView) findViewById(R.id.activity_title);
        activityTitle.setText("ROLES");

        rv = findViewById(R.id.recycler_view);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        final Button addButton = (Button) findViewById(R.id.add_btn);
        final Dialog addDialog = new Dialog(this);

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv);

        addButton.setOnClickListener((view) -> {
            addDialog.setContentView(R.layout.add_role);

            final TextView dialogTitle = (TextView) addDialog.findViewById(R.id.role_title);
            final EditText nameEntry = (EditText) addDialog.findViewById(R.id.rolename);
            final Button roleAddButton = (Button) addDialog.findViewById(R.id.add_button);

            roleAddButton.setOnClickListener((v) -> {
                Log.d("role", "Adding Role!");

                try {
                    final String roleName = nameEntry.getText().toString();

                    final JSONObject payload = new JSONObject()
                            .accumulate("name", roleName);

                    String HTTPPost = "systems/" + systemId + "/roles";
                    session.requestAsync(HTTP.post(HTTPPost, payload), (response) -> {
                        if (response.code() != 200) {
                            Toast.makeText(this, "Failed to Add Role", Toast.LENGTH_SHORT).show();
                        } else {
                            addDialog.dismiss();
                        }

                        response.close();
                        refreshSystems(session);
                    });
                } catch (Exception e) {
                    Log.d("role", e.toString());
                    Toast.makeText(this, "Failed to Add Role", Toast.LENGTH_SHORT).show();
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
            int pos = viewHolder.getAdapterPosition();
            Role deletedRole = rolesList.remove(pos);

            adapter.notifyItemRemoved(pos);
            final Bundle bundle = getIntent().getExtras();
            final int systemId = bundle.getInt("systemId");

            try {

                final JSONObject payload = new JSONObject()
                        .accumulate("name", deletedRole);

                String HTTPDelete = "systems/" + systemId + "/roles";
                session.requestAsync(HTTP.delete(HTTPDelete, payload), (response) -> {
                    response.close();
                    refreshSystems(session);
                });
            } catch (Exception e) {
                Log.d("role", e.toString());
            }

            Snackbar.make(rv, deletedRole.getRoleName(), Snackbar.LENGTH_LONG)
                    .setAction("Undo", v -> {
                        rolesList.add(pos, deletedRole);
                        adapter.notifyItemInserted(pos);
                    }).show();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, rv, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(RoleListActivity.this, R.color.delete))
                    .addActionIcon(R.drawable.ic_delete_black_24dp)
                    .create()
                    .decorate();

            super.onChildDraw(c, rv, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public void refreshSystems(Session session) {
        final Handler handler = new Handler();
        final Bundle bundle = getIntent().getExtras();
        final int systemId = bundle.getInt("systemId");
        Async.task(() -> {
            String HTTPGet = "systems/" + systemId + "/roles";
            try (final Response response = session.request(HTTP.get(HTTPGet))) {
                if (response.isSuccessful()) {
                    final String responseBody = response.body().string();
                    Log.d("role", responseBody);
                    rolesList.removeAll(rolesList);

                    final JSONArray roles = new JSONArray(responseBody);
                    for (int i = 0; i < roles.length(); i++) {
                        final JSONObject object = roles.getJSONObject(i);
                        final Role role = new Role(object.getString("name"));
                        rolesList.add(role);
                    }

                    handler.post(() -> adapter.notifyDataSetChanged());
                } else {
                    Log.d("role", "Bad response from server");
                }
            } catch (IOException e) {
                Log.d("role", "Failed to connect to host " + e.toString());
            } catch (JSONException e) {
                Log.d("role", "Failed to parse JSON string " + e.toString());
            }
        });
    }

    public Session getSession() {
        return session;
    }
}
