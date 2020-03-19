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
import com.example.paybill.Utils.Mail;

import java.util.List;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.ViewHolder> {
    Context context;
    public List<Mail> data;
    public NotiAdapter(Context context, List<Mail> data){
        this.context=context;
        this.data=data;
    }
    @NonNull
    @Override
    public  ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_noti, parent, false);
         ViewHolder viewHolder = new  ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, int i) {
            Mail mail=data.get(i);
            holder.invoid.setText(mail.getAmount()+"");
    }

    @Override
    public int getItemCount() {
        Log.d("data size", ""+data.size());
        return data.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView invoid;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            invoid=(TextView)itemView.findViewById(R.id.inv_id);
        }
    }
}
