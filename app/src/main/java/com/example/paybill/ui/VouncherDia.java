package com.example.paybill.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paybill.CreateAccount.User;
import com.example.paybill.R;
import com.example.paybill.Utils.UserDetailData;
import com.example.paybill.ui.send.SendFragment;


public class VouncherDia {

    TextView account,amount,desc;
    private Context context;

    public VouncherDia(Context context) {
        this.context = context;
    }

    public void addNewMessage(int dialog_layout) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(dialog_layout, null);
        account=(TextView)subView.findViewById(R.id.account);
        amount=(TextView)subView.findViewById(R.id.amount);
        desc=(TextView)subView.findViewById(R.id.description);

//        account.setText(UserDetailData.account_send);
//        amount.setText(UserDetailData.amount_send);
//        desc.setText(UserDetailData.description_send);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Circumstance In Your Wallet?");
        builder.setView(subView);
        builder.create();


        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show();
             //UserDetailData.ok_send=true;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Toast.makeText(context, "Cancel", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }




}
