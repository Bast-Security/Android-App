package com.example.bast;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.list_adapters.UsersAdapter;
import com.example.bast.objects.Role;
import com.example.bast.objects.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class UserListActivity extends AppCompatActivity implements UsersAdapter.OnUserListener {

    ArrayList<User> users = new ArrayList<>();
    RecyclerView rv;
    UsersAdapter adapter;
    Dialog addDialog, displayDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_list);
        TextView title = (TextView) findViewById(R.id.activity_title);
        title.setText("USERS");

        //TODO: Delete dummy data when connected
        users.add(new User("Bast", "bast@gmail.com", "1234567890", 2789, 123456783));
        users.add(new User("Fabio", "bast@gmail.com", "1234567890", 2789, 123456783));
        users.add(new User("Lety", "bast@gmail.com", null, 2789, 123456783));
        users.add(new User("Evan", "bast@gmail.com", null, 2789, 123456783));
        users.add(new User("Kristen", "bast@gmail.com", "1234567890", 2789, 123456783));

        initRecyclerView();
        initButton();

    }

    /**
     * Initializes the Recycler View and its adapter
     */
    private void initRecyclerView() {
        rv = findViewById(R.id.recycler_view);
        adapter = new UsersAdapter(users, this);
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
                addUserMenu();
            }
        });
    }

    public void addUserMenu() {
        displayDialog.setContentView(R.layout.popup_add_menu);

        TextView title = (TextView) addDialog.findViewById(R.id.usertitle);
        EditText name = (EditText) addDialog.findViewById(R.id.username);
        EditText email = (EditText) addDialog.findViewById(R.id.email);
        EditText pin = (EditText) addDialog.findViewById(R.id.pin);
        EditText card = (EditText) addDialog.findViewById(R.id.cardnum);
        EditText phone = (EditText) addDialog.findViewById(R.id.phone);

        //final String uName, uEmail, uPhone;
        //final int uCard, uPin;

        Button add_user = (Button) addDialog.findViewById(R.id.add_user_button);
        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add to user database
                //users.add(new User(uName, uEmail, uPhone, uPin, uCard));
                addDialog.dismiss();
            }
        });

        FloatingActionButton exit = (FloatingActionButton) addDialog.findViewById(R.id.exit_button);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.dismiss();
            }
        });

        addDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        addDialog.show();
    }

    public void displayUser() {
        addDialog.setContentView(R.layout.popup_display_info);

        //final String uName, uEmail, uPhone;
        //final int uCard, uPin;

        TextView title = (TextView) addDialog.findViewById(R.id.userinfo);
        TextView name_header = (TextView) addDialog.findViewById(R.id.textView_name);
        TextView username = (TextView) addDialog.findViewById(R.id.textView_username);
        TextView email_header = (TextView) addDialog.findViewById(R.id.textView_mail);
        TextView email = (TextView) addDialog.findViewById(R.id.textView_email);
        TextView pin_header = (TextView) addDialog.findViewById(R.id.textView_pincode);
        TextView pin = (TextView) addDialog.findViewById(R.id.textView_pin);
        TextView card_header = (TextView) addDialog.findViewById(R.id.textView_card);
        TextView card = (TextView) addDialog.findViewById(R.id.textView_cardnum);

        Button edit_user = (Button) addDialog.findViewById(R.id.edit_button);
        edit_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.dismiss();
                Intent intent = new Intent(UserListActivity.this, EditUserActivity.class);
                startActivity(intent);

            }
        });

        FloatingActionButton exit = (FloatingActionButton) addDialog.findViewById(R.id.exit_button);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.dismiss();
                //Intent intent = new Intent(UserListActivity.this, EditUserActivity.class);
                //startActivity(intent);
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
            User deletedUser = users.remove(pos);
            adapter.notifyItemRemoved(pos);

            Snackbar.make(rv, deletedUser.getUserName(), Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            users.add(pos, deletedUser);
                            adapter.notifyItemInserted(pos);
                        }
                    }).show();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, rv, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(UserListActivity.this, R.color.delete))
                    .addActionIcon(R.drawable.ic_delete_black_24dp)
                    .create()
                    .decorate();

            super.onChildDraw(c, rv, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    public void onUserClick(int position) {
        displayUser();
    }
}
