package com.example.bast.list_adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bast.R;
import com.example.bast.objects.Lock;

import java.util.ArrayList;
import java.util.List;

public class LockCheckList extends ArrayAdapter<Lock> {

    private List<Lock> locks;
    private Context context;
    private List<Lock> checkedLocks = new ArrayList<>();

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
        TextView lockName = row.findViewById(R.id.checkbox_name);
        lockName.setText(locks.get(position).getLockName());

        CheckBox checkBox = row.findViewById(R.id.checkbox_item);

        checkBox.setTag(Integer.valueOf(position));
        checkBox.setOnCheckedChangeListener(mListener);
        return row;
    }

    CompoundButton.OnCheckedChangeListener mListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Lock checkedLock = new Lock(locks.get((Integer) buttonView.getTag()).getLockName());
            if(buttonView.isChecked()){
                Log.d("lock", checkedLock.getLockName() + " checked");
                checkedLocks.add(checkedLock);
            }
            else {
                Log.d("lock", checkedLock.getLockName() + " unchecked");
                checkedLocks.remove(checkedLock);
            }
        }
    };

    public List<Lock> getCheckedLocks(){
        return checkedLocks;
    }

}
