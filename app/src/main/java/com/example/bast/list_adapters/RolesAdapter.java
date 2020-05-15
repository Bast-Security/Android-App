package com.example.bast.list_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private OnRoleListener mOnRoleListener;

    public RolesAdapter(ArrayList<Role> roles, Context mContext, OnRoleListener onRoleListener) {
        this.roles = roles;
        this.mContext = mContext;
        this.mOnRoleListener = onRoleListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        ViewHolder holder = new ViewHolder(view, mOnRoleListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.roleName.setText(roles.get(position).getRoleName());
    }

    @Override
    public int getItemCount() {
        return roles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView roleName;
        RelativeLayout item_parent;
        OnRoleListener onRoleListener;

        public ViewHolder(@NonNull View itemView, OnRoleListener onRoleListener) {
            super(itemView);
            item_parent = itemView.findViewById(R.id.list_parent_layout);
            roleName = itemView.findViewById(R.id.list_item);
            this.onRoleListener = onRoleListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRoleListener.OnRoleClick(getAdapterPosition());
        }
    }

    public interface OnRoleListener{
        void OnRoleClick(int position);
    }

}
