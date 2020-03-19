package com.example.paybill.ChargePhp;

import com.example.paybill.Utils.UserDetailData;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChargeInvoice {

    public static final String BASE_URL = UserDetailData.baseurl;
    private static Retrofit retrofit = null;


    public static Retrofit getInvoice() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
