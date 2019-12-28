package com.example.paybill.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.paybill.R;
import com.example.paybill.Utils.UserDetailData;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    TextView acc_id,name,phone,email,address,state,city;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        acc_id=(TextView)view.findViewById(R.id.tv_account_number);
        name=(TextView)view.findViewById(R.id.tv_name);
        phone=(TextView)view.findViewById(R.id.tv_phno);
        email=(TextView)view.findViewById(R.id.tv_email);
        address=(TextView)view.findViewById(R.id.tv_address);
        state=(TextView)view.findViewById(R.id.tv_state);
        city=(TextView)view.findViewById(R.id.tv_city);

        acc_id.setText(""+ UserDetailData.userAccountID);
        name.setText(""+ UserDetailData.username);
        phone.setText(""+ UserDetailData.phone);
        email.setText(""+ UserDetailData.email_address);
        address.setText(""+ UserDetailData.address);
        state.setText(""+ UserDetailData.state);
        city.setText(""+ UserDetailData.city);

    }
}