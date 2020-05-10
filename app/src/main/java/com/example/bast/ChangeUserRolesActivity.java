package com.example.bast;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bast.list_adapters.RoleCheckList;
import com.example.bast.objects.Role;
import com.example.bast.objects.Session;

import java.util.ArrayList;
import java.util.List;

public class ChangeUserRolesActivity extends AppCompatActivity {

    private Session session;
    private ListView lv;
    private RoleCheckList adapter;
    private List<Role> roles = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_roles);
        getRoles();

        lv = findViewById(R.id.checkbox_list);
        adapter = new RoleCheckList(roles, this);
        lv.setAdapter(adapter);

    }

    private void getRoles() {
        // TODO: fill in the roles from the database into a String Array
    }
}
