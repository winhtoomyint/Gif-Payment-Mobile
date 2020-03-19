package com.example.paybill;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.paybill.ProfileCall.ProfileData;
import com.example.paybill.ProfileCall.ProfileInterface;
import com.example.paybill.ProfileCall.ProfileRetrofit;
import com.example.paybill.Utils.ConfigUtils;
import com.example.paybill.Utils.Helper;
import com.example.paybill.Utils.UserDetailData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

import static com.example.paybill.Utils.UserDetailData.username;
import static com.example.paybill.Utils.UserDetailData.password;
import static com.example.paybill.Utils.UserDetailData.userAccountID;


public class LoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button loginButton;
    TextView register;
    ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.changeLocation(this, ConfigUtils.getInstance(this).loadLocale());
        setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.username_et);
        etPassword = findViewById(R.id.password_et);
        loginButton = findViewById(R.id.btn_login);
        register =  findViewById(R.id.register_txt);
        pd = new ProgressDialog(LoginActivity.this);
        pd.setCancelable(false);
        Firebase.setAndroidContext(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MainActiviy = register
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
    }

    private void doLogin() {
        final String user, pass;
        user = etUsername.getText().toString().trim();
        pass = etPassword.getText().toString().trim();
        if (user.equals("")) {
            etUsername.setError("can't be blank");
        } else if (pass.equals("")) {
            etPassword.setError("can't be blank");
        } else {
            String url = "https://paybill-58d29.firebaseio.com/users.json";
            pd.setMessage("Loading...");
            pd.show();
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if (s.equals("null")) {
                        Toast.makeText(LoginActivity.this, "user not found", Toast.LENGTH_LONG).show();
                    }
                    else {
                        try {
                            JSONObject obj = new JSONObject(s);
                            if (!obj.has(user)) {
                                Toast.makeText(LoginActivity.this, "user not found", Toast.LENGTH_LONG).show();
                                pd.dismiss();
                            } else if (obj.getJSONObject(user).getString("password").equals(pass)) {
                                username = user;
                                password = pass;
                                //Log.d("Before Have ID", "onResponse: Before Have ID"+username);
                                haveStatus();
                                //haveID();
                            } else {
                                pd.dismiss();
                                Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }}
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    System.out.println("" + volleyError);
                    pd.dismiss();
                }
            });

            RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
            rQueue.add(request);
        }
    }

    private void haveStatus(){
        Firebase referenceStatus = new Firebase("https://paybill-58d29.firebaseio.com/users/" + username + "/status/");
        referenceStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Data Comming", "" + dataSnapshot.getValue());
                if (dataSnapshot.getValue() != null) {
                    UserDetailData.status = Integer.valueOf(dataSnapshot.getValue().toString());
                    if(UserDetailData.status == 0){
                        pd.dismiss();
                        Toast.makeText(LoginActivity.this, "Account deactivated, can't login", Toast.LENGTH_SHORT).show();
                    }else{
                        haveID();
                    }
                } else {
                    pd.dismiss();
                    Toast.makeText(LoginActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                pd.dismiss();
            }
        });
    }

    private void haveID() {
        final Firebase reference = new Firebase("https://paybill-58d29.firebaseio.com/users/" + username + "/acc_id/");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Data Comming", "" + dataSnapshot.getValue());
                if (dataSnapshot.getValue() != null) {
                    userAccountID = dataSnapshot.getValue().toString();
                    ProfileInterface myProfile = ProfileRetrofit.getProfile().create(ProfileInterface.class);
                    Call<ProfileData> profileDataCall = myProfile.getProfileData(UserDetailData.userAccountID);
                    profileDataCall.enqueue(new Callback<ProfileData>() {
                        @Override
                        public void onResponse(Call<ProfileData> call, retrofit2.Response<ProfileData> response) {
                            ProfileData profileData = response.body();
                            etUsername.setText("");
                            etPassword.setText("");
                            startActivity(WalletActivity.newIntent(LoginActivity.this, profileData.nrcPhotos.get(0).getMemberRole()));
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                            finish();
                        }
                        @Override
                        public void onFailure(Call<ProfileData> call, Throwable t) {
                            pd.dismiss();
                            Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase reference1 = new Firebase("https://paybill-58d29.firebaseio.com/users/" + username + "/phone_no/");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Data Comming", "" + dataSnapshot.getValue());
                if (dataSnapshot.getValue() != null) {
                    Log.d("Phone Data",""+dataSnapshot.getValue().toString());
                    UserDetailData.phone=dataSnapshot.getValue().toString();
                    UserDetailData.phnodata=dataSnapshot.getValue().toString();
                } else {
                    Log.d("Phone","Error Phno");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        /*Firebase isAgent = new Firebase("https://paybill-58d29.firebaseio.com/users/" + username + "/isAgent/");
        isAgent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Data Comming", "" + dataSnapshot.getValue());
                if (dataSnapshot.getValue() != null) {
                    //Log.d("Email Data",""+dataSnapshot.getValue().toString());
                    UserDetailData.isAgent = Boolean.getBoolean(dataSnapshot.getValue().toString());
                } else {
                    Log.d("emailErr","Error email");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/

        Firebase email_add = new Firebase("https://paybill-58d29.firebaseio.com/users/" + username + "/email/");
        email_add.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Data Comming", "" + dataSnapshot.getValue());
                if (dataSnapshot.getValue() != null) {
                    Log.d("Email Data",""+dataSnapshot.getValue().toString());
                    UserDetailData.email_address=dataSnapshot.getValue().toString();
                } else {
                    Log.d("emailErr","Error email");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase address = new Firebase("https://paybill-58d29.firebaseio.com/users/" + username + "/address/");
        address.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Data Comming", "" + dataSnapshot.getValue());
                if (dataSnapshot.getValue() != null) {
                    Log.d("Address Data",""+dataSnapshot.getValue().toString());
                    UserDetailData.address=dataSnapshot.getValue().toString();
                } else {
                    Log.d("Address","Error address");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase nrcNumber = new Firebase("https://paybill-58d29.firebaseio.com/users/" + username + "/nrc_number/");
        nrcNumber.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Data Comming", "" + dataSnapshot.getValue());
                if (dataSnapshot.getValue() != null) {
                    Log.d("NrcNumber Data",""+dataSnapshot.getValue().toString());
                    UserDetailData.nrcNumber = dataSnapshot.getValue().toString();
                } else {
                    Log.d("Nrc","Error Nrc");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase city = new Firebase("https://paybill-58d29.firebaseio.com/users/" + username + "/city/");
        city.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Data Comming", "" + dataSnapshot.getValue());
                if (dataSnapshot.getValue() != null) {
                    Log.d("city Data",""+dataSnapshot.getValue().toString());
                    UserDetailData.city=dataSnapshot.getValue().toString();
                } else {
                    Log.d("City","Error City");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        Firebase state = new Firebase("https://paybill-58d29.firebaseio.com/users/" + username + "/state/");
        state.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Data Comming", "" + dataSnapshot.getValue());
                if (dataSnapshot.getValue() != null) {
                    Log.d("State Data",""+dataSnapshot.getValue().toString());
                    UserDetailData.state=dataSnapshot.getValue().toString();
                } else {
                    Log.d("State","Error State");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        Firebase member_id = new Firebase("https://paybill-58d29.firebaseio.com/users/" + username + "/member_id/");
        member_id.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Data Comming", "" + dataSnapshot.getValue());
                if (dataSnapshot.getValue() != null) {
                    Log.d("Member Data",""+dataSnapshot.getValue().toString());
                    UserDetailData.memberid=dataSnapshot.getValue().toString();
                } else {
                    Log.d("Member","Error State");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
