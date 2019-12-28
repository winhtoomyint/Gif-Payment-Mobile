package com.example.paybill.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paybill.R;
import com.example.paybill.Retrofit.Invoice;
import com.example.paybill.ui.HistoryDetail;


import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    Context context;
    List<Invoice> data;
    public int adjest,amu;
    public HistoryAdapter(Context context, List<Invoice> data){
        this.data=data;
        this.context=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.view_item_history, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        final Invoice invoice = data.get(i);
        final List<Invoice.invoice_desc> invoice_descs= data.get(i).invoice_detail;
        /*String currency = "MMK";
        currency = invoice.getCurrency();*/
        holder.date.setText(""+invoice.getInvoiceDate());
        holder.description.setText(""+invoice_descs.get(0).getDescription());
        holder.adj.setText(invoice.getCreditAdj() + " " + invoice.getCurrency());
        holder.amount.setText(""+invoice.getAmount() + " " + invoice.getCurrency());
        //holder.curency.setText(""+invoice.getCurrency());
        adjest+=invoice.getCreditAdj();
        amu+=invoice.getAmount();

        Log.d("Total Adj",adjest+"*"+amu);
        holder.hist_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent detail=new Intent(context, HistoryDetail.class);
                    detail.putExtra("Date",invoice.getInvoiceDate()+"");
                    detail.putExtra("Description",invoice_descs.get(0).getDescription()+"");
                    detail.putExtra("Amount",invoice.getCreditAdj()+"");
                    context.startActivity(detail);
                }catch (Exception e){
                    Log.e("Comming Error",e.getMessage().toString());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, description,amount,adj;
        CardView hist_card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=(TextView)itemView.findViewById(R.id.date_his);
            description =(TextView)itemView.findViewById(R.id.description_his);
            //curency = itemView.findViewById(R.id.currency_his);
            amount=(TextView)itemView.findViewById(R.id.amount_his);
            adj=(TextView)itemView.findViewById(R.id.adj_his);
            hist_card=(CardView)itemView.findViewById(R.id.history_card);
        }
    }
}
