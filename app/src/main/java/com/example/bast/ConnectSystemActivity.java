package com.example.bast;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class ConnectSystemActivity extends AppCompatActivity {

    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connect_system);

        Button addButton = findViewById(R.id.button);
        addButton.setOnClickListener(listener);


    }
    private View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            AlertDialog alert = alertDialog.create();
            alert.show();
            alert.setMessage("confirmed");
            Log.d("activity", "add button clicked");
        }
    };

}
