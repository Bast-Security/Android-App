package com.example.bast.list_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.R;
import com.example.bast.objects.Role;

import java.util.ArrayList;

public class RolesAdapter extends RecyclerView.Adapter<RolesAdapter.ViewHolder> {

    private ArrayList<Role> roles;
    private Context mContext;

    public RolesAdapter(Context mContext, ArrayList<Role> roles) {
        this.roles = roles;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.roleName.setText(roles.get(position).getRoleName());

        holder.item_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return roles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView roleName;
        RelativeLayout item_parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_parent = itemView.findViewById(R.id.list_parent_layout);
            roleName = itemView.findViewById(R.id.list_item);
        }
    }

}
