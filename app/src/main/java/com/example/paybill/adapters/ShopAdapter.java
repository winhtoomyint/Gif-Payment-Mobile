package com.example.paybill.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paybill.R;
import com.example.paybill.Retrofit.Invoice;


import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {
    Context context;

    public ShopAdapter(Context context ){

        this.context=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_shoplist, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {


    }

    @Override
    public int getItemCount() {
        return 20;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView town,township,shopname;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
