package com.example.bast.list_adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.R;
import com.example.bast.objects.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private ArrayList<User> users;
    private Context mContext;
    private OnUserListener mOnUserListener;

    public UsersAdapter(ArrayList<User> users, Context mContext, OnUserListener onUserListener) {
        this.users = users;
        this.mContext = mContext;
        this.mOnUserListener = onUserListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        ViewHolder holder = new ViewHolder(view, mOnUserListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.userName.setText(users.get(position).getUserName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView userName;
        RelativeLayout item_parent;
        OnUserListener onUserListener;

        public ViewHolder(@NonNull View itemView, OnUserListener onUserListener) {
            super(itemView);
            item_parent = itemView.findViewById(R.id.list_parent_layout);
            userName = itemView.findViewById(R.id.list_item);
            this.onUserListener = onUserListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onUserListener.OnUserClick(getAdapterPosition());
        }
    }

    public interface OnUserListener{
        void OnUserClick(int position);
    }

    public User getUser(int position) { return users.get(position); }

    /**method will receive a string that will be in a form of a json object
     * @param jsonString - the string that will be received from the controller*/
    public void getUsers(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
        }catch (JSONException err){
            Log.d("Error", err.toString());
        }
    }

}
