package com.example.paybill.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.paybill.R;
import com.example.paybill.Retrieve.UserData;
import com.example.paybill.Retrieve.UserGet;
import com.example.paybill.Retrieve.UserRetrofit;
import com.example.paybill.Retrofit.Invoice;
import com.example.paybill.Retrofit.UserClient;
import com.example.paybill.Retrofit.UserPayInterface;
import com.example.paybill.Utils.ConfigUtils;
import com.example.paybill.Utils.Helper;
import com.example.paybill.Utils.UserDetailData;
import com.example.paybill.adapters.HistoryAdapter;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.paybill.Utils.UserDetailData.location;
import static com.example.paybill.Utils.UserDetailData.userAccountID;
import static com.example.paybill.Utils.UserDetailData.userExternalKey;

public class HomeFragment extends Fragment {
    RecyclerView rvHistory;
    HistoryAdapter adapter;
    UserPayInterface userPayInterface;
    UserGet userGet;
    List<Invoice> invoices;
    TextView textView;
    TextView amount,spent,adj_pay,date_txt;
    FirebaseDatabase database;
    DatabaseReference myRef;
    SwipeRefreshLayout swipeRefreshLayout;
    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);


        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spent=(TextView)view.findViewById(R.id.tv_spent);
        adj_pay=(TextView)view.findViewById(R.id.tv_income);
        amount=(TextView)view.findViewById(R.id.tv_amount);
        date_txt =(TextView)view.findViewById(R.id.tv_date);
        SimpleDateFormat formatter= new SimpleDateFormat("dd-MM,YYYY");
        Date date = new Date(System.currentTimeMillis());
        date_txt.setText(formatter.format(date));
        textView=(TextView)view.findViewById(R.id.tv_name);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.my_swip_lyt);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataFetching();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        textView.append(" "+UserDetailData.username);
        Firebase.setAndroidContext(getContext());
        rvHistory=(RecyclerView)view.findViewById(R.id.rv_history);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.d(TAG,"Call HomeFragment");
        if(userAccountID!=null) {
            dataFetching();
        }
    }

    private void dataFetching() {
//       // Collections.shuffle(invoices, new Random(System.currentTimeMillis()));
//        List<Invoice> data = new ArrayList<>();
//        for (int i = invoices.size() - 1; i >= 0; i--) {
//            data.add(invoices.get(i));
//        }
//        adapter = new HistoryAdapter(getContext(), data);
//        rvHistory.setAdapter(adapter);
        userPayInterface = UserClient.getClient().create(UserPayInterface.class);
        Call<List<Invoice>> call = userPayInterface.getPayList(userAccountID, true);
        call.enqueue(new Callback<List<Invoice>>() {
            @Override
            public void onResponse(Call<List<Invoice>> call, Response<List<Invoice>> response) {
                int ad = 0, am = 0;
                invoices = response.body();

                for (Invoice a : invoices) {
                    ad += a.getCreditAdj();

                }
                for (Invoice b : invoices) {
//                        am += b.getAmount();
//                        adj_pay.setText("a" + am);
                }
                for(int i=0;i<invoices.size();i++){
                    if (invoices.get(i).getStatus().equals("COMMITTED")){
                        am += invoices.get(i).getAmount();
                    }

                    adj_pay.setText(""+am);
                }
                int add_amu = am + ad;
                spent.setText("" + add_amu);
                amount.setText("" + ad);
                List<Invoice> data = new ArrayList<>();
                for (int i = invoices.size() - 1; i >= 0; i--)
                {
                    data.add(invoices.get(i));
                }
                adapter = new HistoryAdapter(getContext(), data);

                rvHistory.setAdapter(adapter);
                //List<InvoiceForNumber.invoice_desc> invoice_descs= data.get(0).invoice_detail;
                // Log.d("OkData",invoices.get(0).getAmount()+""+invoice_descs.get(0).getDescription() );
            }

            @Override
            public void onFailure(Call<List<Invoice>> call, Throwable t) {

            }
        });
    }

//    private void getUserHistory(){
//        if(userAccountID!=null) {
//            userPayInterface = UserClient.getClient().create(UserPayInterface.class);
//            Call<List<InvoiceForNumber>> call = userPayInterface.getPayList(userAccountID, true);
//            call.enqueue(new Callback<List<InvoiceForNumber>>() {
//                @Override
//                public void onResponse(Call<List<InvoiceForNumber>> call, Response<List<InvoiceForNumber>> response) {
//                    int ad = 0, am = 0;
//                    invoices = response.body();
//
//                    for (InvoiceForNumber a : invoices) {
//                        ad += a.getCreditAdj();
//
//                    }
//                    for (InvoiceForNumber b : invoices) {
//                        am += b.getAmount();
//                        adj_pay.setText("" + am);
//                    }
//                    int add_amu = am + ad;
//                    spent.setText("" + add_amu);
//                    amount.setText("" + ad);
//                    List<InvoiceForNumber> data = new ArrayList<>();
//                    for (int i = invoices.size() - 1; i >= 0; i--) {
//                        data.add(invoices.get(i));
//                    }
//                    adapter = new HistoryAdapter(getContext(), data);
//
//                    rvHistory.setAdapter(adapter);
//                    //List<InvoiceForNumber.invoice_desc> invoice_descs= data.get(0).invoice_detail;
//                    // Log.d("OkData",invoices.get(0).getAmount()+""+invoice_descs.get(0).getDescription() );
//                }
//
//                @Override
//                public void onFailure(Call<List<InvoiceForNumber>> call, Throwable t) {
//
//                }
//            });
//        }
//    }
}