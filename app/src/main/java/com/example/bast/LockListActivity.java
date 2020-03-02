package com.example.bast;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.list_adapters.LocksAdapter;
import com.example.bast.objects.Lock;

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

        locks.add(new Lock("First Lock"));

        locksRecyclerView();

    }

    private void locksRecyclerView() {
        RecyclerView rv = findViewById(R.id.recycler_view);
        LocksAdapter adapter = new LocksAdapter(locks, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
