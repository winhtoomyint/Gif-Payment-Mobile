package com.example.paybill.Retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface UserPayInterface {
    @Headers({
            "X-KillBill-ApiKey:ApexPayMentHinThar",
            "X-KillBill-ApiSecret:app10000",
            "Authorization:Basic dXNlcjE6dXNlcjE=",
    })

    @GET("1.0/kb/accounts/{id}/invoices")
    Call<List<Invoice>> getPayList(@Path("id") String accid, @Query("withItems") Boolean flag);

}
