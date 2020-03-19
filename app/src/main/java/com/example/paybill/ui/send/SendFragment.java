package com.example.paybill.ui.send;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import com.example.paybill.ChargePhp.ChargeInvoice;
import com.example.paybill.ChargePhp.InvoiceInterface;
import com.example.paybill.ChargePhp.InvoiceForNumber;
import com.example.paybill.ChargePhp.Charge_Of_Php;
import com.example.paybill.ChargePhp.Transaction;
import com.example.paybill.ChargePhp.TransactionInterface;
import com.example.paybill.R;
import com.example.paybill.SimpleScannerActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.paybill.Utils.UserDetailData.userAccountID;

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
        final List<Charge> chargeList = new ArrayList<>();
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
                final String date=chargeList1.get(0).startDate;
                /*Firebase reference = new Firebase("https://paybill-58d29.firebaseio.com/mails");
                reference.child(mRecieverId).child(mInvoiceId).child("description").setValue(mDescription);
                reference.child(mRecieverId).child(mInvoiceId).child("amount").setValue(mAmount+"");*/
                Map<String, Object> mailData = new HashMap<>();
                mailData.put("description",mDescription);
                mailData.put("amount",mAmount);
                mailData.put("senderId", userAccountID);
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
                getInvoiceNum(mInvoiceId,mAmount,date,mDescription);
                //Log.d("ChargePhp",""+ userAccountID+"*"+mInvoiceId+"*"+mAmount+"*"+date);
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

    private void getInvoiceNum(final String invoiceId, final int amt, final String date, final String desc){
        InvoiceInterface invoiceNumber= ChargeInvoice.getInvoice().create(InvoiceInterface.class);
        Call<InvoiceForNumber> callNumber=invoiceNumber.createCharge(invoiceId,true);
        callNumber.enqueue(new Callback<InvoiceForNumber>() {
            @Override
            public void onResponse(Call<InvoiceForNumber> call, Response<InvoiceForNumber> response) {
                InvoiceForNumber invoiceForNumber=response.body();
                setUpChargePhp(invoiceId,invoiceForNumber.getInvoiceNumber(),amt+"",date,desc);
                Log.d("Invoice Number",""+invoiceForNumber.getInvoiceNumber());
            }

            @Override
            public void onFailure(Call<InvoiceForNumber> call, Throwable t) {
                Log.d("Invoice Number",""+t.getMessage());
            }
        });
    }

    private void setUpChargePhp(String invoice_id,String invoicenum,String amt,String date,String desc){
        TransactionInterface mytransinter = Charge_Of_Php.createCharge().create(TransactionInterface.class);
        RequestBody option= RequestBody.create(MediaType.parse("multipart/form-data"), "D");
        RequestBody status= RequestBody.create(MediaType.parse("multipart/form-data"), "COMPLETE");
        RequestBody accid= RequestBody.create(MediaType.parse("multipart/form-data"), userAccountID);
        RequestBody invoiceid= RequestBody.create(MediaType.parse("multipart/form-data"), invoice_id);
        RequestBody invoiceno= RequestBody.create(MediaType.parse("multipart/form-data"), invoicenum);
        RequestBody amnt= RequestBody.create(MediaType.parse("multipart/form-data"), amt);
        RequestBody date_data= RequestBody.create(MediaType.parse("multipart/form-data"), date);
        RequestBody description= RequestBody.create(MediaType.parse("multipart/form-data"), desc);
        Call<Transaction> callTrans=mytransinter.createChargePhp(option, accid,invoiceid,
        invoiceno,amnt,status,date_data,date_data,description
        );
        Log.d("PhpChargeData",""+ userAccountID+"*"+invoice_id+"*"+invoicenum+"*"+amt+"*"+date+"*"+desc);
        callTrans.enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                Transaction transaction=response.body();
                Log.d("ChargePhp",""+transaction.getStatus());
            }

            @Override
            public void onFailure(Call<Transaction> call, Throwable t) {
                Log.d("ChargePhp ",""+t.getMessage());
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