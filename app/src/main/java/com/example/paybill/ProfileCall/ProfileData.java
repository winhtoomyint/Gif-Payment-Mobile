package com.example.paybill.ProfileCall;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileData {
    @SerializedName("status")
    public int status;

    @SerializedName("data")
    public List<NrcPhoto> nrcPhotos;

    public List<NrcPhoto> getNrcPhotos() {
        return nrcPhotos;
    }

    public int getStatus() {
        return status;
    }

    public ProfileData(int status, List<NrcPhoto> nrcPhotos) {
        this.status = status;
        this.nrcPhotos = nrcPhotos;
    }
}
