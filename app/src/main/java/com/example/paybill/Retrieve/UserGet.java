package com.example.paybill.Retrieve;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface UserGet {
        @Headers({
                "X-KillBill-ApiKey:hELL-0xxxx-12345",
                "X-KillBill-ApiSecret:100-200-300-400-500",
                "Authorization:Basic YWRtaW46cGFzc3dvcmQ=",
        })

        @GET
        Call<UserData>  getUserDetail(@Url String path);



}
