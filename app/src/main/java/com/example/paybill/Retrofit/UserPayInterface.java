package com.example.paybill.Retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface UserPayInterface {
    @Headers({
            "X-KillBill-ApiKey:hELL-0xxxx-12345",
            "X-KillBill-ApiSecret:100-200-300-400-500",
            "Authorization:Basic YWRtaW46cGFzc3dvcmQ=",
    })

    @GET("1.0/kb/accounts/{id}/invoices")
    Call<List<Invoice>> getPayList(@Path("id") String accid, @Query("withItems") Boolean flag);

}
