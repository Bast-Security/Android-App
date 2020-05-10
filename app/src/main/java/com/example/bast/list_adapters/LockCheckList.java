package com.example.bast.list_adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bast.R;
import com.example.bast.objects.Lock;

import java.util.List;

public class LockCheckList extends ArrayAdapter<Lock> {

    private List<Lock> locks;
    private Context context;

    public LockCheckList(List<Lock> locks, Context context) {
        super(context, R.layout.checkbox_item, locks);
        this.locks = locks;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View row = inflater.inflate(R.layout.checkbox_item, parent, false);
        TextView roleName = row.findViewById(R.id.checkbox_name);
        roleName.setText(locks.get(position).getLockName());
        return row;
    }
}
