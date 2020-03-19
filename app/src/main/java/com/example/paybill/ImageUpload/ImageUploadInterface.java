package com.example.paybill.ImageUpload;

import com.example.paybill.ChargePhp.Transaction;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ImageUploadInterface {
    @Multipart
    @POST("api/member/reg")
    Call<ImageData> uploadImageData(
            @Part("application_ssid") RequestBody application_ssid,
            @Part("username") RequestBody username,
            @Part("email") RequestBody email,
            @Part("address") RequestBody address,
            @Part("township") RequestBody township,
            @Part("state") RequestBody state,
            @Part("phone_no") RequestBody phone_no,
            @Part("password") RequestBody password,
            @Part("f_image") RequestBody f_image,
            @Part("n_image") RequestBody n_image

    );
}
