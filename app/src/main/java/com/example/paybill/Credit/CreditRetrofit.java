package com.example.paybill.Credit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreditRetrofit {
    public static final String BASE_URL = "http://54.179.162.230:8080/";
    private static Retrofit retrofit = null;


    public static Retrofit getUser() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
