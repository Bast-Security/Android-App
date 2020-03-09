package com.example.bast;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.list_adapters.LocksAdapter;
import com.example.bast.objects.Lock;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class LockListActivity extends AppCompatActivity {

    public ArrayList<Lock> locks = new ArrayList<>();
    Dialog addDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_list);
        TextView title = (TextView) findViewById(R.id.activity_title);
        title.setText("LOCKS");

        /*
         * adding data using JSON object
         * */

        //dummy data
        String jsonString = "[{\"Name\":\"Front Door\"},{\"Name\":\"Back Door\"}," +
                "{\"Name\":\"Master Bedroom\"},{\"Name\":\"Guest Room\"}]";

        JSONArray jsonObject = null;
        try {
            jsonObject = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < jsonObject.length(); i++){
            try {
                String name = (String) jsonObject.getJSONObject(i).get("Name");
                locks.add(new Lock(name));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        locksRecyclerView();
        initButton();

    }

    private void locksRecyclerView() {
        RecyclerView rv = findViewById(R.id.recycler_view);
        LocksAdapter adapter = new LocksAdapter(locks, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initButton() {
        Button add_button = (Button) findViewById(R.id.add_btn);
        addDialog = new Dialog(this);

        // Popup add user menu
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLock();
            }
        });
    }

    public void addLock() {
        addDialog.setContentView(R.layout.add_lock);

        TextView title = (TextView) addDialog.findViewById(R.id.lock_tle);
        EditText name = (EditText) addDialog.findViewById(R.id.lockname);

        Button add_role = (Button) addDialog.findViewById(R.id.add_button);
        add_role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add to locks database
                addDialog.dismiss();
            }
        });

        addDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        addDialog.show();

    }
}
