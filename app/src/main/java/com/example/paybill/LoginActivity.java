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
import com.example.paybill.Utils.ConfigUtils;
import com.example.paybill.Utils.Helper;
import com.example.paybill.Utils.UserDetailData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

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
                    } else {
                        try {
                            JSONObject obj = new JSONObject(s);

                            if (!obj.has(user)) {
                                Toast.makeText(LoginActivity.this, "user not found", Toast.LENGTH_LONG).show();
                                pd.dismiss();
                            } else if (obj.getJSONObject(user).getString("password").equals(pass)) {

                                username = user;
                                password = pass;
                                //Log.d("Before Have ID", "onResponse: Before Have ID"+username);
                                haveID();
                            } else {
                                pd.dismiss();
                                Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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
    private void haveID() {
        Firebase reference = new Firebase("https://paybill-58d29.firebaseio.com/users/" + username + "/acc_id/");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Data Comming", "" + dataSnapshot.getValue());
                if (dataSnapshot.getValue() != null) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    userAccountID = dataSnapshot.getValue().toString();
                    startActivity(new Intent(LoginActivity.this, WalletActivity.class));
                    pd.dismiss();
                    etUsername.setText("");
                    etPassword.setText("");
                    finish();
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
                } else {
                    Log.d("Phone","Error Phno");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase email_add = new Firebase("https://paybill-58d29.firebaseio.com/users/" + username + "/email/");
        email_add.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Data Comming", "" + dataSnapshot.getValue());
                if (dataSnapshot.getValue() != null) {
                    Log.d("Email Data",""+dataSnapshot.getValue().toString());
                    UserDetailData.email_address+=dataSnapshot.getValue().toString();
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
                    UserDetailData.address+=dataSnapshot.getValue().toString();
                } else {
                    Log.d("Address","Error address");
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
                    UserDetailData.city+=dataSnapshot.getValue().toString();
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
                    UserDetailData.state+=dataSnapshot.getValue().toString();
                } else {
                    Log.d("State","Error State");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
