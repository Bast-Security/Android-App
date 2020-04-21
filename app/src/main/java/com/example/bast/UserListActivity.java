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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.list_adapters.UsersAdapter;
import com.example.bast.objects.Async;
import com.example.bast.objects.HTTP;
import com.example.bast.objects.User;
import com.example.bast.objects.Session;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import okhttp3.Response;

public class UserListActivity extends AppCompatActivity {
    private static final String TAG = "UserListActivity";

    private final ArrayList<User> usersList = new ArrayList<User>();
    private final UsersAdapter adapter = new UsersAdapter(usersList, this);
    private RecyclerView rv;
    private Session session;
    Dialog addDialog, displayDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = getIntent().getExtras();
        final String jwt = bundle.getString("jwt");
        final String systemName = bundle.getString("systemName");
        final int systemId = bundle.getInt("systemId");
        session = new Session(jwt);

        setContentView(R.layout.activity_general_list);
        final TextView activityTitle = findViewById(R.id.activity_title);
        activityTitle.setText("USERS");

        rv = findViewById(R.id.recycler_view);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        final Button addButton = findViewById(R.id.add_btn);
        final Dialog addDialog = new Dialog(this);

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv);

        addButton.setOnClickListener((view) -> {
                addDialog.setContentView(R.layout.popup_add_menu);

                TextView title = addDialog.findViewById(R.id.usertitle);
                EditText name = addDialog.findViewById(R.id.username);
                EditText email = addDialog.findViewById(R.id.email);
                EditText pin = addDialog.findViewById(R.id.pin);
                EditText card = addDialog.findViewById(R.id.cardnum);
                EditText phone = addDialog.findViewById(R.id.phone);


                Button add_user = addDialog.findViewById(R.id.add_user_button);
                add_user.setOnClickListener(v -> {
                    Log.d("user", "Adding User!");

                    try {
                        final String userName = name.getText().toString();
                        final String userEmail = email.getText().toString();
                        final String userPhoneNumber = phone.getText().toString();
                        final String userPin = pin.getText().toString();
                        final String userCard = card.getText().toString();

                        final JSONObject payload = new JSONObject()
                                .accumulate("name", userName)
                                .accumulate("email", userEmail)
                                .accumulate("phone", userPhoneNumber)
                                .accumulate("pin", userPin)
                                .accumulate("cardno", userCard);

                        String HTTPPost = "systems/" + systemId + "/users";
                        session.requestAsync(HTTP.post(HTTPPost, payload), (response) -> {
                            if (response.code() != 200) {
                                Toast.makeText(this, "Failed to Add User", Toast.LENGTH_SHORT).show();
                            } else {
                                addDialog.dismiss();
                            }

                            response.close();
                        });
                    } catch (Exception e) {
                        Log.d("user", e.toString());
                        Toast.makeText(this, "Failed to Add User", Toast.LENGTH_SHORT).show();
                    }
                }
                );


                addDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                addDialog.show();

                FloatingActionButton exit = addDialog.findViewById(R.id.exit_button);
                exit.setOnClickListener(v -> addDialog.dismiss());
        }

        );

        refreshSystems(session);
    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int pos = viewHolder.getAdapterPosition();
            User deletedUser = usersList.remove(pos);
            String userName = deletedUser.getUserName();
            Log.d("user", userName);
            adapter.notifyItemRemoved(pos);

            final Bundle bundle = getIntent().getExtras();
            final int systemId = bundle.getInt("systemId");

            Async.task(() -> {
                try {
                    final String file = String.format("systems/"+ systemId + "/users/" + userName);
                    Log.d("user", file);
                    try (final Response response = session.request(HTTP.delete(file))) {
                        if (!response.isSuccessful()) {
                            throw new Exception("Request failed with status " + response.code());
                        }

                    }
                } catch (JSONException e) {
                    Log.d("user", "JSONException " + e.toString());
                } catch (IOException e) {
                    Log.d("user", "IOException " + e.toString());
                } catch (Exception e) {
                    Log.d("user", e.toString());
                }
            });

            Snackbar.make(rv, deletedUser.getUserName(), Snackbar.LENGTH_LONG)
                    .setAction("Undo", v -> {
                        usersList.add(pos, deletedUser);
                        adapter.notifyItemInserted(pos);
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

    public void refreshSystems(Session session) {
        final Handler handler = new Handler();
        final Bundle bundle = getIntent().getExtras();
        final int systemId = bundle.getInt("systemId");
        Async.task(() -> {
            String HTTPGet = "systems/" + systemId + "/users";
            try (final Response response = session.request(HTTP.get(HTTPGet))) {
                if (response.isSuccessful()) {
                    final String responseBody = response.body().string();
                    Log.d("user", responseBody);
                    usersList.removeAll(usersList);

                    final JSONArray users = new JSONArray(responseBody);
                    for (int i = 0; i < users.length(); i++) {
                        final JSONObject object = users.getJSONObject(i);
                        final User user = new User(object.getString("name"), object.getString("email"),
                                object.getString("phone"), object.getString("pin"),
                                object.getString("cardno"));
                        usersList.add(user);
                    }

                    handler.post(() -> adapter.notifyDataSetChanged());
                } else {
                    Log.d("user", "Bad response from server");
                }
            } catch (IOException e) {
                Log.d("user", "Failed to connect to host " + e.toString());
            } catch (JSONException e) {
                Log.d("user", "Failed to parse JSON string " + e.toString());
            }
        });
    }
    public void displayUser(User u) {
        addDialog.setContentView(R.layout.popup_display_info);

        //final String uName, uEmail, uPhone;
        //final int uCard, uPin;

        TextView title = (TextView) addDialog.findViewById(R.id.userinfo);

        TextView name_header = (TextView) addDialog.findViewById(R.id.textView_name);
        TextView username = (TextView) addDialog.findViewById(R.id.textView_username);
        username.setText(u.getUserName());

        TextView roles_header = (TextView) addDialog.findViewById(R.id.textView_roles);
        TextView roles = (TextView) addDialog.findViewById(R.id.textView_roleslist);
        roles.setText(u.getRoles().toString());

        TextView email_header = (TextView) addDialog.findViewById(R.id.textView_mail);
        TextView email = (TextView) addDialog.findViewById(R.id.textView_email);
        email.setText(u.getEmail());

        TextView pin_header = (TextView) addDialog.findViewById(R.id.textView_pincode);
        TextView pin = (TextView) addDialog.findViewById(R.id.textView_pin);
        pin.setText(u.getPin());

        TextView card_header = (TextView) addDialog.findViewById(R.id.textView_card);
        TextView card = (TextView) addDialog.findViewById(R.id.textView_cardnum);
        card.setText(u.getCardNumber());

        Button edit_user = (Button) addDialog.findViewById(R.id.edit_button);
        edit_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.dismiss();
                Intent intent = new Intent(UserListActivity.this, EditUserActivity.class);
                startActivity(intent);

            }
        });
        Button role_change = (Button) addDialog.findViewById(R.id.roles_button);
        role_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.dismiss();
                Intent intent = new Intent(UserListActivity.this, ChangeUserRolesActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onUserClick(int position) {
        User clicked = adapter.getUser(position);
        displayUser(clicked);
    }

}
