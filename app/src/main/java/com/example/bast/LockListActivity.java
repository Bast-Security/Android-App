package com.example.bast;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.list_adapters.LocksAdapter;
import com.example.bast.objects.Lock;
import com.example.bast.objects.Role;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;

public class LockListActivity extends AppCompatActivity {

    public ArrayList<Lock> locks = new ArrayList<>();

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

    }

    private void locksRecyclerView() {
        RecyclerView rv = findViewById(R.id.recycler_view);
        LocksAdapter adapter = new LocksAdapter(locks, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
