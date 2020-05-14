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
import com.example.bast.objects.Role;

import java.util.ArrayList;
import java.util.List;

public class UserRoleCheckList extends ArrayAdapter<Role> {

    private List<Role> roles;
    private Context context;
    private List<Role> checkedRoles = new ArrayList<>();

    public UserRoleCheckList(List<Role> roles, Context context) {
        super(context, R.layout.checkbox_item, roles);
        this.roles = roles;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View row = inflater.inflate(R.layout.checkbox_item, parent, false);
        TextView roleName = row.findViewById(R.id.checkbox_name);
        roleName.setText(roles.get(position).getRoleName());
        CheckBox checkBox = row.findViewById(R.id.checkbox_item);

        checkBox.setTag(Integer.valueOf(position));
        checkBox.setOnCheckedChangeListener(mListener);


        return row;
    }

    CompoundButton.OnCheckedChangeListener mListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Role checkedRole = new Role(roles.get((Integer) buttonView.getTag()).getRoleName());
            if(buttonView.isChecked()){
                Log.d("role", checkedRole.getRoleName() + " checked");
                checkedRoles.add(checkedRole);
            }
            else {
                Log.d("role", checkedRole.getRoleName() + " unchecked");
                checkedRoles.remove(checkedRole);
            }
        }
    };

    public List<Role> getCheckedRoles(){
        return checkedRoles;
    }

}
