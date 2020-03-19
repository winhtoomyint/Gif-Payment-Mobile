package com.example.paybill.Charge;

import com.example.paybill.Utils.UserDetailData;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChargeRetrofit {
  //  public static final String BASE_URL = "http://52.15.199.209:8080/";
    public static final String BASE_URL = UserDetailData.baseurl;
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
