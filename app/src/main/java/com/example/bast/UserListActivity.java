package com.example.bast;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.list_adapters.UsersAdapter;
import com.example.bast.objects.User;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {

    ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_list);

        //TODO: Delete dummy data when connected
        users.add(new User("Bast", "bast@gmail.com", "1234567890", 2789, 123456783));
        users.add(new User("Fabio", "bast@gmail.com", "1234567890", 2789, 123456783));
        users.add(new User("Lety", "bast@gmail.com", null, 2789, 123456783));
        users.add(new User("Evan", "bast@gmail.com", null, 2789, 123456783));
        users.add(new User("Kristen", "bast@gmail.com", "1234567890", 2789, 123456783));

        initRecyclerView();
        initButton();

    }

    private void initButton() {
        Button add_button = (Button) findViewById(R.id.add_btn);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(this, AddUserActivity.class);
            }
        });
    }

    /**
     * Initializes the Recycler View and its adapter
     */
    private void initRecyclerView() {
        RecyclerView rv = findViewById(R.id.recycler_view);
        UsersAdapter adapter = new UsersAdapter(users, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

    }

}
