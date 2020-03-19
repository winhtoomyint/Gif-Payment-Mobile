package com.example.paybill.CreateAccount;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CreateRetrofit {
    @Headers({
            "X-KillBill-ApiKey:hELL-0xxxx-12345",
            "X-KillBill-ApiSecret:100-200-300-400-500",
            "Authorization:Basic YWRtaW46cGFzc3dvcmQ=",
            "Content-Type:application/json",
            "X-Killbill-CreatedBy:admin"
    })
    @POST("1.0/kb/accounts")
    Call<Void> createAccount(
            @Body Create create
    );
}
