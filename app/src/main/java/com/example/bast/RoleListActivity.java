package com.example.bast;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.list_adapters.RolesAdapter;
import com.example.bast.objects.Role;

import java.util.ArrayList;

public class RoleListActivity extends AppCompatActivity {

    public ArrayList<Role> roles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_list);
        TextView title = (TextView) findViewById(R.id.activity_title);
        title.setText("ROLES");

        roles.add(new Role("Team Leader"));
        roles.add(new Role("Back-end Developer"));
        roles.add(new Role("Front-end Developer"));

        rolesRecyclerView();
    }

    private void rolesRecyclerView() {
        RecyclerView rv = findViewById(R.id.recycler_view);
        RolesAdapter adapter = new RolesAdapter(this, roles);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }


}
