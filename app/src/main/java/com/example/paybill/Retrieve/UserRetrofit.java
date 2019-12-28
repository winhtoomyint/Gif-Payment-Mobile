package com.example.paybill.Retrieve;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRetrofit {

    public static final String BASE_URL = "http://52.15.199.209:8080/";
    private static Retrofit retrofit = null;


    public static Retrofit getCLient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;

    }
}
