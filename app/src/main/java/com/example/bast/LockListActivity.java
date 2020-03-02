package com.example.bast;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.list_adapters.LocksAdapter;
import com.example.bast.objects.Lock;

import java.util.ArrayList;

public class LockListActivity extends AppCompatActivity {

    public ArrayList<Lock> locks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

    }

    private void locksRecyclerView() {
        RecyclerView rv = findViewById(R.id.new_item);
        LocksAdapter adapter = new LocksAdapter(locks, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
