package com.example.bast;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class EditLockActivity extends AppCompatActivity {

    Spinner dropdown;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lock);

        // TODO getExtra for lock list and lock position to edit

        initSpinner();
    }

    public void initSpinner() {
        dropdown = findViewById(R.id.dropdown);

        List<String> categories = new ArrayList<>();
        categories.add(0, "Choose Mode");
        categories.add("Pin Only");
        categories.add("Card Only");
        categories.add("Pin and Card");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_selectable_list_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(dataAdapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(parent.getItemAtPosition(position).equals("Choose Mode")) { }
                else {
                    String item = parent.getItemAtPosition(position).toString();

                    Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();

                    // TODO: edit changes made into database
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
