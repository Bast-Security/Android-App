package com.example.bast;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bast.list_adapters.LockCheckList;
import com.example.bast.list_adapters.UserRoleCheckList;
import com.example.bast.objects.Lock;
import com.example.bast.objects.Role;
import com.example.bast.objects.Session;

import java.util.ArrayList;
import java.util.List;

public class EditRoleActivity extends AppCompatActivity {

    private ListView lv;
    private LockCheckList adapter;
    private List<Lock> locks = new ArrayList<>();
    private Session session;
    private String jwt;
    private String systemName;
    private int systemId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_role);

        final Bundle bundle = getIntent().getExtras();
        jwt = bundle.getString("jwt");
        systemName = bundle.getString("systemName");
        systemId = bundle.getInt("systemId");
        final String roleName = bundle.getString("roleName");
        Role role = new Role(roleName);
        session = new Session(jwt);

        lv = findViewById(R.id.checkbox_list);
        adapter = new LockCheckList(locks, this);
        lv.setAdapter(adapter);

    }

    private void getLocks() {
        // TODO: fill in the locks from the database into a String Array
    }
}
