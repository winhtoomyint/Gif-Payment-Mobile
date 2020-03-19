package com.example.paybill.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.paybill.ProfileCall.NrcPhoto;
import com.example.paybill.ProfileCall.ProfileData;
import com.example.paybill.ProfileCall.ProfileInterface;
import com.example.paybill.ProfileCall.ProfileRetrofit;
import com.example.paybill.R;
import com.example.paybill.Utils.UserDetailData;
import com.firebase.client.Firebase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.paybill.Utils.UserDetailData.nrcNumber;
import static com.example.paybill.Utils.UserDetailData.username;

public class GalleryFragment extends Fragment {
     String front_img,back_img;
//    TextView acc_id,name,phone,email,address,state,city;
    ImageView frontimage,backimage;TextView acc_id;
    EditText name,phone,email,address,state,city,etNrc;

    private ImageView star1,star2,star3,star4,star5;

    Button save_btn;
    List<NrcPhoto> myprofiledata = new ArrayList<>();
    private Integer profileStar = null;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery1, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        acc_id=(TextView)view.findViewById(R.id.tv_account_number);
        name=(EditText) view.findViewById(R.id.et_username);
        phone=(EditText)view.findViewById(R.id.et_phone);
        email=(EditText)view.findViewById(R.id.et_email);
        address=(EditText)view.findViewById(R.id.et_uaddress);
        state=(EditText)view.findViewById(R.id.et_state);
        city=(EditText)view.findViewById(R.id.et_city);
        frontimage=(ImageView)view.findViewById(R.id.iv_nrc_front);
        backimage=(ImageView)view.findViewById(R.id.iv_nrc_back);
        save_btn=(Button)view.findViewById(R.id.save_btn);
        etNrc = view.findViewById(R.id.et_nrc_number);

        star1 = view.findViewById(R.id.iv_star_1);
        star2 = view.findViewById(R.id.iv_star_2);
        star3 = view.findViewById(R.id.iv_star_3);
        star4 = view.findViewById(R.id.iv_star_4);
        star5 = view.findViewById(R.id.iv_star_5);

        setProfile();

        acc_id.setText(""+ UserDetailData.userAccountID);
        name.setText(""+ UserDetailData.username);
        phone.setText(""+ UserDetailData.phnodata);
        email.setText(""+ UserDetailData.email_address);
        address.setText(""+ UserDetailData.address);
        state.setText(""+ UserDetailData.state);
        city.setText(""+ UserDetailData.city);
        etNrc.setText(""+ nrcNumber);

        name.setEnabled(false);
        phone.setEnabled(false);
        email.setEnabled(false);
        address.setEnabled(false);
        state.setEnabled(false);
        city.setEnabled(false);
        etNrc.setEnabled(false);

        view.findViewById(R.id.edit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phone.setEnabled(true);
                email.setEnabled(true);
                address.setEnabled(true);
                state.setEnabled(true);
                city.setEnabled(true);
                etNrc.setEnabled(true);
                save_btn.setVisibility(View.VISIBLE);
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase reference = new Firebase("https://paybill-58d29.firebaseio.com/users");
                reference.child(username).child("address").setValue(address.getText().toString());
                reference.child(username).child("city").setValue(city.getText().toString());
                reference.child(username).child("email").setValue(email.getText().toString());
                reference.child(username).child("state").setValue(state.getText().toString());
                reference.child(username).child("phone_no").setValue(phone.getText().toString());
                reference.child(username).child("nrc_number").setValue(etNrc.getText().toString().trim());

                phone.setEnabled(false);
                email.setEnabled(false);
                address.setEnabled(false);
                state.setEnabled(false);
                city.setEnabled(false);
                etNrc.setEnabled(false);
                save_btn.setVisibility(View.GONE);
            }
        });
    }

    private void setProfile() {
        ProfileInterface myProfile = ProfileRetrofit.getProfile().create(ProfileInterface.class);
        Call<ProfileData> profileDataCall = myProfile.getProfileData(UserDetailData.userAccountID);
        profileDataCall.enqueue(new Callback<ProfileData>() {
            @Override
            public void onResponse(Call<ProfileData> call, Response<ProfileData> response) {
                ProfileData profileData=response.body();
                myprofiledata = profileData.getNrcPhotos();
                profileStar = myprofiledata.get(0).getStar();
                if(profileStar != null){
                    switch (profileStar){
                        case 1:
                            star1.setVisibility(View.VISIBLE);

                            star2.setVisibility(View.INVISIBLE);
                            star3.setVisibility(View.INVISIBLE);
                            star4.setVisibility(View.INVISIBLE);
                            star5.setVisibility(View.INVISIBLE);
                            break;
                        case 2:
                            star1.setVisibility(View.VISIBLE);
                            star2.setVisibility(View.VISIBLE);

                            star3.setVisibility(View.INVISIBLE);
                            star4.setVisibility(View.INVISIBLE);
                            star5.setVisibility(View.INVISIBLE);
                            break;
                        case 3:
                            star1.setVisibility(View.VISIBLE);
                            star2.setVisibility(View.VISIBLE);
                            star3.setVisibility(View.VISIBLE);

                            star4.setVisibility(View.INVISIBLE);
                            star5.setVisibility(View.INVISIBLE);
                            break;
                        case 4:
                            star1.setVisibility(View.VISIBLE);
                            star2.setVisibility(View.VISIBLE);
                            star3.setVisibility(View.VISIBLE);
                            star4.setVisibility(View.VISIBLE);

                            star5.setVisibility(View.INVISIBLE);
                            break;
                        case 5:
                            star1.setVisibility(View.VISIBLE);
                            star2.setVisibility(View.VISIBLE);
                            star3.setVisibility(View.VISIBLE);
                            star4.setVisibility(View.VISIBLE);
                            star5.setVisibility(View.VISIBLE);
                            break;
                        default:
                            star1.setVisibility(View.INVISIBLE);
                            star2.setVisibility(View.INVISIBLE);
                            star3.setVisibility(View.INVISIBLE);
                            star4.setVisibility(View.INVISIBLE);
                            star5.setVisibility(View.INVISIBLE);
                            break;
                    }
                }else{
                    star1.setVisibility(View.INVISIBLE);
                    star2.setVisibility(View.INVISIBLE);
                    star3.setVisibility(View.INVISIBLE);
                    star4.setVisibility(View.INVISIBLE);
                    star5.setVisibility(View.INVISIBLE);
                }
                front_img="http://54.255.138.233/file/nrc_front/"+myprofiledata.get(0).getNrc_front_photo();
                back_img="http://54.255.138.233/file/nrc_back/"+myprofiledata.get(0).getNrc_back_photo();
                Picasso.get().load(front_img).into(frontimage);
                Picasso.get().load(back_img).into(backimage);
                Log.d("GalleryFrg Data",""+myprofiledata.get(0).getNrc_front_photo());
            }
            @Override
            public void onFailure(Call<ProfileData> call, Throwable t) {
                Log.d("GalleryFrg Data", "" +t.getMessage());
            }
        });
    }
}