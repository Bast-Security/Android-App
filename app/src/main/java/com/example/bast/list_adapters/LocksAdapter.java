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
import com.example.bast.objects.Lock;

import java.util.ArrayList;

public class LocksAdapter extends RecyclerView.Adapter<LocksAdapter.LocksAdapterViewHolder> {
    private ArrayList<Lock> mLocks = new ArrayList<>();
    private Context mContext;

    public LocksAdapter(Context context, ArrayList<Lock> locks) {
        mContext = context;
        mLocks = locks;
    }

    @NonNull
    @Override
    public LocksAdapter.LocksAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        LocksAdapterViewHolder viewHolder = new LocksAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LocksAdapter.LocksAdapterViewHolder holder, int position) {
        holder.lockName.setText(mLocks.get(position).getLockName());
    }

    @Override
    public int getItemCount() {
        return mLocks.size();
    }

    public class LocksAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView lockName;
        LinearLayout item_parent;

        public LocksAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            item_parent = itemView.findViewById(R.id.list_parent_layout);
            lockName = itemView.findViewById(R.id.new_item);
        }
    }

}
