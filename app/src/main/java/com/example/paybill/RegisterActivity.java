package com.example.paybill;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.paybill.CreateAccount.Create;
import com.example.paybill.CreateAccount.CreateHttp;
import com.example.paybill.CreateAccount.CreatePhp;
import com.example.paybill.CreateAccount.CreateRetrofit;
import com.example.paybill.CreateAccount.CreateRetrofitPhp;
import com.example.paybill.CreateAccount.PhpObject;
import com.example.paybill.CreateAccount.UserCreateInterface;
import com.example.paybill.Retrieve.UserData;
import com.example.paybill.Retrieve.UserGet;
import com.example.paybill.Retrieve.UserRetrofit;
import com.example.paybill.Utils.UserDetailData;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.paybill.Utils.UserDetailData.location;
import static com.example.paybill.Utils.UserDetailData.password;
import static com.example.paybill.Utils.UserDetailData.phone;
import static com.example.paybill.Utils.UserDetailData.userAccountID;
import static com.example.paybill.Utils.UserDetailData.userExternalKey;
import static com.example.paybill.Utils.UserDetailData.username;

public class RegisterActivity extends AppCompatActivity {
//    UserCreateInterface userCreateInterface;
//    FirebaseDatabase database;
//    DatabaseReference myRef;
    EditText etEmail, etAddress, etCity, etState;
    Button btnRegister;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
        etEmail = findViewById(R.id.et_email);
        etAddress = findViewById(R.id.et_address);
        etCity = findViewById(R.id.et_city);
        etState = findViewById(R.id.et_state);
        btnRegister = findViewById(R.id.btn_login);
        final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(registerUser())
                {
                    pd.setMessage("Registering....");
                    pd.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent=new Intent(getApplicationContext(),WalletActivity.class);
                            pd.dismiss();
                            startActivity(intent);
                            finish();
                        }
                    }, 2000);
                }
            }
        });
    }

    private boolean registerUser(){
        if(etEmail.getText().toString().trim().isEmpty() || etAddress.getText().toString().trim().isEmpty()
                || etCity.getText().toString().trim().isEmpty() || etState.getText().toString().trim().isEmpty()){
            Toast.makeText(RegisterActivity.this, "Please fill information", Toast.LENGTH_SHORT).show();
            return false;
        }
        Create create = new Create(UserDetailData.username,etEmail.getText().toString().trim(),etAddress.getText().toString().trim()
                ,etCity.getText().toString().trim(),etState.getText().toString().trim());
        CreateRetrofit createRetrofit = CreateHttp.createClient().create(CreateRetrofit.class);
        Call<Void> call = createRetrofit.createAccount(create);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                location = response.headers().get("Location");
                uploadUserInfo();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    private void uploadUserInfo(){
        if(location != null) {
            UserGet userGet = UserRetrofit.getCLient().create(UserGet.class);
            Call<UserData> detailData = userGet.getUserDetail(location);
            detailData.enqueue(new Callback<UserData>() {
                @Override
                public void onResponse(Call<UserData> call, Response<UserData> response) {
                    //Toast.makeText(getContext(), ""+response.code(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onResponse: " + response.code());
                    userAccountID = response.body().getAccountId();
                    userExternalKey = response.body().getExternalKey();
                    Firebase reference = new Firebase("https://paybill-58d29.firebaseio.com/users");
                    reference.child(username).child("acc_id").setValue(userAccountID);
                    reference.child(username).child("ex_key").setValue(userExternalKey);
                    String email=etEmail.getText().toString();
                    String address=etAddress.getText().toString();
                    String city=etCity.getText().toString();
                    String state=etState.getText().toString();
                    reference.child(username).child("email").setValue(email);
                    reference.child(username).child("address").setValue(address);
                    reference.child(username).child("city").setValue(city);
                    reference.child(username).child("state").setValue(state);
                    UserDetailData.email_address+=email;
                    UserDetailData.address+=address;
                    UserDetailData.city+=city;
                    UserDetailData.state+=state;
                    Log.d(TAG, "UserAccountID: " + userAccountID + "  UserExternalKey:  " + userExternalKey);

                    createAccountOnPhp();
                    //Toast.makeText(getContext(), ""+userDataList.get(0).getExternalKey(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<UserData> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Fail to register", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });
        }
    }

    private void createAccountOnPhp(){
        //CreateRetrofitPhp createRetrofitPhp = CreatePhp.createClient().create(CreateRetrofitPhp.class);
        CreateRetrofitPhp createRetrofitPhp = CreatePhp.createClient().create(CreateRetrofitPhp.class);
        PhpObject phpObject = new PhpObject(userAccountID,username,"123678904",etEmail.getText().toString(),
                etAddress.getText().toString(), etCity.getText().toString(), etState.getText().toString(), password);
        //Log.d("Php Data",""+username+" * "+password+" * "+etEmail.getText().toString());
        Log.d(TAG, "createAccountOnPhp: "+userAccountID+" * "+username+" * "+phone+" * "+etEmail.getText().toString()
                +" * "+etAddress.getText().toString()+" * "+etCity.getText().toString()+" * "+etState.getText().toString()
                +" * "+password);
        Call<PhpObject> phpObjectCall = createRetrofitPhp.createAccountPhp(phpObject);
        phpObjectCall.enqueue(new Callback<PhpObject>() {
            @Override
            public void onResponse(Call<PhpObject> call, Response<PhpObject> response) {
                PhpObject phpObject1=response.body();
                Log.d(TAG, "onResponse: Php Success"+response.code()+" "+phpObject1.getStatus());
                Toast.makeText(RegisterActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PhpObject> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Fail to register", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }
}
