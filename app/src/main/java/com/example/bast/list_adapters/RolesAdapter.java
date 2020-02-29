package com.example.bast.list_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.R;
import com.example.bast.objects.Role;

import java.util.ArrayList;

public class RolesAdapter extends RecyclerView.Adapter<RolesAdapter.RolesAdapterViewHolder> {
    private ArrayList<Role> mRoles = new ArrayList<>();
    private Context mContext;

    public RolesAdapter(Context context, ArrayList<Role> roles) {
        mContext = context;
        mRoles = roles;
    }

    @NonNull
    @Override
    public RolesAdapter.RolesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        RolesAdapterViewHolder viewHolder = new RolesAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RolesAdapter.RolesAdapterViewHolder holder, int position) {
        holder.roleName.setText(mRoles.get(position).getRoleName());
    }

    @Override
    public int getItemCount() {
        return mRoles.size();
    }

    public class RolesAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView roleName;
        LinearLayout item_parent;

        public RolesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            item_parent = itemView.findViewById(R.id.list_parent_layout);
            roleName = itemView.findViewById(R.id.new_item);
        }
    }

}
