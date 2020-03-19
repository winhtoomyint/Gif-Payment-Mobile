package com.example.paybill.ui;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paybill.Credit.Credit;
import com.example.paybill.Credit.CreditInterface;
import com.example.paybill.Credit.CreditRetrofit;
import com.example.paybill.R;
import com.example.paybill.Retrofit.UserClient;
import com.example.paybill.Utils.Mail;
import com.example.paybill.adapters.NotiAdapter;
import com.example.paybill.commits.CommitAvoidInterface;
import com.example.paybill.commits.CommitRetrofit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.paybill.Utils.UserDetailData.userAccountID;
import static com.example.paybill.Utils.UserDetailData.username;

public class NotiFragment extends AppCompatActivity {
    NotiAdapter notiAdapter;
    SwipeController swipeController = null;
    public List<Mail> mydata;RecyclerView recyclerView;

    FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        setContentView(R.layout.noti_frag);
        mydata=new ArrayList<>();
        recyclerView= (RecyclerView)findViewById(R.id.mynotilist);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        setupData();


        setupRecyclerView();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.noti_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.mymail:
                notiAdapter.data.clear();
                notiAdapter.notifyDataSetChanged();

                Toast.makeText(this, "Clear All", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupRecyclerView() {



        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                avoidInvoice(mydata.get(position).getInvoiceId(), position);
                /*notiAdapter.data.remove(position);
                notiAdapter.notifyItemRemoved(position);
                notiAdapter.notifyItemRangeChanged(position, notiAdapter.getItemCount());*/
            }

            @Override
            public void onLeftClicked(int i) {
                //Accept function
                commitInvoice(mydata.get(i).getInvoiceId(),i);
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }
    private void setupData(){
        //removeFirebaseData("271bc039-c904-4b93-ad1d-7c7144e2262a");
        db.collection(userAccountID.trim())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mydata.add(new Mail(document.getId(),document.get("amount").toString(),document.get("description").toString()));
                                Log.d("ListData", "onComplete: "+mydata.get(i).getInvoiceId());
                                i++;
                            }
                            Log.d("size", "onComplete: "+mydata.size());
                            notiAdapter=new NotiAdapter(getApplicationContext(),mydata);
                            recyclerView.setAdapter(notiAdapter);

                        } else {
                            Log.d("Data Test", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }
    /*private void accept(String invoiceId){
        commitInvoice(invoiceId);
    }*/

    /*private void decline(String invoiceId){
        avoidInvoice(invoiceId);
    }*/

    private void removeFirebaseData(String invoiceId, int position){
        db.collection(userAccountID.trim()).document(invoiceId.trim()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(NotiFragment.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NotiFragment.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
        notiAdapter.data.remove(position);
        notiAdapter.notifyItemRemoved(position);
        notiAdapter.notifyItemRangeChanged(position, notiAdapter.getItemCount());
    }

    private void commitInvoice(String invoiceId, int position){
        final int i = position;
        CommitAvoidInterface commitAvoidInterface = CommitRetrofit.getUser().create(CommitAvoidInterface.class);
        Call<Void> call = commitAvoidInterface.commitInvoice(invoiceId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                createCreditOnOtherAccount(i);
                Toast.makeText(NotiFragment.this, "Accepted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(NotiFragment.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void avoidInvoice(String invoiceId, int position){
        final String iid = invoiceId;
        final int i = position;
        CommitAvoidInterface commitAvoidInterface = CommitRetrofit.getUser().create(CommitAvoidInterface.class);
        Call<Void> call = commitAvoidInterface.avoidInvoice(invoiceId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                /*if(!response.isSuccessful()){
                    Toast.makeText(NotiFragment.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.d("Error avoid", "onResponse: " + response.code());
                    return;
                }*/
                Toast.makeText(NotiFragment.this, "Success", Toast.LENGTH_SHORT).show();
                removeFirebaseData(iid, i);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(NotiFragment.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createCreditOnOtherAccount(int position){
        final int i = position;
        CreditInterface crdInterface = CreditRetrofit.getUser().create(CreditInterface.class);
        Credit credit = new Credit(Double.valueOf(mydata.get(position).getAmount()), userAccountID.trim(), mydata.get(position).getDescription());
        Call<Void> creditCall = crdInterface.createCredit(true,credit);
        creditCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!response.isSuccessful())
                {
                    Toast.makeText(NotiFragment.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                removeFirebaseData(mydata.get(i).getInvoiceId(), i);
                //Log.d(TAG, "onResponse: "+response.headers().get("Location"));
                Log.d("Response", "onResponse: createCredit success");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //Log.d(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(NotiFragment.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
