package com.example.bast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class serviceListAdapter
        extends RecyclerView.Adapter<serviceListAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View serviceList = inflater.inflate(R.layout.list_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(serviceList);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull serviceListAdapter.ViewHolder viewHolder, int position) {
        String service = services.get(position);

        TextView textView = viewHolder.service;
        textView.setText(service);

    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView service;

        public ViewHolder(View item) {
            super(item);

            service = (TextView) item.findViewById(R.id.rvSystems);
        }
    }

    private List<String> services;

    public serviceListAdapter(List<String> service) {
        services = service;
    }
}
