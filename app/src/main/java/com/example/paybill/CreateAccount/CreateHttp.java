package com.example.paybill.CreateAccount;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateHttp {
    public static final String BASE_URL = "http://54.179.162.230:8080/";
    private static Retrofit retrofit = null;


    public static Retrofit createClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
