package com.example.paybill.ProfileCall;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProfileInterface {
    @GET("api/member/profile")
    Call<ProfileData> getProfileData(
            @Query("application_ssid") String member_id
    );
}
