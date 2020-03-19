package com.example.paybill.ui;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.paybill.R;
import com.example.paybill.Utils.UserDetailData;

public class HistoryDetail extends AppCompatActivity {
    String date,desc,amount;
    TextView tvAccountNumber, tvDate, tvDescription, tvAmount;
    Button btnGoBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            date=bundle.getString("Date");
            desc=bundle.getString("Description");
            amount=bundle.getString("Amount");
        }
        //Initialize views
        tvAccountNumber = findViewById(R.id.tv_account_number);
        tvDate = findViewById(R.id.tv_date);
        tvDescription = findViewById(R.id.tv_description);
        tvAmount = findViewById(R.id.tv_amount);
        btnGoBack = findViewById(R.id.btn_go_back);

        //Set value to each views
        tvAccountNumber.setText(""+UserDetailData.userAccountID);
        tvDate.setText(date);
        tvDescription.setText(desc);
        tvAmount.setText(amount);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
