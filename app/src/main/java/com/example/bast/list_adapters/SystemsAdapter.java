package com.example.bast.list_adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.HomeScreenActivity;
import com.example.bast.R;
import com.example.bast.SystemMenuActivity;
import com.example.bast.objects.System;

import java.util.ArrayList;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    // replaces the contents of a view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final System system = systems.get(position);

        holder.systemName.setText(system.name);

        holder.parentLayout.setOnClickListener((view) -> {
            Log.d(TAG, "Clicked on: " + system.name);

            Toast.makeText(mContext, system.name, Toast.LENGTH_SHORT).show();

            final Intent intent = new Intent(mContext, SystemMenuActivity.class);
            intent.putExtra("jwt", ((HomeScreenActivity) mContext).getSession().jwt);
            intent.putExtra("systemName", system.name);
            intent.putExtra("systemId", system.id);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return systems.size();
    }

    public System getSystem(int position) { return systems.get(position); }

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

