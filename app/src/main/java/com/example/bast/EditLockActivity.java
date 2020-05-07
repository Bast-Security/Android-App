package com.example.bast;

import android.os.Bundle;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class EditLockActivity extends AppCompatActivity {

    Spinner dropdown;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lock);

    }

    public void initSpinner() {
        dropdown = findViewById(R.id.dropdown);

        List<String> categories = new ArrayList<>();
        categories.add(0, "Choose Mode");
        categories.add("Pin Only");
        categories.add("Card Only");
        categories.add("Pin and Card");
    }
}
