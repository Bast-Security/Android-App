package com.example.bast;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.list_adapters.UsersAdapter;
import com.example.bast.objects.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {

    ArrayList<User> users = new ArrayList<>();
    Dialog addDialog, successDialog;

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
        addDialog = new Dialog(this);

        // Popup add user menu
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserMenu();
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

    public void addUserMenu() {
        addDialog.setContentView(R.layout.popup_add_menu);

        TextView title = (TextView) addDialog.findViewById(R.id.usertitle);
        EditText name = (EditText) addDialog.findViewById(R.id.username);
        EditText email = (EditText) addDialog.findViewById(R.id.email);
        EditText pin = (EditText) addDialog.findViewById(R.id.pin);
        EditText card = (EditText) addDialog.findViewById(R.id.cardnum);
        EditText phone = (EditText) addDialog.findViewById(R.id.phone);

        //final String uName, uEmail, uPhone;
        //final int uCard, uPin;

        Button add_user = (Button) addDialog.findViewById(R.id.add_user_button);
        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add to user database
                //users.add(new User(uName, uEmail, uPhone, uPin, uCard));
                addDialog.dismiss();
            }
        });

        FloatingActionButton exit = (FloatingActionButton) addDialog.findViewById(R.id.exit_button);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.dismiss();
            }
        });

        addDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        addDialog.show();
    }
}
