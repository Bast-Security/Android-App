package com.example.bast;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.list_adapters.RolesAdapter;
import com.example.bast.objects.Role;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class RoleListActivity extends AppCompatActivity {

    public ArrayList<Role> roles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_list);
        TextView title = (TextView) findViewById(R.id.activity_title);
        title.setText("ROLES");

        /*
        * adding data using JSON object
        *
        * When exchanging data between a browser and a server, the data can only be text.
        * JSON is text, and we can convert any JavaScript object into JSON,
        * and send JSON to the server.
        * We can also convert any JSON received from the server into JavaScript objects.
        * */

        //dummy data
        String jsonString = "[{\"Name\":\"Admin\"},{\"Name\":\"Developer\"},{\"Name\":\"Guest\"}," +
                "{\"Name\":\"IT\"},{\"Name\":\"Researcher\"},{\"Name\":\"Staff\"}]";

        JSONArray jsonObject = null;
        try {
            jsonObject = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < jsonObject.length(); i++){
            try {
                String name = (String) jsonObject.getJSONObject(i).get("Name");
                roles.add(new Role(name));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // shows the recycler view of items
        rolesRecyclerView();
    }

    // intializes the view of roles
    private void rolesRecyclerView() {
        RecyclerView rv = findViewById(R.id.recycler_view);
        RolesAdapter adapter = new RolesAdapter(this, roles);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
