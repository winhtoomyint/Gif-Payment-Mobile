package com.example.paybill.Charge;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChargeInterface {
    @Headers(
            {       "X-KillBill-ApiKey:ApexPayMentHinThar",
                    "X-KillBill-ApiSecret:app10000",
                    "Authorization:Basic YWRtaW46cGFzc3dvcmQ=",
                    "Content-Type:application/json",
                    "Accept:application/json",
                    "X-KillBill-CreatedBy:admin",
                    "X-KillBill-Comment:Test Comment",
                    "X-KillBill-Reason:Test Reason"
            })

    @POST("1.0/kb/invoices/charges/{accountId}")
    Call<List<Charge>> createCharge(@Path("accountId") String accountId,
                                    @Query("autoCommit") boolean autoCommit,
                                    @Body List<Charge> charge);

}
