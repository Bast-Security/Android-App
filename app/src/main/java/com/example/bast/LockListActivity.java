package com.example.bast;

import android.app.Dialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.list_adapters.LocksAdapter;
import com.example.bast.objects.Lock;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class LockListActivity extends AppCompatActivity {

    public ArrayList<Lock> locks = new ArrayList<>();
    RecyclerView rv;
    LocksAdapter adapter;
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
        rv = findViewById(R.id.recycler_view);
        adapter = new LocksAdapter(locks, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv);
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

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int pos = viewHolder.getAdapterPosition();
            Lock deletedLock = locks.remove(pos);
            adapter.notifyItemRemoved(pos);

            Snackbar.make(rv, deletedLock.getLockName(), Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            locks.add(pos, deletedLock);
                            adapter.notifyItemInserted(pos);
                        }
                    }).show();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, rv, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(LockListActivity.this, R.color.delete))
                    .addActionIcon(R.drawable.ic_delete_black_24dp)
                    .create()
                    .decorate();

            super.onChildDraw(c, rv, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

}
