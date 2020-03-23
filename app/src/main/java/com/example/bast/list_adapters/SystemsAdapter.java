package com.example.bast.list_adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.ConnectSystemActivity;
import com.example.bast.R;
import com.example.bast.SystemMenuActivity;
import com.example.bast.objects.HTTP;
import com.example.bast.objects.Session;
import com.example.bast.objects.System;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.InetAddress;
import java.net.URL;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.ArrayList;
import java.util.function.Consumer;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SystemsAdapter extends RecyclerView.Adapter<SystemsAdapter.ViewHolder> {

    private static final String TAG = "SystemsAdapter";

    private ArrayList<System> systems;
    private Context mContext;

    // creates the adapter from the list of systems
    public SystemsAdapter(ArrayList<System> systems, Context mContext) {
        this.systems = systems;
        this.mContext = mContext;
    }

    // creates a new view for the addition of a new system from layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_system_row, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    // replaces the contents of a view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        final System system = systems.get(position);
        String display = system.getSystemName();

        if (system.isOrphan()) {
            display = "(Orphan) " + display;
        }

        holder.systemName.setText(display);
        if(systems.get(position).isConnected() == true){
            holder.parentLayout.setBackgroundColor(Color.WHITE);
        } else{
            holder.parentLayout.setBackgroundColor(Color.LTGRAY);
        }

        holder.parentLayout.setOnClickListener((view) -> {
            Log.d(TAG, "onClick: clicked on: " + systems.get(position));
            Toast.makeText(mContext, systems.get(position).getSystemName(), Toast.LENGTH_SHORT).show();

            final Intent intent = new Intent(mContext, SystemMenuActivity.class);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return systems.size();
    }

    // displays the recycler view
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView systemName;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            systemName = itemView.findViewById(R.id.list_item);
            parentLayout = itemView.findViewById(R.id.list_parent_layout);
        }
    }
}

