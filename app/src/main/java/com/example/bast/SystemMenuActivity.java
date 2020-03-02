package com.example.bast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class SystemMenuActivity extends AppCompatActivity implements View.OnClickListener{

    private CardView lock, user, role, settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_menu);

        menuNav();

    }

    /**
     * Menu Navigation for the Dashboard
     */
    private void menuNav() {
        // defining the card views
        lock = (CardView) findViewById(R.id.locks_button);
        user = (CardView) findViewById(R.id.users_button);
        role = (CardView) findViewById(R.id.roles_button);
        settings = (CardView) findViewById(R.id.settings_button);

        lock.setOnClickListener(this);
        user.setOnClickListener(this);
        role.setOnClickListener(this);
        lock.setOnClickListener(this);

    }

    /**
     * Actions for when navigating through the CardView Menu
     * @param v View
     */
    @Override
    public void onClick(View v) {
        Intent i;

        switch(v.getId()) {
            case R.id.locks_button:
                i = new Intent(this, LockListActivity.class);
                startActivity(i);
                break;

            case R.id.users_button:
                i = new Intent(this, UserListActivity.class);
                startActivity(i);
                break;

            case R.id.roles_button:
                i = new Intent(this, RoleListActivity.class);
                startActivity(i);
                break;
            case R.id.settings_button:
                i = new Intent(this, HomeScreenActivity.class);
                startActivity(i);
                break;

            default: break;



        }
    }
}