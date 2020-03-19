package com.example.paybill.ChargePhp;

import okhttp3.RequestBody;
import retrofit2.Call;

import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface TransactionInterface {
    @Multipart
    @POST("api/transaction/store")
    Call<Transaction> createChargePhp(
            @Part("option") RequestBody option,
            @Part("acc_id") RequestBody acc_id,
            @Part("invoice_id") RequestBody invoice_id,
            @Part("invoice_no") RequestBody invoice_no,
            @Part("amount") RequestBody amount,
            @Part("status") RequestBody status,
            @Part("invoice_date") RequestBody invoice_date,
            @Part("target_date") RequestBody target_date,
            @Part("narration") RequestBody narration

    );
}
