package com.example.bast;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.list_adapters.LocksAdapter;
import com.example.bast.list_adapters.RecyclerViewAdapter;
import com.example.bast.list_adapters.RolesAdapter;
import com.example.bast.list_adapters.UsersAdapter;
import com.example.bast.objects.Lock;
import com.example.bast.objects.Role;
import com.example.bast.objects.System;
import com.example.bast.objects.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SystemMenuActivity extends AppCompatActivity {

    public ArrayList<Lock> locks = new ArrayList<>();
    public ArrayList<User> users = new ArrayList<>();
    public ArrayList<Role> roles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_menu);

        users.add(new User("Evan", "evan@email.com", "555-555-5555", 5555, 12345));
        users.add(new User("Fabio", "fabio@email.com", "555-555-5556", 5556, 123456));
        users.add(new User("Lety", "lety@email.com", "555-555-5557", 5557, 1234567));
        users.add(new User("Kristen", "kristen@email.com", "555-555-5558", 5558, 1234568));
        roles.add(new Role("Team Leader"));
        roles.add(new Role("Back-end Developer"));
        roles.add(new Role("Front-end Developer"));

        LinearLayout locks = (LinearLayout)findViewById(R.id.locks);
        LinearLayout users = (LinearLayout)findViewById(R.id.users);
        LinearLayout roles = (LinearLayout)findViewById(R.id.roles);
        LinearLayout settings = (LinearLayout)findViewById(R.id.settings);

        locks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.list_row);
            }
        });

        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.list_row);
            }
        });

        roles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.list_row);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.popup_display_info);
            }
        });
    }

    //TODO: these calls still have some bugs so need to fix these before they can be called
    private void locksRecyclerView() {
        RecyclerView rv = findViewById(R.id.new_item);
        LocksAdapter adapter = new LocksAdapter(this, locks);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void usersRecyclerView() {
        RecyclerView rv = findViewById(R.id.new_item);
        UsersAdapter adapter = new UsersAdapter(this, users);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void rolesRecyclerView() {
        RecyclerView rv = findViewById(R.id.new_item);
        RolesAdapter adapter = new RolesAdapter(this, roles);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
