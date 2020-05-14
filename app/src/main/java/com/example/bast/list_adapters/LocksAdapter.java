package com.example.bast.list_adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.R;
import com.example.bast.objects.Lock;

import java.util.ArrayList;

public class LocksAdapter extends RecyclerView.Adapter<LocksAdapter.ViewHolder> {

    private ArrayList<Lock> locks;
    private Context mContext;
    private OnLockListener mOnLockListener;

    public LocksAdapter(ArrayList<Lock> locks, Context mContext, OnLockListener onLockListener) {
        this.locks = locks;
        this.mContext = mContext;
        this.mOnLockListener = onLockListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        ViewHolder holder = new ViewHolder(view, mOnLockListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.lockName.setText(locks.get(position).getLockName());
    }

    @Override
    public int getItemCount() {
        return locks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView lockName;
        RelativeLayout item_parent;
        OnLockListener onLockListener;

        public ViewHolder(@NonNull View itemView, OnLockListener onLockListener) {
            super(itemView);
            item_parent = itemView.findViewById(R.id.list_parent_layout);
            lockName = itemView.findViewById(R.id.list_item);
            this.onLockListener = onLockListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onLockListener.OnLockClick(getAdapterPosition());
            Log.d("lock", "lock clicked");
        }
    }

    public interface OnLockListener{
        void OnLockClick(int position);
    }

}
