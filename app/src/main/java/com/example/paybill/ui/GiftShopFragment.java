package com.example.paybill.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paybill.R;
import com.example.paybill.adapters.ShopAdapter;

public class GiftShopFragment extends Fragment {
    RecyclerView myshops;
    ShopAdapter shopAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.gift_shop_locationfrag,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myshops=(RecyclerView)view.findViewById(R.id.myshop_list);
        myshops.setLayoutManager(new LinearLayoutManager(getContext()));
        shopAdapter=new ShopAdapter(getContext());
        myshops.setAdapter(shopAdapter);
    }
}
