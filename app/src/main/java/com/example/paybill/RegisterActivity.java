package com.example.paybill;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.paybill.CreateAccount.Create;
import com.example.paybill.CreateAccount.CreateHttp;
import com.example.paybill.CreateAccount.CreatePhp;
import com.example.paybill.CreateAccount.CreateRetrofit;
import com.example.paybill.CreateAccount.CreateRetrofitPhp;
import com.example.paybill.CreateAccount.PhpObject;
import com.example.paybill.CreateAccount.User;
import com.example.paybill.CreateAccount.UserCreateInterface;
import com.example.paybill.ImageUpload.ImageData;
import com.example.paybill.ImageUpload.ImageRetrofit;
import com.example.paybill.ImageUpload.ImageUploadInterface;
import com.example.paybill.Retrieve.UserData;
import com.example.paybill.Retrieve.UserGet;
import com.example.paybill.Retrieve.UserRetrofit;
import com.example.paybill.Utils.UserDetailData;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.paybill.Utils.UserDetailData.location;
import static com.example.paybill.Utils.UserDetailData.password;

import static com.example.paybill.Utils.UserDetailData.userAccountID;
import static com.example.paybill.Utils.UserDetailData.userExternalKey;
import static com.example.paybill.Utils.UserDetailData.username;

public class RegisterActivity extends AppCompatActivity {
//    UserCreateInterface userCreateInterface;
//    FirebaseDatabase database;
//    DatabaseReference myRef;
private static final int CAMERA_REQUEST = 1888;
    int i=0;
    Map map;String myimage="";
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    EditText etEmail, etAddress, etCity, etState;
    Button btnRegister;

    ProgressDialog pd;

    ImageView front,back;String frontimg,backimg;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
        pd = new ProgressDialog(RegisterActivity.this);
        etEmail = findViewById(R.id.et_email);
        etAddress = findViewById(R.id.et_address);
        etCity = findViewById(R.id.et_city);
        etState = findViewById(R.id.et_state);
        btnRegister = findViewById(R.id.btn_login);
        front=(ImageView)findViewById(R.id.front_img);
        back=(ImageView)findViewById(R.id.back_img);
        Firebase.setAndroidContext(this);
        map=new HashMap();
        front.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //front.setImageResource(R.drawable.uns11);
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
                front.setClickable(false);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
                back.setClickable(false);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Registering....");
                pd.show();
                registerUser();
                /*if(registerUser())
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            Intent intent=new Intent(getApplicationContext(),WalletActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 2000);
                }*/
            }
        });
    }

    private void registerUser(){
        if(etEmail.getText().toString().trim().isEmpty() || etAddress.getText().toString().trim().isEmpty()
                || etCity.getText().toString().trim().isEmpty() || etState.getText().toString().trim().isEmpty()){
            Toast.makeText(RegisterActivity.this, "Please fill information", Toast.LENGTH_SHORT).show();
            return;
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
                pd.dismiss();
                Toast.makeText(RegisterActivity.this, "Registration Fail", Toast.LENGTH_SHORT).show();
            }
        });
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
                    reference.child(username).child("isAgent").setValue(false);
                    reference.child(username).child("status").setValue(1);
                    UserDetailData.email_address+=email;
                    UserDetailData.address+=address;
                    UserDetailData.city+=city;
                    UserDetailData.state+=state;
                    Log.d(TAG, "UserAccountID: " + userAccountID + "  UserExternalKey:  " + userExternalKey);

//                    createAccountOnPhp();
                    UploadImage();
                    //Toast.makeText(getContext(), ""+userDataList.get(0).getExternalKey(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<UserData> call, Throwable t) {
                    pd.dismiss();
                    Toast.makeText(RegisterActivity.this, "Registration Fail", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });
        }
    }

    private void createAccountOnPhp(){
        //CreateRetrofitPhp createRetrofitPhp = CreatePhp.createClient().create(CreateRetrofitPhp.class);
        CreateRetrofitPhp createRetrofitPhp = CreatePhp.createClient().create(CreateRetrofitPhp.class);

        PhpObject phpObject = new PhpObject(userAccountID,username, UserDetailData.phnodata,etEmail.getText().toString(),
                etAddress.getText().toString(), etCity.getText().toString(), etState.getText().toString(), password);
        //Log.d("Php Data",""+username+" * "+password+" * "+etEmail.getText().toString());
        Log.d(TAG, "createAccountOnPhp: "+userAccountID+" * "+username+" * "+
                UserDetailData.phnodata+" * "+etEmail.getText().toString()
                +" * "+etAddress.getText().toString()+" * "+etCity.getText().toString()+" * "+etState.getText().toString()
                +" * "+password);
        Call<PhpObject> phpObjectCall = createRetrofitPhp.createAccountPhp(phpObject);
        phpObjectCall.enqueue(new Callback<PhpObject>() {
            @Override
            public void onResponse(Call<PhpObject> call, Response<PhpObject> response) {
//                PhpObject phpObject1=response.body();
            UploadImage();
          Log.d(TAG, "onResponse: Php Success"+response.code() );
                Toast.makeText(RegisterActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PhpObject> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(RegisterActivity.this, "Registration Fail", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (i<2){
            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK ) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
                String imgdata= Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
                myimage=imgdata;
                Log.d("ImageData",""+imgdata);
                i+=1;
                map.put(i,imgdata);

                Set set=map.entrySet();//Converting to Set so that we can traverse
                Iterator itr=set.iterator();
                while(itr.hasNext()) {
                    //Converting to Map.Entry so that we can get key and value separately
                    Map.Entry entry = (Map.Entry) itr.next();
                    if (entry.getKey().toString().equals("1")) {
                        byte[] decodedString = Base64.decode(entry.getValue() + "", Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        front.setImageBitmap(bitmap
                        );
                        frontimg=entry.getValue()+"";
                    }
                    else if (entry.getKey().toString().equals("2")){
                        byte[] decodedString = Base64.decode(entry.getValue() + "", Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        back.setImageBitmap(bitmap
                        );
                        backimg=entry.getValue()+"";
                    }
                    Log.d("ImgAll",entry.getKey()+""+entry.getValue());
                }

            }
           // Toast.makeText(this, "Ok"+i, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Limited"+i, Toast.LENGTH_SHORT).show();
        }

    }
    private void UploadImage(){
        ImageUploadInterface imageUploadInterface= ImageRetrofit.uploadImage().create(ImageUploadInterface.class);
        RequestBody appssid= RequestBody.create(MediaType.parse("multipart/form-data"), userAccountID);
        RequestBody usernamereq= RequestBody.create(MediaType.parse("multipart/form-data"), UserDetailData.username);
        RequestBody email= RequestBody.create(MediaType.parse("multipart/form-data"), UserDetailData.email_address);
        RequestBody address= RequestBody.create(MediaType.parse("multipart/form-data"), UserDetailData.address);
        RequestBody township= RequestBody.create(MediaType.parse("multipart/form-data"), UserDetailData.city);
        RequestBody state= RequestBody.create(MediaType.parse("multipart/form-data"), UserDetailData.state);
        RequestBody phno= RequestBody.create(MediaType.parse("multipart/form-data"), UserDetailData.phnodata);
        RequestBody passwrod= RequestBody.create(MediaType.parse("multipart/form-data"), password);
        RequestBody frontreq= RequestBody.create(MediaType.parse("multipart/form-data"), frontimg);
        RequestBody backreq= RequestBody.create(MediaType.parse("multipart/form-data"), backimg);

        Call<ImageData> imageDataCall=imageUploadInterface.uploadImageData
                (appssid,usernamereq,email,address,township,state,phno,passwrod,frontreq,backreq
        );
        Log.d("RegActi ImageUploadData",frontreq+"*"+backreq);
        imageDataCall.enqueue(new Callback<ImageData>() {
            @Override
            public void onResponse(Call<ImageData> call, Response<ImageData> response) {
                ImageData imageData=response.body();
//                UserDetailData.memberid=imageData.getMember_id();
//                Firebase reference = new Firebase("https://paybill-58d29.firebaseio.com/users");
//                reference.child(username).child("member_id").setValue(UserDetailData.memberid);
                Toast.makeText(RegisterActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                Intent intent=new Intent(getApplicationContext(),WalletActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onFailure(Call<ImageData> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(RegisterActivity.this, "Registration Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
