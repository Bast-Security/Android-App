package com.example.bast;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bast.list_adapters.LockCheckList;
import com.example.bast.objects.Async;
import com.example.bast.objects.HTTP;
import com.example.bast.objects.Lock;
import com.example.bast.objects.Role;
import com.example.bast.objects.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class EditRoleActivity extends AppCompatActivity {
    private Session session;
    private ListView lv;
    private LockCheckList adapter;
    private List<Lock> locksList = new ArrayList<>();
    private List<String> listLocks = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_role);

        final Bundle bundle = getIntent().getExtras();
        int systemId = bundle.getInt("systemId");
        String systemName = bundle.getString("systemName");
        int userId = bundle.getInt("userId");
        String jwt = bundle.getString("jwt");
        String roleName = bundle.getString("roleName");
        Role role = new Role(roleName);
        session = new Session(jwt);
        listAllRoles(session);

        lv = findViewById(R.id.checkbox_list);
        Button button = findViewById(R.id.edit_button);
        TextView roleNameText = findViewById(R.id.textView_roleName);
        roleNameText.setText(roleName);
        adapter = new LockCheckList(locksList, this);
        lv.setAdapter(adapter);

        button.setOnClickListener(v -> {
            final Handler handler = new Handler();
            String rolename = roleNameText.getText().toString();

            try {
                final JSONArray doors = new JSONArray();

                for (final Lock lock : adapter.getCheckedLocks()) {
                    final JSONObject door = new JSONObject()
                            .accumulate("name", lock.getLockName())
                            .accumulate("id", lock.getLockId());

                    doors.put(door);
                }

                final JSONObject payload = new JSONObject()
                        .accumulate("name", rolename)
                        .accumulate("doors", doors);

                // HTTP request to post to the database
                String HTTPPost = "systems/" + systemId + "/roles/" + rolename;
                Log.d("lock", "Updating locks at path: " + HTTPPost);
                session.requestAsync(HTTP.put(HTTPPost, payload), (response) -> {
                    if (response.code() != 200) {
                    }
                    response.close();
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(this, SystemMenuActivity.class);
            intent.putExtra("jwt", jwt);
            intent.putExtra("systemName", systemName);
            intent.putExtra("systemId", systemId);
            startActivity(intent);
            Toast.makeText(this, "Role updated!",
                    Toast.LENGTH_SHORT).show();
        });

    }

    private void listAllRoles(Session session) {
        final Handler handler = new Handler();
        final Bundle bundle = getIntent().getExtras();
        final int systemId = bundle.getInt("systemId");
        Async.task(() -> {
            String HTTPGet = "systems/" + systemId + "/locks";
            try (final Response response = session.request(HTTP.get(HTTPGet))) {
                if (response.isSuccessful()) {
                    final String responseBody = response.body().string();
                    Log.d("role", responseBody);
                    locksList.removeAll(locksList);

                    final JSONArray locks = new JSONArray(responseBody);
                    for (int i = 0; i < locks.length(); i++) {
                        final JSONObject object = locks.getJSONObject(i);
                        final Lock lock = new Lock(object.getInt("id")
                                , object.getString("name"));
                        locksList.add(lock);
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
}
