package com.example.paybill.CreateAccount;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface CreateRetrofitPhp {
    @POST("api/member/reg")
    Call<PhpObject> createAccountPhp(
            @Body PhpObject phpObject
    );
}
