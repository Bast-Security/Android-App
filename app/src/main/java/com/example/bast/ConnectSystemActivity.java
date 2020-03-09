package com.example.bast;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class ConnectSystemActivity extends AppCompatActivity {

    Dialog successDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_system);

        Button button = (Button) findViewById(R.id.add_button);
        EditText systemName = (EditText) findViewById(R.id.username);

        String name = systemName.toString();

        successDialog = new Dialog(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

    }


    public void openDialog() {
        successDialog.setContentView(R.layout.popup_add_menu);
        ImageView check = (ImageView) successDialog.findViewById(R.id.success_check);
        TextView successText = (TextView) successDialog.findViewById(R.id.success_text);

        successDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        successDialog.show();

        successDialog.setCanceledOnTouchOutside(true);

        successDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // dialog dismisses
                Intent homeIntent = new Intent(ConnectSystemActivity.this, HomeScreenActivity.class);
                ConnectSystemActivity.this.startActivity(homeIntent);
            }
        });
    }

}
