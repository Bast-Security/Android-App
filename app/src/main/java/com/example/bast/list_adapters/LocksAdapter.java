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
import com.example.bast.objects.Lock;

import java.util.ArrayList;

public class LocksAdapter extends RecyclerView.Adapter<LocksAdapter.ViewHolder> {

    private ArrayList<Lock> locks;
    private Context mContext;

    public LocksAdapter(Context mContext, ArrayList<Lock> locks) {
        this.locks = locks;
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
        holder.lockName.setText(locks.get(position).getLockName());

        holder.item_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return locks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView lockName;
        RelativeLayout item_parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lockName = itemView.findViewById(R.id.list_item);
            item_parent = itemView.findViewById(R.id.list_parent_layout);

        }
    }

    public interface OnLockListener {
        void onLockClick(int position);
    }

}
