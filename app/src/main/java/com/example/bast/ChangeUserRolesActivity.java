package com.example.bast;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bast.list_adapters.RoleCheckList;
import com.example.bast.objects.Async;
import com.example.bast.objects.HTTP;
import com.example.bast.objects.Role;
import com.example.bast.objects.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class ChangeUserRolesActivity extends AppCompatActivity {

    private Session session;
    private ListView lv;
    private RoleCheckList adapter;
    private List<Role> rolesList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_roles);

        // Intialize values for database
        Bundle bundle = getIntent().getExtras();
        String jwt = bundle.getString("jwt");
        session = new Session(jwt);

        getRoles(session);
        lv = findViewById(R.id.checkbox_list);
        adapter = new RoleCheckList(rolesList, this);
        lv.setAdapter(adapter);

    }

    public void getRoles(Session session) {
        //dummy data. todo: delete later
        rolesList.add(0, new Role("President"));
        rolesList.add(new Role("Janitor"));

        //populate rolesList with roles pulled from database
        final Handler handler = new Handler();
        final Bundle bundle = getIntent().getExtras();
        final int systemId = bundle.getInt("systemId");
        Async.task(() -> {
            String HTTPGet = "systems/" + systemId + "/roles";
            Log.d("role", HTTPGet);
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
