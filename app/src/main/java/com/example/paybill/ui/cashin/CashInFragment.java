package com.example.paybill.ui.cashin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.paybill.Credit.Credit;
import com.example.paybill.Credit.CreditInterface;
import com.example.paybill.Credit.CreditRetrofit;
import com.example.paybill.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.paybill.Utils.UserDetailData.userAccountID;

public class CashInFragment extends Fragment {

    private EditText etAmount;
    private EditText etDescription;
    private Button btn_cashIn;

    private static final String TAG = "CashInFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cash_in,container,false);
        etAmount = view.findViewById(R.id.et_amount);
        etDescription = view.findViewById(R.id.et_description);
        btn_cashIn = view.findViewById(R.id.button_cash_in);
        btn_cashIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cashIn();
            }
        });
        return view;
    }

    private void cashIn(){
        if(etAmount.getText().toString().trim().isEmpty() || etDescription.toString().trim().isEmpty()){
            Toast.makeText(getContext(), "Please fill the amount and description", Toast.LENGTH_SHORT).show();
            return;
        }
        CreditInterface crdInterface = CreditRetrofit.getUser().create(CreditInterface.class);
        Credit credit = new Credit(
                Double.valueOf(etAmount.getText().toString().trim()),
                userAccountID,
                etDescription.getText().toString().trim());
        Call<Void> creditCall = crdInterface.createCredit(true,credit);
        creditCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!response.isSuccessful())
                {
                    Log.d(TAG, "onResponse: "+response.code());
                    return;
                }
                //Log.d(TAG, "onResponse: "+response.headers().get("Location"));
                Log.d(TAG, "onResponse: createCredit success");
                Toast.makeText(getContext(), "Cash in successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }
}
