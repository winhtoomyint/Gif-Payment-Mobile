package com.example.paybill.ChargePhp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Charge_Of_Php {

    public static final String BASE_URL = "http://54.255.138.233/";
    private static Retrofit retrofit = null;


    public static Retrofit createCharge() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
