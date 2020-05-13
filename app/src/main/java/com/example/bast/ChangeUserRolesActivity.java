package com.example.bast;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bast.list_adapters.UserRoleCheckList;
import com.example.bast.objects.Async;
import com.example.bast.objects.HTTP;
import com.example.bast.objects.Role;
import com.example.bast.objects.Session;
import com.example.bast.objects.User;

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
    private UserRoleCheckList adapter;
    private List<Role> rolesList = new ArrayList<>();
    private List<Role> checkedRoles = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_roles);

        final Bundle bundle = getIntent().getExtras();
        int userID = bundle.getInt("id");
        String username = bundle.getString("username");
        String email = bundle.getString("email");
        String card = bundle.getString("card");
        String pin = bundle.getString("pin");
        String phone = bundle.getString("phone");
        int systemId = bundle.getInt("systemId");
        String systemName = bundle.getString("systemName");
        int userId = bundle.getInt("userId");
        String jwt = bundle.getString("jwt");

        User userSelected = new User(userID, username, email, phone, pin, card);
        session = new Session(jwt);
        getUsersRoles(userSelected);
        listAllRoles(session);


        lv = findViewById(R.id.checkbox_list);
        Button button = findViewById(R.id.confirmRoles_button);
        adapter = new UserRoleCheckList(rolesList, this);
        lv.setAdapter(adapter);


        button.setOnClickListener((view) -> {
            Log.d("user", "Updating User");

            try {
                // turning the input fields into fields of a JSON object
                final JSONObject payload = new JSONObject()
                        .accumulate("name", username)
                        .accumulate("email", email)
                        .accumulate("phone", phone)
                        .accumulate("pin", pin)
                        .accumulate("cardno", card)
                        .accumulate("roles", checkedRoles);

                // HTTP request to post to the database
                String HTTPPut = "systems/" + systemId + "/users/" + userId;
                Log.d("user", "Updating user at path: " + HTTPPut);
                session.requestAsync(HTTP.put(HTTPPut, payload), (response) -> {
                    if (response.code() != 200) {
                    }
                });

                /*
                * Go back to the edit user screen after the updated roles have been pushed to the
                * controller
                * */
                Intent intent = new Intent(this, UserListActivity.class);
                intent.putExtra("jwt", jwt);
                intent.putExtra("systemName", systemName);
                intent.putExtra("systemId", systemId);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void listAllRoles(Session session) {
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

    private void getUsersRoles(User u){
        final Handler handler = new Handler();
        final Bundle bundle = getIntent().getExtras();
        final int systemId = bundle.getInt("systemId");
        Async.task(() -> {
            String HTTPGet = "systems/" + systemId + "/users";
            try (final Response response = session.request(HTTP.get(HTTPGet))) {
                if (response.isSuccessful()) {
                    final String responseBody = response.body().string();
                    Log.d("role", responseBody);
                    checkedRoles.removeAll(checkedRoles);

                    final JSONArray roles = new JSONArray(responseBody);
                    for (int i = 0; i < roles.length(); i++) {
                        final JSONObject object = roles.getJSONObject(i);
                        final Role role = new Role(object.getString("roles"));
                        checkedRoles.add(role);
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
