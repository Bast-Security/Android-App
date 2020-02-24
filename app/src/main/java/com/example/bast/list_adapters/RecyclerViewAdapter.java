package com.example.bast.list_adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.ConnectSystemActivity;
import com.example.bast.R;
import com.example.bast.SystemMenuActivity;
import com.example.bast.objects.System;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<System> systems;
    private Context mContext;

    // creates the adapter from the list of systems
    public RecyclerViewAdapter(ArrayList<System> systems, Context mContext) {
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

    // replades the contents of a view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.systemName.setText(systems.get(position).getSystemName());
        if(systems.get(position).isConnected() == true){
            holder.parentLayout.setBackgroundColor(Color.WHITE);
        }
        else{
            holder.parentLayout.setBackgroundColor(Color.LTGRAY);
        }

        // adds connect button if the system is not already connected
        holder.systemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                Log.d(TAG, "onClick: clicked on: " + systems.get(position));
                Toast.makeText(mContext, systems.get(position).getSystemName(), Toast.LENGTH_SHORT).show();

                if(systems.get(position).isConnected()) { intent = new Intent(mContext, SystemMenuActivity.class); }
                else { intent = new Intent(mContext, ConnectSystemActivity.class); }
                mContext.startActivity(intent);
            }
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

            systemName = itemView.findViewById(R.id.new_system);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            
        }

    }
}

