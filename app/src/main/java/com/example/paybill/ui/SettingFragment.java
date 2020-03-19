package com.example.paybill.ui;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.paybill.R;
import com.example.paybill.Utils.ConfigUtils;
import com.example.paybill.Utils.Helper;
import com.example.paybill.Utils.UserDetailData;
import com.firebase.client.Firebase;

import java.util.Calendar;
import java.util.Locale;

import static com.example.paybill.Utils.UserDetailData.username;

public class SettingFragment extends Fragment {

    private Button btnChangeLanguage;
    private Button btnpasschange;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_setting,container,false);
        btnChangeLanguage = view.findViewById(R.id.btn_change_language);
        if (ConfigUtils.getInstance(getContext()).loadLocale().equals("my")) {
           // Toast.makeText(getContext(), "my", Toast.LENGTH_SHORT).show();
        } else
        {
           // Toast.makeText(getContext(), "en", Toast.LENGTH_SHORT).show();
        }

        btnChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnpasschange=(Button)view.findViewById(R.id.btn_passchange);

        btnpasschange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (etcurrent_password.getText().toString().equals(UserDetailData.password)){
                    Firebase reference = new Firebase("https://paybill-58d29.firebaseio.com/users");
                    reference.child(username).child("password").setValue(editText_password.getText().toString());
                    Toast.makeText(getContext(), "Successfully Changed", Toast.LENGTH_SHORT).show();
                    editText_password.setText("");
                    etcurrent_password.setText("");
                }
                else {
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }*/
                showPasswordDialog();
            }
        });
    }

    private void showPasswordDialog(){
        final Dialog customDialog = new Dialog(getContext());

        customDialog.setContentView(R.layout.dialog_change_password);

        Window window = customDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        final EditText etNewPassword = window.findViewById(R.id.et_new_password);
        final EditText etOldPassword = window.findViewById(R.id.et_old_password);
        Button btnChange = window.findViewById(R.id.btn_change);

        customDialog.show();

        btnChange.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (etOldPassword.getText().toString().equals(UserDetailData.password)){
                    Firebase reference = new Firebase("https://paybill-58d29.firebaseio.com/users");
                    reference.child(username).child("password").setValue(etNewPassword.getText().toString());
                    Toast.makeText(getContext(), "Successfully Change", Toast.LENGTH_SHORT).show();
                    customDialog.dismiss();
                }
                else {
                    Toast.makeText(getContext(), "Fail to change password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDialog() {

        final Dialog customDialog = new Dialog(getContext());

        customDialog.setContentView(R.layout.dialog_language);

        Window window = customDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        TextView btnMyanmar = window.findViewById(R.id.btn_myanmar);
        TextView btnEnglish = window.findViewById(R.id.btn_english);
        TextView btnChinese = window.findViewById(R.id.btn_chinese);

        customDialog.show();


        btnMyanmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.changeLocation(getContext(), "my");
                getActivity().recreate();
                ConfigUtils.getInstance(getContext()).saveLocale("my");
                customDialog.dismiss();
            }
        });

        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.changeLocation(getContext(), "en");
                getActivity().recreate();
                ConfigUtils.getInstance(getContext()).saveLocale("en");
                customDialog.dismiss();
            }
        });

        btnChinese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.changeLocation(getContext(), "zh");
                getActivity().recreate();
                ConfigUtils.getInstance(getContext()).saveLocale("zh");
                customDialog.dismiss();
            }
        });
    }
}
