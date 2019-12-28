package com.example.paybill.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.paybill.R;
import com.example.paybill.Utils.ConfigUtils;
import com.example.paybill.Utils.Helper;

import java.util.Locale;

public class SettingFragment extends Fragment {

    private Button btnChangeLanguage;
    private Button btnReport;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_setting,container,false);
        btnChangeLanguage = view.findViewById(R.id.btn_change_language);
        btnReport = view.findViewById(R.id.btn_report);
        if (ConfigUtils.getInstance(getContext()).loadLocale().equals("my")) {
           // Toast.makeText(getContext(), "my", Toast.LENGTH_SHORT).show();
        } else
        {
           // Toast.makeText(getContext(), "en", Toast.LENGTH_SHORT).show();
        }

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Thanks for your report!!", Toast.LENGTH_SHORT).show();
            }
        });


        btnChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        return view;
    }

    private void showDialog() {

        final Dialog customDialog = new Dialog(getContext());

        customDialog.setContentView(R.layout.dialog_language);

        Window window = customDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        TextView btnMyanmar = window.findViewById(R.id.btn_myanmar);
        TextView btnEnglish = window.findViewById(R.id.btn_english);

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
    }
}
