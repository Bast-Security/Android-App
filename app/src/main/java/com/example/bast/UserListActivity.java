package com.example.bast;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
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
import com.example.bast.objects.Session;
import com.example.bast.objects.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import okhttp3.Response;

public class UserListActivity extends AppCompatActivity implements UsersAdapter.OnUserListener {
    private static final String TAG = "UserListActivity";

    // Initialize values
    private final ArrayList<User> usersList = new ArrayList<>();
    private final UsersAdapter adapter = new UsersAdapter(usersList, this, this);
    private RecyclerView rv;
    private Session session;
    private Dialog userDialog;
    private String systemName;
    private int systemId;
    private String jwt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize values for database
        final Bundle bundle = getIntent().getExtras();
        if(jwt == null){jwt = bundle.getString("jwt");}
        if(systemName == null){systemName = bundle.getString("systemName");}
        systemId = bundle.getInt("systemId");
        session = new Session(jwt);
        refreshUsers(session);

        // Display the list
        setContentView(R.layout.activity_general_list);
        final TextView activityTitle = findViewById(R.id.activity_title);
        activityTitle.setText("USERS");
        rv = findViewById(R.id.recycler_view);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Initialize buttons
        final Button addButton = findViewById(R.id.add_btn);
        final Dialog addDialog = new Dialog(this);
        userDialog= new Dialog(this);

        // Initialize swipe to delete users
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv);

        // OnClickListener to add users
        addButton.setOnClickListener((view) -> {
                    addDialog.setContentView(R.layout.popup_add_usermenu);

                    // Initialize variables in popup_add_menu
                    TextView title = addDialog.findViewById(R.id.usertitle);
                    EditText name = addDialog.findViewById(R.id.username);
                    EditText email = addDialog.findViewById(R.id.email);
                    EditText pin = addDialog.findViewById(R.id.pin);
                    EditText card = addDialog.findViewById(R.id.cardnum);
                    EditText phone = addDialog.findViewById(R.id.phone);

                    // Initialize button to add user once fields have been filled out
                    Button add_user = addDialog.findViewById(R.id.add_user_button);
                    add_user.setOnClickListener(v -> {
                                Log.d("user", "Adding User!");

                                try {
                                    final String userName = name.getText().toString();
                                    final String userEmail = email.getText().toString();
                                    final String userPhoneNumber = phone.getText().toString();
                                    final String userPin = pin.getText().toString();
                                    final String userCard = card.getText().toString();

                                    // turning the input fields into fields of a JSON object
                                    final JSONObject payload = new JSONObject()
                                            .accumulate("name", userName)
                                            .accumulate("email", userEmail)
                                            .accumulate("phone", userPhoneNumber)
                                            .accumulate("pin", userPin)
                                            .accumulate("cardno", userCard);

                                    // HTTP request to post to the database
                                    String HTTPPost = "systems/" + systemId + "/users";
                                    session.requestAsync(HTTP.post(HTTPPost, payload), (response) -> {
                                        if (response.code() != 200) {
                                            Toast.makeText(this, "Failed to Add User",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            addDialog.dismiss();
                                        }

                                        response.close();
                                        refreshUsers(session);
                                    });
                                } catch (Exception e) {
                                    Log.d("user", e.toString());
                                    Toast.makeText(this, "Failed to Add User",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                    );

                    // Displays the popup_add_menu
                    addDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    addDialog.show();

                    // Initialize button to exit out of popup_add_menu
                    FloatingActionButton exit = addDialog.findViewById(R.id.exit_button);
                    exit.setOnClickListener(v -> addDialog.dismiss());
                }
        );
        // Refreshes the list of users once a user is added
        refreshUsers(session);
    }

    // Initializes the swipe feature
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // Initialize variables for deleting a user
            int pos = viewHolder.getAdapterPosition();
            User deletedUser = usersList.remove(pos);
            int userId = deletedUser.getUserID();
            String userID = Integer.toString(userId);
            adapter.notifyItemRemoved(pos);

            // Initializing HTTP variables for delete requests
            final Bundle bundle = getIntent().getExtras();
            final int systemId = bundle.getInt("systemId");

            Async.task(() -> {
                try {
                    // HTTP delete requests
                    final String file = String.format("systems/"+ systemId + "/users/" + userID);
                    Log.d("user", "Deleting user at path: " + file);
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

            // Display a snackbar popup to undo a delete
            Snackbar.make(rv, deletedUser.getUserName(), Snackbar.LENGTH_LONG)
                    .setAction("Undo", v -> {
                        usersList.add(pos, deletedUser);
                        try {
                            // turning the input fields into fields of a JSON object
                            final JSONObject payload = new JSONObject()
                                    .accumulate("name", deletedUser.getUserName())
                                    .accumulate("email", deletedUser.getEmail())
                                    .accumulate("phone", deletedUser.getPhoneNumber())
                                    .accumulate("pin", deletedUser.getPin())
                                    .accumulate("cardno", deletedUser.getCardNumber());

                            // HTTP request to post to the database
                            String HTTPPost = "systems/" + systemId + "/users";
                            session.requestAsync(HTTP.post(HTTPPost, payload), (response) -> {
                                if (response.code() != 200) {} else {}
                                response.close();
                                refreshUsers(session);
                            });
                        } catch (Exception e) {
                            Log.d("user", e.toString());
                        }
                        adapter.notifyItemInserted(pos);
                    }).show();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView rv,
                                @NonNull RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, rv, viewHolder, dX, dY, actionState,
                    isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(
                            UserListActivity.this, R.color.delete))
                    .addActionIcon(R.drawable.ic_delete_black_24dp)
                    .create()
                    .decorate();

            super.onChildDraw(c, rv, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    // Refreshes the list of users
    public void refreshUsers(Session session) {
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
                        final User user = new User(object.getInt("id"),
                                object.getString("name"), object.getString("email"),
                                object.getString("phone"), object.getString("pin"),
                                object.getString("cardno"));
                        usersList.add(user);
                    }

                    handler.post(adapter::notifyDataSetChanged);
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
        userDialog.setContentView(R.layout.popup_display_userinfo);

        //final String uName, uEmail, uPhone;
        final int uCard, uPin;

        TextView title = userDialog.findViewById(R.id.userinfo);

        TextView name_header = userDialog.findViewById(R.id.textView_name);
        TextView username = userDialog.findViewById(R.id.textView_username);
        username.setText(u.getUserName());

        TextView roles_header = userDialog.findViewById(R.id.textView_roles);
        TextView roles = userDialog.findViewById(R.id.textView_roleslist);
        roles.setText(u.getRoles().toString());

        TextView email_header = userDialog.findViewById(R.id.textView_mail);
        TextView email = userDialog.findViewById(R.id.textView_email);
        email.setText(u.getEmail());

        TextView pin_header = userDialog.findViewById(R.id.textView_pincode);
        TextView pin = userDialog.findViewById(R.id.textView_pin);
        pin.setText(u.getPin());

        TextView card_header = userDialog.findViewById(R.id.textView_card);
        TextView card = userDialog.findViewById(R.id.textView_cardnum);
        card.setText(u.getCardNumber());

        TextView phone_header = userDialog.findViewById(R.id.textView_phonenumber);
        TextView phone = userDialog.findViewById(R.id.textView_phone);
        phone.setText(u.getPhoneNumber());

        Button edit_user = userDialog.findViewById(R.id.edit_button);
        edit_user.setOnClickListener(v -> {
            userDialog.dismiss();
            Intent intent = new Intent(UserListActivity.this,
                    EditUserActivity.class);
            intent.putExtra("systemName", systemName);
            intent.putExtra("systemId",systemId);
            intent.putExtra("jwt", jwt);
            intent.putExtra("userId", u.getUserID());
            intent.putExtra("username", u.getUserName());
            intent.putExtra("email", u.getEmail());
            intent.putExtra("pin", u.getPin());
            intent.putExtra("phone", u.getPhoneNumber());
            intent.putExtra("card", u.getCardNumber());
            intent.putExtra("roles", u.getRoles());
            startActivity(intent);

        });
        Button role_change = userDialog.findViewById(R.id.roles_button);
        role_change.setOnClickListener(v -> {
            userDialog.dismiss();
            Intent intent = new Intent(UserListActivity.this,
                    ChangeUserRolesActivity.class);
            intent.putExtra("systemName", systemName);
            intent.putExtra("systemId",systemId);
            intent.putExtra("jwt", jwt);
            intent.putExtra("id", u.getUserID());
            intent.putExtra("username", u.getUserName());
            intent.putExtra("email", u.getEmail());
            intent.putExtra("pin", u.getPin());
            intent.putExtra("phone", u.getPhoneNumber());
            intent.putExtra("card", u.getCardNumber());
            intent.putExtra("roles", u.getRoles());
            startActivity(intent);
        });

        FloatingActionButton exit = userDialog.findViewById(R.id.floatingActionButton);
        exit.setOnClickListener(v -> userDialog.dismiss());

        userDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        userDialog.show();
    }

    @Override
    public void OnUserClick(int position) {
        User userClicked = usersList.get(position);
        Log.d("user", userClicked.getUserName() + " clicked");
        displayUser(userClicked);
    }



}