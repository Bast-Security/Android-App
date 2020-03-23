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

import com.example.bast.list_adapters.RolesAdapter;
import com.example.bast.objects.Lock;
import com.example.bast.objects.Role;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RoleListActivity extends AppCompatActivity {

    public ArrayList<Role> roles = new ArrayList<>();
    RecyclerView rv;
    RolesAdapter adapter;
    Dialog addDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_list);
        TextView title = (TextView) findViewById(R.id.activity_title);
        title.setText("ROLES");

        /*
        * adding data using JSON object
        *
        * When exchanging data between a browser and a server, the data can only be text.
        * JSON is text, and we can convert any JavaScript object into JSON,
        * and send JSON to the server.
        * We can also convert any JSON received from the server into JavaScript objects.
        * */

        //dummy data
        String jsonString = "[{\"Name\":\"Admin\"},{\"Name\":\"Developer\"},{\"Name\":\"Guest\"}," +
                "{\"Name\":\"IT\"},{\"Name\":\"Researcher\"},{\"Name\":\"Staff\"}]";

        JSONArray jsonObject = null;
        try {
            jsonObject = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < jsonObject.length(); i++){
            try {
                String name = (String) jsonObject.getJSONObject(i).get("Name");
                roles.add(new Role(name));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // shows the recycler view of items
        rolesRecyclerView();
        initButton();
    }

    // initializes the view of roles
    private void rolesRecyclerView() {
        rv = findViewById(R.id.recycler_view);
        adapter = new RolesAdapter(this, roles);
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
                addRole();
            }
        });
    }

    public void addRole() {
        addDialog.setContentView(R.layout.add_role);

        TextView title = (TextView) addDialog.findViewById(R.id.role_title);
        EditText name = (EditText) addDialog.findViewById(R.id.rolename);

        Button add_role = (Button) addDialog.findViewById(R.id.add_button);
        add_role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add to roles database
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
            Role deletedRole = roles.remove(pos);
            adapter.notifyItemRemoved(pos);

            Snackbar.make(rv, deletedRole.getRoleName(), Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            roles.add(pos, deletedRole);
                            adapter.notifyItemInserted(pos);
                        }
                    }).show();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, rv, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(RoleListActivity.this, R.color.delete))
                    .addActionIcon(R.drawable.ic_delete_black_24dp)
                    .create()
                    .decorate();

            super.onChildDraw(c, rv, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}
