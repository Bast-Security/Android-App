package com.example.bast;

import android.content.Context;
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

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> systems;
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> systems, Context mContext) {
        this.systems = systems;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_system_row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.systemName.setText(systems.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + systems.get(position));
                Toast.makeText(mContext, systems.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return systems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView systemName;
        Button connectButton;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            systemName = itemView.findViewById(R.id.newsystemname);
            connectButton = itemView.findViewById(R.id.addbutton);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}

