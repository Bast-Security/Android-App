package com.example.bast;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bast.list_adapters.UserRoleCheckList;
import com.example.bast.objects.Role;
import com.example.bast.objects.Session;

import java.util.ArrayList;
import java.util.List;

public class ChangeUserRolesActivity extends AppCompatActivity {

    private Session session;
    private ListView lv;
    private UserRoleCheckList adapter;
    private List<Role> roles = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_roles);
        //getRoles();
        roles.add(0, new Role("President"));
        roles.add(new Role("Janitor"));

        lv = findViewById(R.id.checkbox_list);
        adapter = new UserRoleCheckList(roles, this);
        lv.setAdapter(adapter);

    }

    private void getRoles() {
        // TODO: fill in the roles from the database
    }
}
