package com.example.bast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class SystemMenuActivity extends AppCompatActivity implements View.OnClickListener{

    private String jwt;
    private String systemName;
    private int systemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_menu);

        final Bundle bundle = getIntent().getExtras();
        jwt = bundle.getString("jwt");
        systemName = bundle.getString("systemName");
        systemId = bundle.getInt("systemId");

        final TextView title = (TextView) findViewById(R.id.activity_title);
        title.setText(systemName);

        // defining the card views
        final CardView lock = (CardView) findViewById(R.id.locks_button);
        final CardView user = (CardView) findViewById(R.id.users_button);
        final CardView role = (CardView) findViewById(R.id.roles_button);
        final CardView settings = (CardView) findViewById(R.id.logs_button);

        lock.setOnClickListener(this);
        user.setOnClickListener(this);
        role.setOnClickListener(this);
        lock.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch(v.getId()) {
            case R.id.locks_button:
                intent = new Intent(this, LockListActivity.class);
                intent.putExtra("jwt", jwt);
                intent.putExtra("systemName", systemName);
                intent.putExtra("systemId", systemId);
                startActivity(intent);
                break;

            case R.id.users_button:
                intent = new Intent(this, UserListActivity.class);
                intent.putExtra("jwt", jwt);
                intent.putExtra("systemName", systemName);
                intent.putExtra("systemId", systemId);
                startActivity(intent);
                break;

            case R.id.roles_button:
                intent = new Intent(this, RoleListActivity.class);
                intent.putExtra("jwt", jwt);
                intent.putExtra("systemName", systemName);
                intent.putExtra("systemId", systemId);
                startActivity(intent);
                break;

            case R.id.logs_button:
                intent = new Intent(this, HomeScreenActivity.class);
                intent.putExtra("jwt", jwt);
                intent.putExtra("systemName", systemName);
                intent.putExtra("systemId", systemId);
                startActivity(intent);
                break;

            default: break;
        }
    }
}