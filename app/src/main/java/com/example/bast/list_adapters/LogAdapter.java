package com.example.bast.list_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bast.R;
import com.example.bast.objects.Log;

import java.util.ArrayList;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder>{

    private ArrayList<Log> logs;
    private Context mContext;

    public LogAdapter(Context mContext, ArrayList<Log> logs) {
        this.logs = logs;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LogAdapter.ViewHolder holder, int position) {
        Log log = logs.get(position);

        holder.timestamp.setText(logs.get(position).getTimeAccessed());
        holder.door.setText(logs.get(position).getDoor());

    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView timestamp;
        TextView description;
        TextView door;
        CardView cv;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timestamp = (TextView)itemView.findViewById(R.id.log_timestamp);
            description = (TextView)itemView.findViewById(R.id.log_description);
            door = (TextView)itemView.findViewById(R.id.door_description);
            //cv = (CardView)itemView.findViewById(R.id.card);
            //layout = (LinearLayout)itemView.findViewById(R.id.log_card);
        }
    }
}
