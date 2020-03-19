package com.example.paybill;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.paybill.Utils.ConfigUtils;
import com.example.paybill.Utils.Helper;
import com.example.paybill.Utils.UserDetailData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.TimeUnit;

import static com.example.paybill.Utils.UserDetailData.password;
import static com.example.paybill.Utils.UserDetailData.username;

public class MainActivity extends AppCompatActivity {
    EditText  editTextCode, name_field, pass_field;
    public EditText editTextPhone;
    private String verificationId;
    FirebaseAuth mAuth;PhoneAuthProvider mPhoneAuthProvider;
    DatabaseReference myRef;
    String user, pass,phone;
    String codeSent;
    Button getverify_code;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.changeLocation(this, ConfigUtils.getInstance(this).loadLocale());
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("register");
        Firebase.setAndroidContext(this);
        mAuth = FirebaseAuth.getInstance();
        mPhoneAuthProvider = PhoneAuthProvider.getInstance();
        editTextCode = findViewById(R.id.editTextCode);
        editTextPhone = findViewById(R.id.editTextPhone);
        phone=editTextPhone.getText().toString();
        getverify_code=(Button)findViewById(R.id.buttonGetVerificationCode);
//      UserDetailData.phone+=editTextPhone.getText().toString();
        name_field = findViewById(R.id.et_name);
        pass_field =  findViewById(R.id.et_pass);
        findViewById(R.id.register_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
       getverify_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendVerificationCode();
                getverify_code.setClickable(false);
                Toast.makeText(MainActivity.this, "Please Wait OTP Code ", Toast.LENGTH_SHORT).show();

            }
        });


        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySignInCode(editTextCode.getText().toString().trim());
            }
        });
    }

    private void verifySignInCode(String code) {
        try {
            UserDetailData.phnodata=editTextPhone.getText().toString();
            //Log.d("MyPhone",""+UserDetailData.phone);
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneAuthCredential(credential);
        } catch (Exception e) {
            Toast.makeText(this, "Please Enter Verification Code", Toast.LENGTH_SHORT).show();
        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //here you can open new activity
                            //doLogin();
                            uploadToFirebase();
//                            Toast.makeText(getApplicationContext(),
//                                    "Login Successfull", Toast.LENGTH_LONG).show();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),
                                        "Incorrect Verification Code ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void uploadToFirebase(){
        user = name_field.getText().toString().trim();
        pass = pass_field.getText().toString().trim();
        if(user.equals("")){
            name_field.setError("can't be blank");
        }
        else if(pass.equals("")){
            pass_field.setError("can't be blank");
        }
        else if(user.length()<3){
            name_field.setError("at least 3 characters long");
        }
        else if(pass.length()<5){
            pass_field.setError("at least 5 characters long");
        }
        else {
            final ProgressDialog pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Loading...");
            pd.show();

            String url = "https://paybill-58d29.firebaseio.com/users.json";

            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                @Override
                public void onResponse(String s) {
                    Firebase reference = new Firebase("https://paybill-58d29.firebaseio.com/users");

                    if(s.equals("null")) {
                        reference.child(user).child("password").setValue(pass);
                        reference.child(user).child("phone_no").setValue(editTextPhone.getText().toString().trim());
                        username = user;
                        password = pass;
                        UserDetailData.phnodata=editTextPhone.getText().toString();
                        UserDetailData.phone+=phone;
                        Log.d("MyPhone",""+UserDetailData.phone);
                        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                        Toast.makeText(MainActivity.this, "SignUp successful", Toast.LENGTH_LONG).show();
                    }
                    else {
                        try {
                            JSONObject obj = new JSONObject(s);
                            if (!obj.has(user)) {
                                reference.child(user).child("password").setValue(pass);
                                reference.child(user).child("phone_no").setValue(editTextPhone.getText().toString().trim());
                                username = user;
                                password = pass;
                                UserDetailData.phnodata=editTextPhone.getText().toString();
                                UserDetailData.phone+=phone;
                                Log.d("MyPhone",""+UserDetailData.phone);
                                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                                Toast.makeText(MainActivity.this, "registration successful", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Username already exists", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    pd.dismiss();
                    name_field.setText("");
                    pass_field.setText("");
                    editTextPhone.setText("");
                    editTextCode.setText("");
                }

            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    System.out.println("" + volleyError );
                    pd.dismiss();
                }
            });

            RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
            rQueue.add(request);
        }
    }


    private void sendVerificationCode(){

        String phone ="+959"+editTextPhone.getText().toString();
       // Toast.makeText(this, "Getting OTP", Toast.LENGTH_SHORT).show();
        if(phone.isEmpty()){
            editTextPhone.setError("Phone number is required");
            editTextPhone.requestFocus();
            return;
        }

        if(phone.length() < 10 ){
            editTextPhone.setError("Please enter a valid phone");
            editTextPhone.requestFocus();
            return;
        }
//        mPhoneAuthProvider.verifyPhoneNumber(phone,30,TimeUnit.SECONDS,this,mCallbacks);
//
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }



    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code != null){
                editTextCode.setText(code);
                //verify
                verifySignInCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
