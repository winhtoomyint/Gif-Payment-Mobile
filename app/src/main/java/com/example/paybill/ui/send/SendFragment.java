package com.example.paybill.ui.send;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.paybill.Charge.Charge;
import com.example.paybill.Charge.ChargeInterface;
import com.example.paybill.Charge.ChargeRetrofit;
import com.example.paybill.Credit.Credit;
import com.example.paybill.Credit.CreditInterface;
import com.example.paybill.Credit.CreditRetrofit;
import com.example.paybill.R;
import com.example.paybill.SimpleScannerActivity;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.paybill.Utils.UserDetailData.userAccountID;
import static com.example.paybill.Utils.UserDetailData.userExternalKey;
import static com.example.paybill.Utils.UserDetailData.username;

public class SendFragment extends Fragment{
    private static final int ZXING_CAMERA_PERMISSION = 1;
    //private ZXingScannerView zXingScannerView;
    public static EditText etReceiverAccount;
    private EditText etAmount;
    private EditText etDescription;
    private ImageButton scanQr;
    private Button buttonSend;
    private static final String TAG = "SendFragment";
    private SendViewModel sendViewModel;
    private Dialog customDialog;
    private FirebaseFirestore db;

    ProgressDialog pd;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewModelProviders.of(this).get(SendViewModel.class);
        View view = inflater.inflate(R.layout.fragment_sendbill, container, false);
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        etReceiverAccount = view.findViewById(R.id.et_receiver_account);
        etAmount = view.findViewById(R.id.et_amount);
        etDescription = view.findViewById(R.id.et_description);
        scanQr = view.findViewById(R.id.qr_scan);
        pd= new ProgressDialog(getContext());

        buttonSend = view.findViewById(R.id.button_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etReceiverAccount.getText().toString().trim().isEmpty()||etAmount.getText().toString().trim().isEmpty()||
                        etDescription.getText().toString().trim().isEmpty())
                {
                    Log.d(TAG, "onClick: Send");
                    Toast.makeText(getContext(), "Please fill information", Toast.LENGTH_SHORT).show();
                    return;
                }
                confirmationDialog();
                /*pd.setMessage("Sending....");
                pd.show();
                createCreditOnOtherAccount();*/
            }
        });

        scanQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
                } else {
                    Intent intent = new Intent(getContext(), SimpleScannerActivity.class);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    private void confirmationDialog(){

        customDialog = new Dialog(getContext());
        customDialog.setContentView(R.layout.custom_dialog_fragment);

        Window window = customDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        customDialog.setCanceledOnTouchOutside(false);

        TextView tvAccountId = window.findViewById(R.id.tv_account_id);
        TextView tvAmount = window.findViewById(R.id.tv_amount);
        TextView tvDescription = window.findViewById(R.id.tv_description);
        Button btnYes = window.findViewById(R.id.btn_yes);
        Button btnNo = window.findViewById(R.id.btn_no);

        tvAccountId.setText(etReceiverAccount.getText().toString());
        tvAmount.setText(etAmount.getText().toString());
        tvDescription.setText(etDescription.getText().toString());

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Sending....");
                pd.show();
                createChargeOnCurrentUserAccount();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
            }
        });

        customDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(SimpleScannerActivity.class != null) {
                        Intent intent = new Intent(getContext(), SimpleScannerActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getContext(), "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    private void createChargeOnCurrentUserAccount(){
        ChargeInterface chargeInterface = ChargeRetrofit.getUser().create(ChargeInterface.class);
        List<Charge> chargeList = new ArrayList<>();
        Charge charge = new Charge(
                etDescription.getText().toString().trim(),
                Integer.valueOf(etAmount.getText().toString().trim()));
        chargeList.add(charge);
        Call<List<Charge>> chargeCall = chargeInterface.createCharge(userAccountID,false, chargeList);
        chargeCall.enqueue(new Callback<List<Charge>>() {
            @Override
            public void onResponse(Call<List<Charge>> call, Response<List<Charge>> response) {
                if(!response.isSuccessful()){
                    Log.d(TAG, "onResponse: "+response.code());
                    Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                    customDialog.dismiss();
                    return;
                }
                List<Charge> chargeList1 = response.body();
                final String mRecieverId = etReceiverAccount.getText().toString().trim();
                final String mDescription = etDescription.getText().toString().trim();
                final String mInvoiceId = chargeList1.get(0).invoiceId;
                final int mAmount = chargeList1.get(0).amount;
                /*Firebase reference = new Firebase("https://paybill-58d29.firebaseio.com/mails");
                reference.child(mRecieverId).child(mInvoiceId).child("description").setValue(mDescription);
                reference.child(mRecieverId).child(mInvoiceId).child("amount").setValue(mAmount+"");*/
                Map<String, Object> mailData = new HashMap<>();
                mailData.put("senderId", userAccountID);
                mailData.put("description",mDescription);
                mailData.put("amount",mAmount);
                db.collection(mRecieverId).document(mInvoiceId).set(mailData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                successFun();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                Toast.makeText(getContext(), "Successfully Sent", Toast.LENGTH_SHORT).show();
                //Log.d(TAG, "onResponse: ");
            }

            @Override
            public void onFailure(Call<List<Charge>> call, Throwable t) {
                Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                successFun();
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }
    private void successFun(){
        etReceiverAccount.setText("");
        etDescription.setText("");
        etAmount.setText("");
        pd.dismiss();
        customDialog.dismiss();
    }

}