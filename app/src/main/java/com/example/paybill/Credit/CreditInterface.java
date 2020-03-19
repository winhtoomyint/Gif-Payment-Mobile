package com.example.paybill.Credit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CreditInterface {
    @Headers(
            {       "X-KillBill-ApiKey:hELL-0xxxx-12345",
                    "X-KillBill-ApiSecret:100-200-300-400-500",
                    "Authorization:Basic YWRtaW46cGFzc3dvcmQ=",
                    "Content-Type:application/json",
                    "Accept:application/json",
                    "X-KillBill-CreatedBy:admin",
            })

    @POST("1.0/kb/credits")
    Call<Void> createCredit(@Query("autoCommit") boolean autoCommit,
                            @Body Credit credit);
}
