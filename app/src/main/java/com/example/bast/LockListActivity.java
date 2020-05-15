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

        // Popup add user menu
        add_button.setOnClickListener((view) -> {
            Intent intent = new Intent(LockListActivity.this, AddLockActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        refresh(session, systemId);
    }

    public void refresh(final Session session, final int systemId) {
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
                            final Lock lock = new Lock(object.getString("name"));
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

    /**
     * Kept the old popup add code just in case but not using it for now
     * @param session
     * @param systemId
     */
    public void addPopUp(Session session, final int systemId) {
        addDialog.setContentView(R.layout.add_lock);

        final TextView lockTitle = addDialog.findViewById(R.id.lock_title);
        final EditText name = addDialog.findViewById(R.id.lockname);

        Button add_lock = addDialog.findViewById(R.id.add_button);
        add_lock.setOnClickListener((btn) -> {
            final String lockName = name.getText().toString();

            final Handler handler = new Handler();

            Async.task(() -> {
                try {
                    final JSONObject payload = new JSONObject().accumulate("name", lockName);
                    final String file = String.format("systems/%d/locks", systemId);
                    try (final Response response = session.request(HTTP.post(file, payload))) {
                        if (!response.isSuccessful()) {
                            throw new Exception("Request failed with status " + response.code());
                        }

                        addDialog.dismiss();
                    }
                } catch (JSONException e) {
                    Log.d("locks", "JSONException " + e.toString());
                } catch (IOException e) {
                    Log.d("locks", "IOException " + e.toString());
                } catch (Exception e) {
                    Log.d("locks", e.toString());
                }
            });
        });

        addDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        addDialog.show();
    }

    // Old
    public void editLockName(Lock current) {
        addDialog.setContentView(R.layout.add_lock);

        TextView title = addDialog.findViewById(R.id.lock_title);
        title.setText("EDIT LOCK NAME");
        EditText name = addDialog.findViewById(R.id.lockname);

        Button edit_role = addDialog.findViewById(R.id.add_button);
        edit_role.setText("EDIT");
        edit_role.setOnClickListener(v -> {
            addDialog.dismiss();
            Intent intent = new Intent(LockListActivity.this, EditLockActivity.class);
            startActivity(intent);
        });

        addDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        addDialog.show();
    }

    public void displayLock(Lock current, int position) {
        displayDialog = new Dialog(this);
        displayDialog.setContentView(R.layout.popup_display_lock);

        TextView lockName = displayDialog.findViewById(R.id.lock);
        lockName.setText(current.getLockName());

        TextView textView_mode = displayDialog.findViewById(R.id.textView_mode);
        TextView mode = displayDialog.findViewById(R.id.textView_modeType);
        if(current.getMode() != null) {
            mode.setText(current.getMode());
        }
        else{
            mode.setText("<mode not set>");
        }
        Button edit_lock = displayDialog.findViewById(R.id.edit_button);
        edit_lock.setOnClickListener(v -> {
            displayDialog.dismiss();
            Intent intent = new Intent(LockListActivity.this, EditLockActivity.class);
            intent.putExtra("systemName", systemName);
            intent.putExtra("systemId",systemId);
            intent.putExtra("jwt", jwt);
            intent.putExtra("lockName", current.getLockName());
            intent.putExtra("mode", current.getMode());
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

    @Override
    public void OnLockClick(int position) {
        Lock clicked = locks.get(position);
        displayLock(clicked, position);
    }
}