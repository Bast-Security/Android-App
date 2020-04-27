package com.example.bast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bast.objects.HTTP;
import com.example.bast.objects.Session;

import org.json.JSONException;
import org.json.JSONObject;

public class EditUserActivity extends AppCompatActivity {
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        // initialize xml EditText
        EditText username = findViewById(R.id.textView_username);
        EditText email = findViewById(R.id.textView_email);
        EditText card = findViewById(R.id.textView_cardnum);
        EditText pin = findViewById(R.id.textView_pin);
        EditText phone = findViewById(R.id.textView_phone);
        Button confirmEditButton = findViewById(R.id.edit_button);

        // pulling values from previous activity
        Bundle bundle = getIntent().getExtras();
        String usernameBefore = bundle.getString("username");
        String emailBefore = bundle.getString("email");
        String cardBefore = bundle.getString("card");
        String pinBefore = bundle.getString("pin");
        String phoneBefore = bundle.getString("phone");
        int systemId = bundle.getInt("systemId");
        String systemName = bundle.getString("systemName");
        int userId = bundle.getInt("userId");
        String jwt = bundle.getString("jwt");

        session = new Session(jwt);

        // setting values to xml
        if(username != null){username.setText(usernameBefore);}
        if(email != null){email.setText(emailBefore);}
        if(card != null){card.setText(cardBefore);}
        if(pin != null){pin.setText(pinBefore);}
        if(phone != null){phone.setText(phoneBefore);}

        // confirm user edit
        confirmEditButton.setOnClickListener((view) ->{
            Log.d("user", "Updating User");

            try {
                final String userName = username.getText().toString();
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
                String HTTPPut = "systems/" + systemId + "/users/" + userId;
                Log.d("user", "Updating user at path: " + HTTPPut);
                //TODO: fix this put request
                session.requestAsync(HTTP.put(HTTPPut, payload), (response) -> {
                    if (response.code() != 200) {
                    }
            });
                Intent backToUserList = new Intent(this,
                        UserListActivity.class);
                backToUserList.putExtra("jwt", jwt);
                backToUserList.putExtra("systemId", systemId);
                backToUserList.putExtra("systemName", systemName);
                startActivity(backToUserList);
        } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
}
