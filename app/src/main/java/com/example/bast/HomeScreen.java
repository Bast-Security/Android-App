package com.example.bast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void joinSystem(View view){
        Log.d("clicked", "userClick");
        //ip address of fabio's computer
        String ipAdress = "http://192.168.1.99:8080/addUser";
        //gives the object
        final TextView textView = (TextView) findViewById(R.id.username);

        final String userName = textView.getText().toString();
        Log.d("userName",userName);

        //sending data over
        String payload = "{\"name\": \"" + userName + "\"}";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST,ipAdress,
                new Response.Listener<String>(){
            @Override
                    public void onResponse(String response){}
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){}
        });

        queue.add(request);

        setContentView(R.layout.activity_create_system);


        //create public and private pair
        //list is going to be empty at the first time

        //figure out how to send a message[

    }

}
