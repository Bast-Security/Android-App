package com.example.bast;

import android.os.Bundle;

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
        setContentView(R.layout.activity_users);

        roles.add(new Role("Team Leader"));
        roles.add(new Role("Back-end Developer"));
        roles.add(new Role("Front-end Developer"));

    }

    private void rolesRecyclerView() {
        RecyclerView rv = findViewById(R.id.new_item);
        RolesAdapter adapter = new RolesAdapter(this, roles);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }


}
