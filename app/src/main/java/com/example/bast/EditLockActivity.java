package com.example.bast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bast.objects.HTTP;
import com.example.bast.objects.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditLockActivity extends AppCompatActivity {

    Spinner dropdown;
    private String systemName;
    private int systemId;
    private String jwt;
    private Session session;
    private String newLockName;
    private int choice = 2;
    private int lockId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lock);

        final Bundle bundle = getIntent().getExtras();
        jwt = bundle.getString("jwt");
        systemName = bundle.getString("systemName");
        systemId = bundle.getInt("systemId");
        String lockname = bundle.getString("lockName");
        int methodtype = bundle.getInt("method");
        lockId = bundle.getInt("id");
        session = new Session(jwt);

        TextView lockName = findViewById(R.id.textView_lockname);
        lockName.setText(lockname);
        TextView methodType = findViewById(R.id.textView_modeType);
        if(methodtype == 2){
            methodType.setText("Card Only");
        }else if(methodtype == 4){
            methodType.setText("Pin Only");
        }else if(methodtype == 6){
            methodType.setText("Pin or Card");
        }else{
            methodType.setText("Pin and Card");
        }
        Button confirm = findViewById(R.id.editLock_button);
        confirm.setOnClickListener(v -> {
            newLockName = lockName.getText().toString();
            pushChanges();

            Intent intent = new Intent(this, SystemMenuActivity.class);
            intent.putExtra("jwt", jwt);
            intent.putExtra("systemName", systemName);
            intent.putExtra("systemId", systemId);
            startActivity(intent);
            Toast.makeText(this, "Lock updated!",
                    Toast.LENGTH_SHORT).show();
        });
        initSpinner();
    }

    public void initSpinner() {
        dropdown = findViewById(R.id.dropdown);

        List<String> categories = new ArrayList<>();
        categories.add(0, "Choose Mode");
        categories.add("Pin Only");
        categories.add("Card Only");
        categories.add("Card or Pin");
        categories.add("Pin and Card");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_selectable_list_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(dataAdapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Choose Mode")) { }
                else if(parent.getItemAtPosition(position).equals("Pin Only")) {
                    choice = 4;
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
                }
                else if(parent.getItemAtPosition(position).equals("Card Only")) {
                    choice = 2;
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
                }
                else if(parent.getItemAtPosition(position).equals("Pin or Card")) {
                    choice = 6;
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
                }
                else if(parent.getItemAtPosition(position).equals("Pin and Card")) {
                    choice = 8;
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void pushChanges() {
        final Bundle bundle = getIntent().getExtras();
        final String lockname = newLockName;
        final int method =  choice;
        Log.d("locks", "activity "+ lockname + " "+ method);
        try {
            final JSONObject payload = new JSONObject()
                    .accumulate("method", method)
                    .accumulate("name", lockname);

            // HTTP request to post to the database
            String HTTPPut = "systems/" + systemId + "/locks/" + lockId;
            Log.d("lock", "Updating lock at path: " + HTTPPut);
            session.requestAsync(HTTP.put(HTTPPut, payload), (response) -> {
                if (response.code() != 200) {
                }
            });
            Intent backToUserList = new Intent(this,
                    UserListActivity.class);
            backToUserList.putExtras(bundle);
            startActivity(backToUserList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
