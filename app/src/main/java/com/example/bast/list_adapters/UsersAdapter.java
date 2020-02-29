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
import com.example.bast.objects.User;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersAdapterViewHolder> {
    private ArrayList<User> mUsers = new ArrayList<>();
    private Context mContext;

    public UsersAdapter(Context context, ArrayList<User> Users) {
        mContext = context;
        mUsers = Users;
    }

    @NonNull
    @Override
    public UsersAdapter.UsersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        UsersAdapterViewHolder viewHolder = new UsersAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.UsersAdapterViewHolder holder, int position) {
        holder.UserName.setText(mUsers.get(position).getUserName());
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class UsersAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView UserName;
        LinearLayout item_parent;

        public UsersAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            item_parent = itemView.findViewById(R.id.list_parent_layout);
            UserName = itemView.findViewById(R.id.new_item);
        }
    }

}
