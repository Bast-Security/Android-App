package com.example.bast;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.example.bast.objects.Async;
import com.example.bast.objects.HTTP;
import com.example.bast.objects.Lock;
import com.example.bast.objects.Session;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import okhttp3.Response;

public class LockListActivity extends AppCompatActivity implements LocksAdapter.OnLockListener {

    public ArrayList<Lock> locks = new ArrayList<>();
    RecyclerView rv;
    LocksAdapter adapter;
    Dialog addDialog, displayDialog;
    private String systemName;
    private int systemId;
    private String jwt;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_list);
        TextView title = findViewById(R.id.activity_title);
        title.setText("LOCKS");

        final Bundle bundle = getIntent().getExtras();
        jwt = bundle.getString("jwt");
        systemName = bundle.getString("systemName");
        systemId = bundle.getInt("systemId");
        session = new Session(jwt);

        rv = findViewById(R.id.recycler_view);
        adapter = new LocksAdapter( locks,this, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv);

        final Button add_button = findViewById(R.id.add_btn);
        addDialog = new Dialog(this);

        refresh();
        // Popup add user menu
        add_button.setOnClickListener((view) -> {
            Intent intent = new Intent(LockListActivity.this, AddLockActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });

    }

    public void refresh() {
        final Handler handler = new Handler();
        Async.task(() -> {
            try {
                final String file = String.format("systems/%d/locks", systemId);
                try (final Response response = session.request(HTTP.get(file))) {
                    if (response.isSuccessful()) {
                        locks.clear();

                        final String body = response.body().string();
                        Log.d("locks", body);
                        
                        final JSONArray array = new JSONArray(body);
                        for (int i = 0; i < array.length(); i++) {
                            final JSONObject object = array.getJSONObject(i);
                            final Lock lock = new Lock(object.getInt("id")
                                    , object.getString("name"),
                                    object.getInt("method"));
                            locks.add(lock);
                        }
                    }

                    handler.post(() -> {
                        adapter.notifyDataSetChanged();
                    });
                }
            } catch (JSONException e) {
                Log.d("locks", "JSONException " + e.toString());
            } catch (IOException e) {
                Log.d("locks", "IOException " + e.toString());
            } catch (Exception e) {
                Log.d("locks", e.toString());
            }
        });
    }

    public void displayLock(Lock current, int position) {
        Log.d("lock", String.valueOf(current.getMethod()));
        displayDialog = new Dialog(this);
        displayDialog.setContentView(R.layout.popup_display_lock);

        TextView lockName = displayDialog.findViewById(R.id.lock);
        lockName.setText(current.getLockName());

        TextView textView_mode = displayDialog.findViewById(R.id.textView_mode);
        TextView mode = displayDialog.findViewById(R.id.textView_modeType);
        if(current.getMethod() == 2){
            mode.setText("Card Only");
        }else if(current.getMethod() == 4){
            mode.setText("Pin Only");
        }else if(current.getMethod() == 6){
            mode.setText("Pin or Card");
        }else{
            mode.setText("Pin and Card");
        }

        Button edit_lock = displayDialog.findViewById(R.id.edit_button);
        edit_lock.setOnClickListener(v -> {
            displayDialog.dismiss();
            Intent intent = new Intent(LockListActivity.this, EditLockActivity.class);
            intent.putExtra("systemName", systemName);
            intent.putExtra("systemId",systemId);
            intent.putExtra("jwt", jwt);
            intent.putExtra("lockName", current.getLockName());
            intent.putExtra("id", current.getLockId());
            intent.putExtra("method", current.getMethod());
            startActivity(intent);
        });

        displayDialog.show();
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // Initialize variables for deleting a user
            int pos = viewHolder.getAdapterPosition();
            Lock deletedLock = locks.remove(pos);
            int lockId = deletedLock.getLockId();
            String userID = Integer.toString(lockId);
            adapter.notifyItemRemoved(pos);

            // Initializing HTTP variables for delete requests
            final Bundle bundle = getIntent().getExtras();
            final int systemId = bundle.getInt("systemId");

            Async.task(() -> {
                try {
                    // HTTP delete requests
                    final String file = String.format("systems/"+ systemId + "/locks/" + lockId);
                    Log.d("lock", "Deleting lock at path: " + file);
                    try (final Response response = session.request(HTTP.delete(file))) {
                        if (!response.isSuccessful()) {
                            throw new Exception("Request failed with status " + response.code());
                        }
                    }
                } catch (JSONException e) {
                    Log.d("lock", "JSONException " + e.toString());
                } catch (IOException e) {
                    Log.d("lock", "IOException " + e.toString());
                } catch (Exception e) {
                    Log.d("lock", e.toString());
                }
            });

            // Display a snackbar popup to undo a delete
            Snackbar.make(rv, deletedLock.getLockName(), Snackbar.LENGTH_LONG)
                    .setAction("Undo", v -> {
                        locks.add(pos, deletedLock);
                        try {
                            // turning the input fields into fields of a JSON object
                            final JSONObject payload = new JSONObject()
                                    .accumulate("name", deletedLock.getLockName())
                                    .accumulate("method", deletedLock.getMethod());

                            // HTTP request to post to the database
                            String HTTPPost = "systems/" + systemId + "/locks/" + lockId;
                            session.requestAsync(HTTP.post(HTTPPost, payload), (response) -> {
                                if (response.code() != 200) {} else {}
                                response.close();
                                refresh();
                            });
                        } catch (Exception e) {
                            Log.d("lock", e.toString());
                        }
                        adapter.notifyItemInserted(pos);
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

    @Override
    public void OnLockClick(int position) {
        Lock clicked = locks.get(position);
        displayLock(clicked, position);
    }
}